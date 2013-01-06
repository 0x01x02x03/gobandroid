package org.ligi.gobandroid_hd.ui.tsumego;

import java.util.Vector;

import org.ligi.android.common.dialogs.ActivityFinishOnDialogClickListener;
import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.gobandroid_beta.R;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.logic.GoGame.GoGameChangeListener;
import org.ligi.gobandroid_hd.logic.GoMove;
import org.ligi.gobandroid_hd.ui.GoActivity;
import org.ligi.gobandroid_hd.ui.review.SGFMetaData;
import org.ligi.tracedroid.logging.Log;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class TsumegoActivity extends GoActivity implements GoGameChangeListener {

	private GoMove finishing_move;

	private TsumegoGameExtrasFragment myTsumegoExtrasFragment;
	private Vector<GoMove> on_path_moves;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setTitle(R.string.tsumego);

		on_path_moves = new Vector<GoMove>();

		if (getGame() == null) { // there was no game - fallback to main menu
			EasyTracker.getTracker().trackException("tsumego start getGame() returned null in onCreate", false);
			finish();
			// startActivity(new Intent(this, gobandroid.class));
			return;
		}

		// build a on path Vector to do a fast isOnPath() later
		recursive_add_on_path_moves(getGame().getFirstMove());

		// try to find the correct solution
		if (!isFinishingMoveKnown())
			new AlertDialog.Builder(this).setMessage(R.string.tsumego_sgf_no_solution).setNegativeButton(R.string.ok, new DialogDiscarder()).setPositiveButton(R.string.go_back, new ActivityFinishOnDialogClickListener(this)).show();

		getGame().addGoGameChangeListener(this);

		float myZoom = TsumegoHelper.calcZoom(getGame(),true);

		getBoard().setZoom(myZoom);
		getBoard().setZoomPOI(TsumegoHelper.calcPOI(getGame(),true));
	}

	private GoMove getFinishingMove() {
		if (finishing_move == null)
			finishing_move = getCorrectMove(getGame().getFirstMove());

		return finishing_move;
	}

	private boolean isFinishingMoveKnown() {
		return getFinishingMove() != null;
	}

	private void recursive_add_on_path_moves(GoMove act) {
		on_path_moves.add(act);
		if (act.hasNextMove())
			for (GoMove child : act.getNextMoveVariations())
				recursive_add_on_path_moves(child);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (getGame() != null) // TODO investigate when game can be null here
			getGame().removeGoGameChangeListener(this);
	}

	private boolean isOnPath() {
		return on_path_moves.contains(getGame().getActMove());
	}

	public byte doMoveWithUIFeedback(byte x, byte y) {

		byte res = super.doMoveWithUIFeedback(x, y);

		// if the move was valid and we have a counter move -> we will play it
		if (res == GoGame.MOVE_VALID)
			if (getGame().getActMove().hasNextMove()) {
				getGame().jump(getGame().getActMove().getnextMove(0));
			}

		getGame().notifyGameChange();
		return res;
	}

	private GoMove getCorrectMove(GoMove act_mve) {
		if (isCorrectMove(act_mve))
			return act_mve;

		for (GoMove next_moves : act_mve.getNextMoveVariations()) {
			GoMove res = getCorrectMove(next_moves);
			if (res != null)
				return res;
		}

		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		if (getGame() == null) { // there was no game - fallback to main menu
			EasyTracker.getTracker().trackException("tsumego start getGame() returned null in onCreate", false);
			finish();
			// startActivity(new Intent(this, gobandroid.class));
			return super.onCreateOptionsMenu(menu);
		}

		this.getSupportMenuInflater().inflate(R.menu.ingame_tsumego, menu);
		menu.findItem(R.id.menu_game_hint).setVisible(isFinishingMoveKnown() && isOnPath());
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (!super.onOptionsItemSelected(item))
			switch (item.getItemId()) {

			case R.id.menu_game_hint:

				TsumegoHintAlert.show(this, getFinishingMove());
				break;
			}

		return false;
	}

	@Override 
	public void initializeStoneMove() {
		// disable stone move - not wanted in tsumego
	}
	
	
	@Override
	public void requestUndo() {
		getBoard().move_stone_mode = false;
		// we do not want to keep user-variations in tsumego mode- but we want
		// to keep tsumego variation
		getGame().undo(isOnPath());

		// remove the counter-move if any
		if (!getGame().isBlackToMove())
			getGame().undo(isOnPath());
	}

	@Override
	public Fragment getGameExtraFragment() {
		// if(myTsumegoExtrasFragment==null)
		myTsumegoExtrasFragment = new TsumegoGameExtrasFragment();

		return myTsumegoExtrasFragment;
	}

	private boolean isCorrectMove(GoMove move) {
		return (move.getComment().trim().toUpperCase().startsWith("CORRECT") || // gogameguru
																				// style
		// act_mve.getComment().trim().toUpperCase().startsWith("RIGHT") || //
		// goproblem.com
		move.getComment().contains("RIGHT") // goproblem.com
		);
	}

	@Override
	public void onGoGameChange() {
		super.onGoGameChange();
		if (myTsumegoExtrasFragment != null) {
			myTsumegoExtrasFragment.setOffPathVisibility(!isOnPath());
			myTsumegoExtrasFragment.setCorrectVisibility(isCorrectMove(getGame().getActMove()));
		}
		if (isCorrectMove(getGame().getActMove())) {
			SGFMetaData meta = new SGFMetaData(getGame().getMetaData().getFileName());
			meta.setIsSolved(true);
			meta.persist();
			/*
			 * this.getBaseContext().getSharedPreferences("tsumego_stats",
			 * Activity.MODE_PRIVATE)
			 * .edit().putInt(game.getMetaData().getFileName(), 100).commit();
			 */
			Log.i("written finished" + getGame().getMetaData().getFileName());
		}
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				invalidateOptionsMenu();
			}
		});
	}

	@Override
	public boolean isAsk4QuitEnabled() {
		return false;
	}

}

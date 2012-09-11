package org.ligi.gobandroid_hd.ui.editing;

import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.logic.GoGame.GoGameChangeListener;
import org.ligi.gobandroid_hd.logic.markers.CircleMarker;
import org.ligi.gobandroid_hd.logic.markers.GoMarker;
import org.ligi.gobandroid_hd.logic.markers.SquareMarker;
import org.ligi.gobandroid_hd.logic.markers.TriangleMarker;
import org.ligi.gobandroid_hd.ui.GoActivity;

import com.actionbarsherlock.view.Menu;

import android.os.Bundle;
import android.view.WindowManager;

/**
 * Activity to record a Game - or play on one device
 * 
 * @author ligi
 * 
 */
public class EditGameActivity extends GoActivity implements
		GoGameChangeListener {

	private EditModeItemPool editModePool=new EditModeItemPool();
	private int last_letter_id=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO the next line works but needs investigation - i thought more of
		// getBoard().requestFocus(); - but that was not working ..
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private int findNextNumber() {
		for (int i=1;i<99;i++) {
			boolean found=false;
			for (GoMarker marker:getGame().getActMove().getMarkers())
				found|=marker.getText().equals(""+i);
			if (!found)
				return i;
		}
		return 0; // should not happen - only if a hundret markers			
	}
	
	private String findNextLetter() {
		for (int i=0;i<23;i++) {
			boolean found=false;
			for (GoMarker marker:getGame().getActMove().getMarkers())
				found|=marker.getText().equals(""+(char)('A'+i));
			if (!found)
				return ""+(char)('A'+i);
		}
		return "a"; // should not happen - only if a hundret markers			
	}
	
	@Override
	public byte doMoveWithUIFeedback(byte x, byte y) {
		
		// if it is a marker we have to check if there is one allready and remove
		switch (getMode()) {
		case TRIANGLE:
		case SQUARE:
		case CIRCLE:
		case NUMBER:
		case LETTER:
			// remove markers with same coordinates
			GoMarker marker2remove=null;
			for (GoMarker marker: getGame().getActMove().getMarkers()) {
				if (marker.getX()==x && marker.getY()==y)
					marker2remove=marker;
			}
			getGame().getActMove().getMarkers().remove(marker2remove);
			
		}
		
		switch (getMode()) {
		case BLACK:
			if (getGame().getHandicapBoard().isCellBlack(x, y))
				getGame().getHandicapBoard().setCellFree(x, y);
			else
				getGame().getHandicapBoard().setCellBlack(x, y);
			getGame().jump(getGame().getActMove()); // we need to totally refresh the board
			break;
		case WHITE:
			if (getGame().getHandicapBoard().isCellWhite(x, y))
				getGame().getHandicapBoard().setCellFree(x, y);
			else
				getGame().getHandicapBoard().setCellWhite(x, y);
			getGame().jump(getGame().getActMove()); // we need to totally refresh the board
			break;				
		case TRIANGLE:
			getGame().getActMove().getMarkers().add(new TriangleMarker(x,y));
			break;
		case SQUARE:
			getGame().getActMove().getMarkers().add(new SquareMarker(x,y));
			break;
		case CIRCLE:
			getGame().getActMove().getMarkers().add(new CircleMarker(x,y));
			break;
		
		case NUMBER:
			getGame().getActMove().getMarkers().add(new GoMarker(x,y,""+findNextNumber()));
			break;
			
		case LETTER:
			getGame().getActMove().getMarkers().add(new GoMarker(x,y,""+findNextLetter()));
			last_letter_id++;
			break;
		
		}
		getGame().notifyGameChange();
		return GoGame.MOVE_VALID;
	}

	private EditGameMode getMode() {
		return editModePool.getActMode();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		try {
			menu.findItem(R.id.menu_game_pass).setVisible(
					!getGame().isFinished());
			/*menu.findItem(R.id.menu_game_results).setVisible(
					getGame().isFinished());*/
			menu.findItem(R.id.menu_game_undo).setVisible(getGame().canUndo());
		} catch (NullPointerException e) {
		} // we do not care when they do not exist

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.ingame_record, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onGoGameChange() {
		super.onGoGameChange();
		this.invalidateOptionsMenu();
	}

	@Override
	public EditGameExtrasFragment getGameExtraFragment() {
		return new EditGameExtrasFragment(editModePool);
	}

}

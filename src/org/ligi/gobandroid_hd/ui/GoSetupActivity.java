/**
 * gobandroid 
 * by Marcus -Ligi- Bueschleb 
 * http://ligi.de
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as 
 * published by the Free Software Foundation; 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 **/

package org.ligi.gobandroid_hd.ui;

import org.ligi.android.common.intents.IntentHelper;
import org.ligi.gobandroid_hd.InteractionScope;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GnuGoMover;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity;
import org.ligi.gobandroid_hd.ui.recording.GameRecordActivity;
import org.ligi.gobandroid_hd.ui.gnugo.PlayAgainstGnugoActivity;
import org.ligi.tracedroid.logging.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;


/**
 * Activity for setting up a game
 * 
 * TODO needs cleaning
 * 
 * @author <a href="http://ligi.de">Marcus -Ligi- Bueschleb</a>
 * 
 *         This software is licensed with GPLv3
 * 
 **/

public class GoSetupActivity extends GobandroidFragmentActivity implements OnSeekBarChangeListener, OnClickListener, OnItemSelectedListener{

	private byte act_size=9;
	private byte act_handicap=0;
	
	private final static int size_offset=2;
	 
	private SeekBar size_seek;
	private SeekBar handicap_seek;
	
	private TextView size_text;
	private Button size_button9x9;
	private Button size_button13x13;
	private Button size_button19x19;
	
	private Spinner black_player_spinner;
	private Spinner white_player_spinner;

	private GoBoardViewHD board;
	
	private TextView handicap_text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		GoPrefs.init(this);
		this.setContentView(R.layout.game_setup);
		this.setTitle(R.string.board_setup);
		this.getSupportActionBar().setSubtitle(R.string.for_recording);
	}
	
	private void setup_board() {
		board=findById(R.id.go_board);
		
		if (board==null) 
			return;
		
		board.setOnTouchListener(new OnTouchListener() {
		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				start_game();
				return false;
			}
		});
		
		board.do_actpos_highlight=false;
		board.do_legend=false;
		board.legend_sgf_mode=false;
		board.setFocusable(false); // we are not here to interact
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("GoSetup Resume");
		
		setup_board();
		
		size_seek=findById(R.id.size_slider);
		size_seek.setOnSeekBarChangeListener(this);
		
		size_text=findById(R.id.game_size_label);
		
		size_button9x9=findById(R.id.size_button9x9);
		size_button9x9.setOnClickListener(this);


		((Button)findById(R.id.InstallAIButton)).setOnClickListener(this);
		
		size_button13x13=findById(R.id.size_button13x13);
		size_button13x13.setOnClickListener(this);

		
		size_button19x19=findById(R.id.size_button19x19);
		size_button19x19.setOnClickListener(this);
		
		
		black_player_spinner=findById(R.id.BlackPlayerSpinner);
	
		String[] player_strings;
		if (IntentHelper.isIntentAvailable(this.getPackageManager() ,new Intent(GnuGoMover.intent_action_name)))
			player_strings=new String[] {"Phone","GnuGo"};
		else
			player_strings=new String[] {"Phone"};
		
		ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(this,
	            android.R.layout.simple_spinner_item , player_strings);

 		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

 		white_player_spinner=findById(R.id.WhitePlayerSpinner);
 		
		black_player_spinner.setAdapter(spinner_adapter);
		white_player_spinner.setAdapter(spinner_adapter);
		
		white_player_spinner.setOnItemSelectedListener(this);
		black_player_spinner.setOnItemSelectedListener(this);
		
		// set the default selection 
		int spinner_pos=spinner_adapter.getPosition(GoPrefs.getLastPlayerBlack());
		if (spinner_pos==-1)
			spinner_pos=0;
				
		black_player_spinner.setSelection(spinner_pos,true);
		
		spinner_pos=spinner_adapter.getPosition(GoPrefs.getLastPlayerWhite());
			if (spinner_pos==-1)
				spinner_pos=0;
					
		white_player_spinner.setSelection(spinner_pos,true);

		handicap_text=findById(R.id.handicap_label);
		handicap_seek=findById(R.id.handicap_seek);
		handicap_seek.setOnSeekBarChangeListener(this);
		
		// set defaults
		act_size=(byte)GoPrefs.getLastBoardSize();
		act_handicap=(byte)GoPrefs.getLastHandicap();
		
		refresh_ui();
	}
	
	
	/**
	 * refresh the ui elements with values from act_size / act_handicap 
	 */
	public void refresh_ui() {
		size_text.setText(getString(R.string.size)+" "+act_size+"x"+act_size);
		handicap_text.setText(getString(R.string.handicap) +" "+ act_handicap);
	
		// the checks for change here are important - otherwise samsung moment will die here with stack overflow
		if ((act_size-size_offset)!=size_seek.getProgress())
			size_seek.setProgress(act_size-size_offset);
		
		if (act_handicap!=handicap_seek.getProgress())
			handicap_seek.setProgress(act_handicap);
		
		if (getApp().getInteractionScope().getMode()==InteractionScope.MODE_GNUGO)
			size_seek.setMax(19-size_offset);
		
		// only enable handicap seeker when the size is 9x9 or 13x13 or 19x19
		handicap_seek.setEnabled((act_size==9)||(act_size==13)||(act_size==19));
		
		GoPrefs.setLastBoardSize(act_size);
		GoPrefs.setLastHandicap(act_handicap);
		
		getApp().getInteractionScope().setGame(new GoGame(act_size,act_handicap));
		if (board!=null) {
			board.boardSizeChanged();
			board.invalidate();
		}
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if ((seekBar==size_seek)&&(act_size!=(byte)(progress+size_offset))) 
			act_size=(byte)(progress+size_offset);
		else if ((seekBar==handicap_seek)&&(act_handicap!=(byte)progress))
			act_handicap=(byte)progress;

		refresh_ui();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	@Override
	public void onClick(View v) {
		if (v==(findById(R.id.InstallAIButton))) {
			try {
				this.startActivity(new Intent().setAction(Intent.ACTION_VIEW)
						.setData(Uri.parse("market://search?q=pname:org.ligi.gobandroid.ai.gnugo")));
			}
			catch (Exception e) {
				new AlertDialog.Builder(this).setTitle(R.string.problem)
				.setMessage(
					R.string.android_market_problem
				).setPositiveButton(R.string.ok,  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
			
					}
				}).setCancelable(true).show();
			}
		}
		else 
			if (v==size_button9x9)
				act_size=9;
			if (v==size_button13x13)
				act_size=13;
			else if (v==size_button19x19)
				act_size=19;
		
		refresh_ui();
	}
	
	private void start_game() {
		GoGame new_game=new GoGame(act_size,act_handicap);
		
		getApp().getInteractionScope().setGame(new_game);
		
		
		Intent go_intent;
		
		if (getApp().getInteractionScope().getMode()==InteractionScope.MODE_RECORD)
			go_intent=new Intent(this,GameRecordActivity.class);
		else
			go_intent=new Intent(this,PlayAgainstGnugoActivity.class);
		
		/*go_intent.putExtra("size",act_size );
		go_intent.putExtra("handicap",act_handicap );

		go_intent.putExtra("white_player",white_player_spinner.getSelectedItemPosition());
		go_intent.putExtra("black_player",black_player_spinner.getSelectedItemPosition());
		 */
		getTracker().trackPageView("/record/start?size="+act_size);
		startActivity(go_intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> selected_item, View selected_view, int pos,
			long arg3) {
		if (selected_item==white_player_spinner)
			GoPrefs.setLastPlayerWhite((String)selected_item.getAdapter().getItem(pos));
		if (selected_item==black_player_spinner)
			GoPrefs.setLastPlayerBlack((String)selected_item.getAdapter().getItem(pos));

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.game_setup, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.i("item sel");
		switch (item.getItemId()) {
		case R.id.menu_start:
			/*
			if ((act_size>19)&&((black_player_spinner.getSelectedItemPosition()!=0)||(white_player_spinner.getSelectedItemPosition()!=0)))
					{
						new AlertDialog.Builder(this).setTitle(R.string.problem)
						.setMessage(
    					 R.string.gnugo_size_problem
						).setPositiveButton(R.string.ok,  new DialogDiscarder()).setCancelable(true).show();
    			
					}
			else  */
				 start_game() ;

			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
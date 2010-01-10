package org.ligi.gobandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Activity for a Game
 * 
 * @author <a href="http://ligi.de">Marcus -Ligi- Bueschleb</a>
 *         
 * This software is licenced with GPLv3 
 * 
**/


public class GoActivity extends Activity {
    private static final int MENU_UNDO = 0;
    private static final int MENU_PASS = 1;
	private static final int MENU_FINISH = 2;

	GoGame game;
    View board_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        byte size=getIntent().getByteExtra("size",(byte)9);
        game=new GoGame(size);
        board_view=new GoBoardView(this,game);
        board_view.setOnTouchListener((OnTouchListener)board_view );
        setContentView(board_view);
    }
    
int i=0;
    /* Creates the menu items */
   public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
   
        if (!game.isFinished())
        {
        if (game.canUndo()) {
            MenuItem undo_menu=menu.add(0,MENU_UNDO,0,"Undo");
            undo_menu.setIcon(android.R.drawable.ic_menu_revert);
        }
        MenuItem pass_menu=menu.add(0,MENU_PASS,0,"Pass" );
        pass_menu.setIcon(android.R.drawable.ic_menu_set_as);
        }
        else {
        	MenuItem finish_menu=menu.add(0,MENU_FINISH,0,"Finished Marking dead Stones" );
        	finish_menu.setIcon(android.R.drawable.ic_menu_set_as);
            	
        	
        }
        
       return true;
    }

    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case MENU_UNDO:
                game.undo();
                break;
                
            case MENU_PASS:
                game.pass();
                break;
            
        }
        board_view.invalidate();
        return false;
    }

      
}
package org.ligi.gobandroid.logic;

import org.ligi.gobandroid.ai.gnugo.IGnuGoService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class GnuGoMover implements Runnable{

	private IGnuGoService gnu_service ;
	private GoGame game;
	

	private boolean gnugo_size_set;
	public boolean playing_black=false;
	public boolean playing_white=false;
	
	public final static String intent_action_name="org.ligi.gobandroid.ai.gnugo.GnuGoService";
	
	public GnuGoMover(Activity activity,GoGame game,boolean playing_black,boolean playing_white) {
		this.playing_black=playing_black;
		this.playing_white=playing_white;
		
		this.game=game;
		
        ServiceConnection conn = new ServiceConnection() {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				gnu_service = IGnuGoService.Stub.asInterface(service);
	
				try {
					Log.i("INFO", "Service bound "  + gnu_service.processGTP("test"));
				} catch (RemoteException e) {
				}
				
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.i("INFO", "Service unbound ");				
			}
        	
        	
        };
        
        activity.bindService(new Intent("org.ligi.gobandroid.ai.gnugo.GnuGoService"), conn, Context.BIND_AUTO_CREATE);
	
        new Thread(this).start();
	}
	
	public String coordinates2gtpstr(byte x,byte y)  {
	if (x>8) x++; // I is missing
	y=(byte)(game.getBoardSize()-(y));
	return ""+(char)('A'+x) + ""+(y);
	}

	public void processWhiteMove(byte x,byte y)   {
		try {
			gnu_service.processGTP("white " + coordinates2gtpstr(x,y));
		} catch (RemoteException e) {		}
	}
	
	public void processBlackMove(byte x,byte y)   {
		try {
			gnu_service.processGTP("black " + coordinates2gtpstr(x,y));
		} catch (RemoteException e) {		}
	}
	
	@Override
	public void run() {
		while ( true) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if (gnu_service==null)
				continue;
			
			if (!gnugo_size_set)
				try {
					gnu_service.processGTP("boardsize " + game.getBoardSize());
					gnugo_size_set=true;
				} catch (RemoteException e) {}
			
				if (game.isBlackToMove()&&(playing_black)) {
				try {
				
					String answer= gnu_service.processGTP("genmove black");
					GTPHelper.doMoveByGTPString(answer, game);
					Log.i("gobandroid", "gugoservice" + gnu_service.processGTP("showboard"));		
										
				} catch (RemoteException e) {
				}
			}
			if (!game.isBlackToMove()&&(playing_white)) {
				
				try {
					String answer= gnu_service.processGTP("genmove white");
					GTPHelper.doMoveByGTPString(answer, game);
					
					Log.i("gobandroid", "gugoservice" + gnu_service.processGTP("showboard"));
					
				} catch (RemoteException e) {
				}				
			}

		}
	}

	
}

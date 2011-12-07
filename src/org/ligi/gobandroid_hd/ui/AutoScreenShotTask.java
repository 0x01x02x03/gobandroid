package org.ligi.gobandroid_hd.ui;

import java.io.File;

import org.ligi.android.common.dialogs.DialogDiscarder;
import org.ligi.android.common.files.FileHelper;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GoGameProvider;
import org.ligi.gobandroid_hd.logic.SGFHelper;
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity;
import org.ligi.gobandroid_hd.ui.tsumego.TsumegoActivity;
import org.ligi.tracedroid.logging.Log;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;

/**
 * A task to do ScreenShots of GO-Boards/Situations to use as thumbnail to make a 
 * more intuitive and better looking interface 
 * 
 * @author ligi
 *
 */
public class AutoScreenShotTask extends AsyncTask<String,String,Integer> {

	private GobandroidFragmentActivity activity;
	private AlertDialog progress_dialog;
	GoBoardViewHD gbv;
	
	public AutoScreenShotTask(GobandroidFragmentActivity activity) {
		this.activity=activity;
	}
	
     protected void onPostExecute(Integer result) {
    	 progress_dialog.dismiss();
    	 String msg="No new Tsumegos found :-(";
    	 
    	 if (result>0)
    		 msg="Downloaded " + result + " new Tsumegos ;-)";
    	 
    	 //LinearLayout lin=new LinearLayout(activity);
    	
    	 new AlertDialog.Builder(activity).setMessage(msg)
    	 	.setTitle("Download Report")
    	 	//.setView(gbv)
    	 	.setPositiveButton("OK", new DialogDiscarder()).show();
     }

     protected void onProgressUpdate(String... progress) {
        progress_dialog.setMessage(progress[0]);
        gbv.invalidate();
     }

	@Override
	protected void onPreExecute() {
		
		View v=activity.getLayoutInflater().inflate(R.layout.screenshot_dialog, null);
		
		 gbv=(GoBoardViewHD)v.findViewById(R.id.board_to_shoot);//new GoBoardViewHD(activity);
    	 
		 
		 gbv.setBackgroundResource(R.drawable.shinkaya);
    	 gbv.grid_embos=false; // looks better when scaled down
    	 gbv.do_legend=false; // gets to small in thumb
    	 
    	 progress_dialog=new AlertDialog.Builder(activity).setView(v).show();
    	
    	super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... params) {
		processPath(activity.getSettings().getTsumegoPath());
		return 2;
	}		
	
	public void processPath(String path) {
	
		Log.i("processing "+ path);
	    File dir=new File(path);
		File[] files=dir.listFiles();
		
		Log.i("processing res "+ (dir==null) + " " + (files==null));
		if (files!=null) for (File file:files) {
			if (file!=null) {
				if (file.isDirectory()) {
					processPath(file.getPath());
				} else if (file.getName().endsWith(".sgf")) {
					String sgf_content=FileHelper.file2String(file);
					if ((sgf_content!=null)&&(!sgf_content.equals(""))) {
						GoGameProvider.setGame(SGFHelper.sgf2game(FileHelper.file2String(file), null));
						
						gbv.setZoom(TsumegoActivity.calcZoom(GoGameProvider.getGame()));
						gbv.setZoomPOI(TsumegoActivity.calcPOI(GoGameProvider.getGame()));
						
						try {
							Thread.sleep(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i("doing screenshot" + file.getName());
						gbv.screenshot(file.getPath()+".png");
						//this.publishProgress("foo");
						this.publishProgress("foo");
					}
					}
			}
		}

	}
}
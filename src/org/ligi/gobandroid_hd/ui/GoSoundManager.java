package org.ligi.gobandroid_hd.ui;

import java.util.HashMap;

import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity;
import org.ligi.tracedroid.logging.Log;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.media.MediaPlayer.OnCompletionListener;

public class GoSoundManager {
	 
	private  SoundPool mSoundPool;
	private  HashMap<Integer, Integer> mSoundPoolMap;
	private  AudioManager  mAudioManager;
	private  GobandroidFragmentActivity mContext;
	
	private boolean intro_sound_played=false;
	
	public final static int SOUND_START=1;
	public final static int SOUND_END=2;
	public final static int SOUND_PLACE1=3;
	public final static int SOUND_PLACE2=4;
	public final static int SOUND_PICKUP1=5;
	public final static int SOUND_PICKUP2=6;
	
	public GoSoundManager (GobandroidFragmentActivity theContext) {
		Log.i("sound_man init");
	    mContext = theContext;
	    mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer, Integer> ();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	    
	    addSound(SOUND_START,R.raw.go_start);
	    addSound(SOUND_END,R.raw.go_clearboard);
	    addSound(SOUND_PLACE1,R.raw.go_place1);
	    addSound(SOUND_PLACE2,R.raw.go_place2);
	    addSound(SOUND_PICKUP1,R.raw.go_pickup1);
	    addSound(SOUND_PICKUP2,R.raw.go_pickup2);
	}
	
	public void addSound(int index, int SoundID) {
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	public void playSound(int index) {
	
		if ((!mContext.getSettings().isSoundEnabled())||(!intro_sound_played))
			return;

		Log.i("playing sound " + index);
		float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	    mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
	}
		
	public void playGameIntro() {
		if (!mContext.getSettings().isSoundEnabled())
			return;

		if (intro_sound_played)
			return;
		
		MediaPlayer mp = MediaPlayer.create(mContext, R.raw.go_start);
		
	    mp.setOnCompletionListener(new OnCompletionListener() {
	    	@Override
	        public void onCompletion(MediaPlayer mp) {
	    		mp.release();
	    		intro_sound_played=true;
	        }
	    });
	    
	    mp.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.i("sound_err");
				return false;
			}
	    	
	    });
    	mp.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
    		
    	});
    	
	    try {
			mp.prepareAsync();
		} catch (IllegalStateException e) {
			Log.i("sound_err" +e );
			intro_sound_played=true;
		}
	}
}

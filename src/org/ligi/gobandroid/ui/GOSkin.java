package org.ligi.gobandroid.ui;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class GOSkin {

	private static boolean do_board_skin=false;
	private static String board_skin_name="none";
	
	private static boolean do_stone_skin=false;
	private static String stone_skin_name="none";
	

	public final static String skin_base_path="/sdcard/gobandroid/skins/";
	
	/*
	public static String getSkinName() {
		return skin_name;
		
	}*/
	
	public static void setBoardSkin(String name) {
		if ((new File(skin_base_path+name).exists())) 
		{
			board_skin_name=name;
			do_board_skin=true;
		}
		else 
			do_board_skin=false;
	}


	public static void setStoneSkin(String name) {
		if ((new File(skin_base_path+name).exists())) 
		{
			stone_skin_name=name;
			do_stone_skin=true;
		}
		else 
			do_stone_skin=false;
	}

	
	public static String getBoardFname() {
		return skin_base_path+board_skin_name+"/board.jpg";
	}
	
	public static Bitmap getBoard(int width,int height) {
		if (do_board_skin)
			return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(getBoardFname()), width,height, true);
		else
			return null;
	}
	
	
	public static Bitmap getWhiteStone(float size) {
		return getStone("white",size);
	}
	
	public static Bitmap getBlackStone(float size) {
		return getStone("black",size);
	}
	
	public static Bitmap getStone(String name,float size) {
		if (do_stone_skin) {
				
		int size_append=17;
		if (size>23)
			size_append=32;
		if (size>50)
			size_append=64;

		Log.i("gobandroid", "scale to size" + size);	
		return Bitmap.createScaledBitmap(BitmapFactory.decodeFile(skin_base_path+stone_skin_name+"/"+name + size_append + ".png"
			), (int)size, (int)size, true);
		}
		else {
			Bitmap btm=Bitmap.createBitmap((int)size,(int)size,Bitmap.Config.ARGB_4444);
			
			Canvas c=new Canvas(btm);
			Paint mPaint=new Paint();
			mPaint.setAntiAlias(true);
			if (name.equals("white"))
				mPaint.setColor(Color.WHITE);
			else
				mPaint.setColor(Color.BLACK);
			
			c.drawCircle(c.getWidth()/2, c.getHeight()/2, size/2.0f, mPaint);
			return btm;
		}
	}
	
}

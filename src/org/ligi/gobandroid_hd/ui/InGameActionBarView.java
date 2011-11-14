package org.ligi.gobandroid_hd.ui;

import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.logic.GoGameProvider;
import org.ligi.tracedroid.logging.Log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class InGameActionBarView extends View implements GoGame.GoGameChangeListener {

    private Bitmap white_stone_bitmap=null;
    private Bitmap black_stone_bitmap=null;
    private Paint mPaint=new Paint();
    private Rect active_player_bg_rect=new Rect();
	private Paint myActiveBGPaint=new Paint();
	private FontMetrics fm;
	private float text_offset;
	
    private Bitmap getScaledRes(float size,int resID) {
    	Bitmap unscaled_bitmap=BitmapFactory.decodeResource(this.getResources(),resID);
    	return   Bitmap.createScaledBitmap(unscaled_bitmap, (int)size, (int)size, true);
    }
    
	public InGameActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InGameActionBarView(Context context) {
		super(context);
		init();
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPaint.setTextSize((int)(h/2.5));
		black_stone_bitmap=getScaledRes(h/2,R.drawable.stone_black);
		white_stone_bitmap=getScaledRes(h/2,R.drawable.stone_white);
		active_player_bg_rect=new Rect(0,0,black_stone_bitmap.getWidth()*3,black_stone_bitmap.getHeight());
		fm=mPaint.getFontMetrics();
		text_offset=(black_stone_bitmap.getHeight()-mPaint.getTextSize())/2-(fm.top+fm.bottom);
	}

	public void init() {
		mPaint.setColor(Color.WHITE);
		getGame().addGoGameChangeListener(this);
		myActiveBGPaint.setColor(Color.RED);
	}

	private GoGame getGame() {
		return GoGameProvider.getGame();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		active_player_bg_rect.offsetTo(0, getGame().isBlackToMove()?black_stone_bitmap.getHeight():0);
		
		canvas.drawRect(active_player_bg_rect, myActiveBGPaint);
		
    	canvas.drawBitmap(black_stone_bitmap, black_stone_bitmap.getWidth()/2, 0,null);
    	canvas.drawBitmap(white_stone_bitmap, black_stone_bitmap.getWidth()/2, this.getHeight()/2,null);
    	
    	canvas.drawText(" "+ GoGameProvider.getGame().getCapturesBlack(), (int)(black_stone_bitmap.getWidth()*1.5), text_offset, mPaint);
    	canvas.drawText(" "+ GoGameProvider.getGame().getCapturesWhite(), (int)(black_stone_bitmap.getWidth()*1.5), this.getHeight()/2 +text_offset, mPaint);
		super.onDraw(canvas);
	}

	@Override
	public void onGoGameChange() {
		Log.i("game changed");
		this.postInvalidate();
	}
}

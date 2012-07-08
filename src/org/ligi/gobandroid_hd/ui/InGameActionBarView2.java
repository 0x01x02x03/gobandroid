package org.ligi.gobandroid_hd.ui;

import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.gobandroid_hd.InteractionScope;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.ui.gnugo.GnuGoHelper;
import org.ligi.gobandroid_hd.ui.ingame_common.SwitchModeHelper;
import org.ligi.tracedroid.logging.Log;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class InGameActionBarView2 extends LinearLayout implements
		GoGame.GoGameChangeListener, DialogInterface {

	private TextView white_captures_tv;
	private TextView black_captures_tv;
	private TextView move_tv;
	private TextView mode_tv;
	private ViewGroup black_info_container;
	private ViewGroup white_info_container;
	private LayoutInflater inflater;
	private GobandroidApp app;
	private Activity activity;

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		app.getGame().addGoGameChangeListener(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		app.getGame().removeGoGameChangeListener(this);
	}

	public InGameActionBarView2(Activity _activity) {
		super(_activity);
		
		activity=_activity;
		app = (GobandroidApp) _activity.getApplicationContext();

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ViewGroup top_view = (ViewGroup) inflater.inflate(
				R.layout.top_nav_and_extras, null);

		white_captures_tv = (TextView) top_view
				.findViewById(R.id.white_captures_tv);
		black_captures_tv = (TextView) top_view
				.findViewById(R.id.black_captures_tv);
		move_tv = (TextView) top_view.findViewById(R.id.move_tv);
		mode_tv = (TextView) top_view.findViewById(R.id.mode_tv);
		black_info_container = (ViewGroup) top_view
				.findViewById(R.id.black_info_container);
		white_info_container = (ViewGroup) top_view
				.findViewById(R.id.white_info_container);
		// move_tv.setOnClickListener(new ToggleModePopup(context));

		top_view.findViewById(R.id.fake_spinner).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showModePopup(v.getContext());
					}
					
				});
		
		
		this.addView(top_view);
	
		refresh();

	}

	private void addItem(LinearLayout container, int image_resId,
			int str_resid, final DialogInterface.OnClickListener listener) {

		Log.i("", "container" + container + " inflater" + inflater);
		View v = inflater.inflate(R.layout.dropdown_item, null);
		((TextView) v.findViewById(R.id.text)).setText(str_resid);
		((ImageView) v.findViewById(R.id.image)).setImageResource(image_resId);
		
		v.findViewById(R.id.click_container).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(InGameActionBarView2.this, 0);
				
			}
		});
		container.addView(v);
	}

	private PopupWindow popup;

	private void addModeItem(LinearLayout container, final byte mode,
			int string_res, int icon_res) {
		if (mode == app.getInteractionScope().getMode())
			return; // already here

		addItem(container, icon_res, string_res,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
						Log.i("settingmode"+mode);
						app.getInteractionScope().setMode(mode);
						Intent i = SwitchModeHelper.getIntentByMode(app, mode);
						activity.startActivity(i);
					}

				});
	}

	private void showModePopup(Context ctx) {


	
		LinearLayout content_view = new LinearLayout(ctx);
		content_view.setOrientation(LinearLayout.VERTICAL);
		content_view.setBackgroundDrawable(new BitmapDrawableNoMinimumSize(
				ctx.getResources(), R.drawable.wood_bg));

		addModeItem(content_view, InteractionScope.MODE_RECORD,
				R.string.record, R.drawable.dashboard_record);
		addModeItem(content_view, InteractionScope.MODE_REVIEW,
				R.string.review, R.drawable.dashboard_review);
		addModeItem(content_view, InteractionScope.MODE_TELEVIZE,
				R.string.televize, R.drawable.gobandroid_tv);
		addModeItem(content_view, InteractionScope.MODE_TSUMEGO,
				R.string.tsumego, R.drawable.dashboard_tsumego);
		if (GnuGoHelper.isGnuGoAvail(ctx))
			addModeItem(content_view, InteractionScope.MODE_GNUGO,
				R.string.gnugo, R.drawable.dashboard_gnugo_item);

		BetterPopupWindow pop = new BetterPopupWindow(mode_tv);
		pop.setContentView(content_view);
		
		pop.showLikePopDownMenu();
	}
	
	class IconTextAndMode {
		public IconTextAndMode(int mode, int text_res, int icon_res) {
			this.icon_res = icon_res;
			this.text_res = text_res;
			this.mode = mode;
		}

		public int icon_res;
		public int text_res;
		public int mode;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popup != null)
				popup.dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onGoGameChange() {
		Log.i("game changed");
		refresh();
	}

	private void refresh() {
		this.post(new Runnable() {

			@Override
			public void run() {
				mode_tv.setText(InteractionScope.getModeStringRes(app
						.getInteractionScope().getMode()));

				white_captures_tv
						.setText("" + app.getGame().getCapturesWhite());
				black_captures_tv
						.setText("" + app.getGame().getCapturesBlack());
				int highlight_color = app.getResources().getColor(
						R.color.dividing_color);
				white_info_container.setBackgroundColor(app.getGame()
						.isBlackToMove() ? android.R.color.transparent
						: highlight_color);
				black_info_container.setBackgroundColor(app.getGame()
						.isBlackToMove() ? highlight_color
						: android.R.color.transparent);
				move_tv.setText(app.getResources().getString(R.string.move) + " "
						+ app.getGame().getActMove().getMovePos());

			}
		});
	}

	@Override
	public void cancel() {
	}

	@Override
	public void dismiss() {
	}

}

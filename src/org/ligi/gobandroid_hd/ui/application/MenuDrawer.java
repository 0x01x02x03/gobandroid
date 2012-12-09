package org.ligi.gobandroid_hd.ui.application;

import java.util.ArrayList;
import java.util.List;

import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.gobandroid_beta.R;
import org.ligi.gobandroid_hd.logic.GoGame;
import org.ligi.gobandroid_hd.ui.GoPrefsActivity;
import org.ligi.gobandroid_hd.ui.HelpDialog;
import org.ligi.gobandroid_hd.ui.links.LinksActivity;
import org.ligi.gobandroid_hd.ui.recording.GameRecordActivity;
import org.ligi.gobandroid_hd.ui.sgf_listing.SGFSDCardListActivity;

import com.google.analytics.tracking.android.EasyTracker;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivityBase;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuDrawer implements OnItemClickListener {

	private Activity ctx;
	private SlidingActivityBase sliding_base;

	public MenuDrawer(Activity ctx) {
		this.ctx = ctx;

		try {
			this.sliding_base = (SlidingActivityBase) ctx;
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"context must implement SlidingActivityBase");
		}

		ListView lv = new ListView(ctx);
		sliding_base.setBehindContentView(lv);
		// setSlidingActionBarEnabled(false);

		SlidingMenu sm = sliding_base.getSlidingMenu();
		sm.setBehindWidthRes(R.dimen.menu_drawer_width);

		sm.setShadowDrawable(R.drawable.divider_v);

		// sm.setBehindOffset(10);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setShadowWidth(5);

		lv.setAdapter(getAdapter());
		lv.setOnItemClickListener(this);

	}

	public ListAdapter getAdapter() {

		List<Object> items = new ArrayList<Object>();
		items.add(new Category("LOAD"));
		items.add(new Item(R.id.empty, "Empty Board", R.drawable.play));
		items.add(new Item(R.id.tsumego, "Tsumego",
				R.drawable.dashboard_tsumego));
		items.add(new Item(R.id.review, "Review", R.drawable.dashboard_review));
		items.add(new Item(R.id.bookmark, "Bookmark", R.drawable.bookmark));
		items.add(new Category("MORE"));
		items.add(new Item(R.id.links, "Links", R.drawable.dashboard_links));
		items.add(new Item(R.id.preferences, "Preferences",
				R.drawable.preferences));
		items.add(new Item(R.id.help, "Help",R.drawable.help));

		return new MenuAdapter(items);
	}

	private void handleId(int id) {
		switch (id) {
		case R.id.help:
	
			new HelpDialog(ctx).show();
			EasyTracker.getTracker().trackEvent("ui_action", "dashboard",
					"help", null);

			
			break;
		case R.id.empty:
			GoGame act_game=getApp().getInteractionScope().getGame();
			
			getApp().getInteractionScope().setGame(new GoGame((byte)act_game.getSize(),(byte)act_game.getHandicap()));
			getApp().getInteractionScope().getGame().notifyGameChange();
			
			ctx.startActivity(new Intent(ctx, GameRecordActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
			break;

		case R.id.links:
			ctx.startActivity(new Intent(ctx, LinksActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
			break;
		case R.id.preferences:

			ctx.startActivity(new Intent(ctx, GoPrefsActivity.class)
					.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
			break;
		case R.id.tsumego:
			startSGFListForPath(getApp().getSettings().getTsumegoPath());
			break;

		case R.id.review:
			startSGFListForPath(getApp().getSettings().getReviewPath());
			break;

		case R.id.bookmark:
			startSGFListForPath(getApp().getSettings().getBookmarkPath());
			break;

		}
	}

	private void startSGFListForPath(String path) {
		Intent i = new Intent(ctx, SGFSDCardListActivity.class);
		i.setData(Uri.parse("file://" + path));
		ctx.startActivity(i);
		ctx.finish();
	}

	private GobandroidApp getApp() {
		return (GobandroidApp) ctx.getApplicationContext();
	}

	private static class Item {

		String mTitle;
		int mIconRes;
		int id;

		Item(int id, String title, int iconRes) {
			this.id = id;
			mTitle = title;
			mIconRes = iconRes;
		}
	}

	private static class Category {

		String mTitle;

		Category(String title) {
			mTitle = title;
		}
	}

	private class MenuAdapter extends BaseAdapter {

		private List<Object> mItems;

		MenuAdapter(List<Object> items) {
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position) instanceof Item ? 0 : 1;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isEnabled(int position) {
			return getItem(position) instanceof Item;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			Object item = getItem(position);

			if (item instanceof Category) {
				if (v == null) {
					v = ctx.getLayoutInflater().inflate(
							R.layout.menu_row_category, parent, false);
				}

				((TextView) v).setText(((Category) item).mTitle);

			} else {
				if (v == null) {
					v = ctx.getLayoutInflater().inflate(R.layout.menu_row_item,
							parent, false);
				}

				v.setTag(((Item) item).id);

				TextView tv = (TextView) v;
				tv.setText(((Item) item).mTitle);
				// BitmapDrawable bmp=BitmapDrawable.c;
				Resources res = tv.getContext().getResources();
				int icon_size = res
						.getDimensionPixelSize(R.dimen.actionbar_height);
				Bitmap bmp = (Bitmap.createScaledBitmap(BitmapFactory
						.decodeResource(res, ((Item) item).mIconRes, null),
						icon_size, icon_size, false));
				Drawable bmp_d = new BitmapDrawable(ctx.getResources(), bmp);
				tv.setCompoundDrawablesWithIntrinsicBounds(bmp_d, null, null,
						null);
			}

			/*
			 * v.setTag(R.id.mdActiveViewPosition, position);
			 * 
			 * if (position == mActivePosition) { mMenuDrawer.setActiveView(v,
			 * position); }
			 */

			return v;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		handleId((Integer) arg1.getTag());
	}
}

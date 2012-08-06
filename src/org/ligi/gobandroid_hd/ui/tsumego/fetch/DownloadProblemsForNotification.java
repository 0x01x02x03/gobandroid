package org.ligi.gobandroid_hd.ui.tsumego.fetch;

import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.gobandroid_hd.ui.GobandroidNotifications;

import com.google.analytics.tracking.android.EasyTracker;

import android.content.Context;

public class DownloadProblemsForNotification {

	public static void show(Context ctx) {

		GobandroidApp app = (GobandroidApp) ctx.getApplicationContext();

		EasyTracker.getTracker().trackEvent("ui_action", "tsumego",
				"refresh_notification", null);

		int res = TsumegoDownloadHelper.doDownloadDefault(app);
		if (res > 0)
			GobandroidNotifications.addNewTsumegosNotification(ctx, res);
	}
}

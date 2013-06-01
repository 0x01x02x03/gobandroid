package org.ligi.gobandroid_hd.ui.tsumego.fetch;

import android.content.Context;
import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.gobandroid_hd.ui.GobandroidNotifications;

public class DownloadProblemsForNotification {

    public static void show(Context ctx) {

        GobandroidApp app = (GobandroidApp) ctx.getApplicationContext();

        GobandroidApp.getTracker().trackEvent("ui_action", "tsumego", "refresh_notification", null);

        int res = TsumegoDownloadHelper.doDownloadDefault(app);
        if (res > 0)
            new GobandroidNotifications(ctx).addNewTsumegosNotification(res);
    }
}

package org.ligi.gobandroidhd.uitest;

import android.test.suitebuilder.annotation.MediumTest;

import com.squareup.spoon.Spoon;

import org.ligi.gobandroid_hd.App;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.ui.tsumego.TsumegoActivity;
import org.ligi.gobandroidhd.base.BaseIntegration;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.ligi.gobandroidhd.base.GoViewActions.placeStone;

public class TheTsumegoActivity extends BaseIntegration<TsumegoActivity> {

    public TheTsumegoActivity() {
        super(TsumegoActivity.class);
    }

    @MediumTest
    public void testThatNoTsumegoWarningComes() {
        App.setGame(readGame("default_marker"));
        final TsumegoActivity activity = getActivity();

        onView(withText(R.string.tsumego_sgf_no_solution)).check(matches(isDisplayed()));
        Spoon.screenshot(activity, "tsumego_fail");
    }

    @MediumTest
    public void testThatOffPathMessageIsNotThereInBeginning() {
        App.setGame(readGame("tsumego"));
        final TsumegoActivity activity = getActivity();

        onView(withId(R.id.tsumego_off_path_view)).check(matches(not(isDisplayed())));
        Spoon.screenshot(activity, "tsumego_on_path");
    }

    @MediumTest
    public void testThatOffPathMessageComes() {
        App.setGame(readGame("tsumego"));
        final TsumegoActivity activity = getActivity();

        onView(withId(R.id.go_board)).perform(placeStone(1, 1));

        onView(withId(R.id.tsumego_off_path_view)).check(matches(isDisplayed()));
        Spoon.screenshot(activity, "tsumego");
    }


}

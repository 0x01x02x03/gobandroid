package org.ligi.gobandroidhd.base;

import android.view.View;

import com.google.android.apps.common.testing.ui.espresso.ViewAction;
import com.google.android.apps.common.testing.ui.espresso.action.CoordinatesProvider;
import com.google.android.apps.common.testing.ui.espresso.action.GeneralClickAction;
import com.google.android.apps.common.testing.ui.espresso.action.Press;
import com.google.android.apps.common.testing.ui.espresso.action.Tap;

import org.ligi.gobandroid_hd.App;

public class GoViewActions {

    public static ViewAction placeStone(final int x, final int y) {
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);
                        final int gameSize = App.getGame().getSize();

                        final float screenX = screenPos[0] + x * (view.getWidth() / gameSize);
                        final float screenY = screenPos[1] + y * (view.getHeight() / gameSize);
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }
}

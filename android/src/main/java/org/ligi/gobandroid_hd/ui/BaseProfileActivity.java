package org.ligi.gobandroid_hd.ui;

import android.os.Bundle;
import android.widget.EditText;

import butterknife.Bind;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity;

import butterknife.ButterKnife;

public class BaseProfileActivity extends GobandroidFragmentActivity {

    @Bind(R.id.username_edit)
    EditText username_et;

    @Bind(R.id.rank_edit)
    EditText rank_et;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        setTitle(R.string.profile);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        ButterKnife.bind(this);

        rank_et.setText(settings.getRank());
        username_et.setText(settings.getUsername());
    }


    @Override
    protected void onPause() {
        settings.setRank(rank_et.getText().toString());
        settings.setUsername(username_et.getText().toString());
        super.onPause();
    }

}

package org.ligi.gobandroid_hd.ui.application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.google.analytics.tracking.android.EasyTracker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.GooglePlusUtil;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;

import org.ligi.gobandroid_hd.GobandroidApp;
import org.ligi.gobandroid_hd.R;
import org.ligi.gobandroid_hd.logic.GoGame;

import java.lang.reflect.Field;

public class GobandroidFragmentActivity extends SherlockFragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    protected static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    protected ProgressDialog mConnectionProgressDialog;
    protected PlusClient mPlusClient;
    protected ConnectionResult mConnectionResult;
    private NavigationDrawer mMenuDrawer;
    private AQuery mAQ;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout drawerLayout;

    public void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(R.layout.navigation_drawer_container);
        View v = getLayoutInflater().inflate(layoutResId, null);
        ViewGroup vg = (ViewGroup) findViewById(R.id.content_frame);
        vg.addView(v);
        mMenuDrawer = new NavigationDrawer(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_launcher,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        );

        // Set the drawer toggle as the DrawerListener

        drawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getSupportActionBar() != null) // yes this happens - e.g.
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // a little hack because I strongly disagree with the style guide here
        // ;-)
        // not having the Actionbar overfow menu also with devices with hardware
        // key really helps discoverability
        // http://stackoverflow.com/questions/9286822/how-to-force-use-of-overflow-menu-on-devices-with-menu-button
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore - but at least we tried ;-)
        }

        // we do not want focus on custom views ( mainly for GTV )
        /*
         * wd if ((this.getSupportActionBar()!=null) &&
		 * (this.getSupportActionBar().getCustomView()!=null))
		 * this.getSupportActionBar().getCustomView().setFocusable(false);
		 *
		 */

        mPlusClient = new PlusClient.Builder(getApplicationContext(), this, this)

                .setVisibleActivities("http://schemas.google.com/CreateActivity",
                        "http://schemas.google.com/ReviewActivity",
                        "http://schemas.google.com/CommentActivity",
                        "http://schemas.google.com/AddActivity")
                .setScopes(Scopes.PLUS_LOGIN)
                .build();

        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        mPlusClient.connect();
    }

    public boolean doFullScreen() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //NaDra mMenuDrawer.refresh();

        if (doFullScreen()) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public GobandroidApp getApp() {
        return (GobandroidApp) getApplicationContext();
    }

    public GoGame getGame() {
        return getApp().getGame();
    }

    public GobandroidSettings getSettings() {
        return getApp().getSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        android.view.MenuItem foo = new android.view.MenuItem() {

            @Override
            public int getItemId() {
                return android.R.id.home;
            }

            @Override
            public int getGroupId() {
                return 0;
            }

            @Override
            public int getOrder() {
                return 0;
            }

            @Override
            public android.view.MenuItem setTitle(CharSequence charSequence) {
                return null;
            }

            @Override
            public android.view.MenuItem setTitle(int i) {
                return null;
            }

            @Override
            public CharSequence getTitle() {
                return null;
            }

            @Override
            public android.view.MenuItem setTitleCondensed(CharSequence charSequence) {
                return null;
            }

            @Override
            public CharSequence getTitleCondensed() {
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(Drawable drawable) {
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(int i) {
                return null;
            }

            @Override
            public Drawable getIcon() {
                return null;
            }

            @Override
            public android.view.MenuItem setIntent(Intent intent) {
                return null;
            }

            @Override
            public Intent getIntent() {
                return null;
            }

            @Override
            public android.view.MenuItem setShortcut(char c, char c2) {
                return null;
            }

            @Override
            public android.view.MenuItem setNumericShortcut(char c) {
                return null;
            }

            @Override
            public char getNumericShortcut() {
                return 0;
            }

            @Override
            public android.view.MenuItem setAlphabeticShortcut(char c) {
                return null;
            }

            @Override
            public char getAlphabeticShortcut() {
                return 0;
            }

            @Override
            public android.view.MenuItem setCheckable(boolean b) {
                return null;
            }

            @Override
            public boolean isCheckable() {
                return false;
            }

            @Override
            public android.view.MenuItem setChecked(boolean b) {
                return null;
            }

            @Override
            public boolean isChecked() {
                return false;
            }

            @Override
            public android.view.MenuItem setVisible(boolean b) {
                return null;
            }

            @Override
            public boolean isVisible() {
                return false;
            }

            @Override
            public android.view.MenuItem setEnabled(boolean b) {
                return null;
            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public boolean hasSubMenu() {
                return false;
            }

            @Override
            public SubMenu getSubMenu() {
                return null;
            }

            @Override
            public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
                return null;
            }

            @Override
            public ContextMenu.ContextMenuInfo getMenuInfo() {
                return null;
            }

            @Override
            public void setShowAsAction(int i) {

            }

            @Override
            public android.view.MenuItem setShowAsActionFlags(int i) {
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(View view) {
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(int i) {
                return null;
            }

            @Override
            public View getActionView() {
                return null;
            }

            @Override
            public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
                return null;
            }

            @Override
            public ActionProvider getActionProvider() {
                return null;
            }

            @Override
            public boolean expandActionView() {
                return false;
            }

            @Override
            public boolean collapseActionView() {
                return false;
            }

            @Override
            public boolean isActionViewExpanded() {
                return false;
            }

            @Override
            public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener onActionExpandListener) {
                return null;
            }
        };
        mDrawerToggle.onOptionsItemSelected(foo);
        return true;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_WINDOW) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // very nice hint by Jake Wharton via twitter
    @SuppressWarnings("unchecked")
    public <T> T findById(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this); // Add this method.
        mPlusClient.disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this); // Add this method
        mPlusClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }

        // Save the intent so that we can start an activity when the user clicks
        // the sign-in button.
        mConnectionResult = result;
    }

    private void workingPostToGPlus() {
        // Create an interactive post with the "VIEW_ITEM" label. This will
        // create an enhanced share dialog when the post is shared on Google+.
        // When the user clicks on the deep link, ParseDeepLinkActivity will
        // immediately parse the deep link, and route to the appropriate resource.
        Uri callToActionUrl = Uri.parse("https://cloud-goban.appspot.com/game/ag1zfmNsb3VkLWdvYmFucgwLEgRHYW1lGPK_JAw");
        String callToActionDeepLinkId = "/foo/bar";


        // Create an interactive post builder.
        PlusShare.Builder builder = new PlusShare.Builder(this, mPlusClient);

        // Set call-to-action metadata.
        builder.addCallToAction("CREATE_ITEM", callToActionUrl, callToActionDeepLinkId);

        // Set the target url (for desktop use).
        builder.setContentUrl(Uri.parse("https://cloud-goban.appspot.com/game/ag1zfmNsb3VkLWdvYmFucgwLEgRHYW1lGPK_JAw"));

        // Set the target deep-link ID (for mobile use).
        builder.setContentDeepLinkId("/pages/",
                null, null, null);

        // Set the pre-filled message.
        builder.setText("foo bar");

        startActivityForResult(builder.getIntent(), 0);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // We've resolved any connection errors.
        mConnectionProgressDialog.dismiss();


        /*
        ItemScope target = new ItemScope.Builder()
                //.setUrl("https://cloud-goban.appspot.com/game/"+key)
                .setUrl("https://developers.google.com/+/web/snippet/examples/thing")
                //.setUrl("http://cloud-goban.appspot.com/game/ag1zfmNsb3VkLWdvYmFucgwLEgRHYW1lGKGnJww")
                //.setType("http://schema.org/CreativeWork")
                .build();

        Moment moment = new Moment.Builder()
                .setType("http://schemas.google.com/AddActivity")
                .setTarget(target)
                .build();

        getPlusClient().writeMoment(moment);
         */

        final int errorCode = GooglePlusUtil.checkGooglePlusApp(this);
        if (errorCode == GooglePlusUtil.SUCCESS) {
            /*PlusShare.Builder builder = new PlusShare.Builder(this, mPlusClient);

            // Set call-to-action metadata.
            builder.addCallToAction(
                    "CREATE_ITEM",
                    Uri.parse("http://plus.google.com/pages/create"),
                    "/pages/create");
            // Set the content url (for desktop use).
            builder.setContentUrl(Uri.parse("https://plus.google.com/pages/"));

            // Set the target deep-link ID (for mobile use).
            builder.setContentDeepLinkId("/pages/", null, null, null);

            // Set the share text.
            builder.setText("Create your Google+ Page too!");
            startActivityForResult(builder.getIntent(), 0);
            */

            /*
               Intent shareIntent = new PlusShare.Builder(this)
          .setType("text/plain")
          .setText("Welcome to the Google+ platform.")
          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
          .getIntent();

             startActivityForResult(shareIntent, 0);
               */

            /*
            Intent shareIntent = new PlusShare.Builder(this)
                    .setText("Lemon Cheesecake recipe")
                    .setType("text/plain")
                    .setContentDeepLinkId("/cheesecake/lemon",
                            "Lemon Cheesecake recipe",
                            "A tasty recipe for making lemon cheesecake.",
                            Uri.parse("http://example.com/static/lemon_cheesecake.png"))
                    .getIntent();

            startActivityForResult(shareIntent, 0);
              */
            //workingPostToGPlus();


        } else {
            // Prompt the user to install the Google+ app.
            GooglePlusUtil.getErrorDialog(errorCode, this, 0).show();
        }


    }

    @Override
    public void onDisconnected() {

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    protected AQuery getAQ() {
        if (mAQ == null) {
            mAQ = new AQuery(this);
        }
        return mAQ;
    }

    public PlusClient getPlusClient() {
        return mPlusClient;
    }

}

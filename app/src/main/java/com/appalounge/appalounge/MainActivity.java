package com.appalounge.appalounge;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import appalounge.APPAAnnounce;
import appalounge.APPALogin;
import appalounge.AppaLounge;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Main views
    private LinearLayout announcementLinearView;

    private final Context context = this;
    private String userKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(R.drawable.ic_action_refresh));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAnnouncements();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        //Load each view
        announcementLinearView = (LinearLayout) findViewById(R.id.announcement_list);

        // Load the announcements
        loadAnnouncements();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Load announcements and populate the announcement list
     */
    public void loadAnnouncements(){

        // Download the announcements using EasyAPI
        class AnnouncementTask extends AsyncTask<Void, Void, Void> {

            final AppaLounge appaObject = new AppaLounge();
            APPAAnnounce announcer;

            @Override
            protected Void doInBackground(Void... params) {

                announcer = appaObject.createAnnouncementObject();
                try {
                    announcer.downloadData();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void result) {

                announcementLinearView.removeAllViews();

                for(int i = 0; i < announcer.getNumAnnouncements(); i++){
                    LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View content = vi.inflate(R.layout.announcement_text, null);

                    TextView message = (TextView) content.findViewById(R.id.announce_content);
                    TextView byline = (TextView) content.findViewById(R.id.announce_byline);

                    message.setText(announcer.getMessageAt(i));
                    byline.setText("- " + announcer.getCreatorAt(i) + ", " + announcer.getCreatedAt(i));

                    // Add a margin
                    Resources r = context.getResources();
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            8,
                            r.getDisplayMetrics()
                    );

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins(0, px, 0, px);
                    content.setLayoutParams(params);

                    announcementLinearView.addView(content);

                }

            }

            @Override
            protected void onPreExecute() {

                Snackbar.make(announcementLinearView, "Refreshing...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }

        }

        new AnnouncementTask().execute();

    }

    public void showLogin(){

        // custom view dialog

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.login_title)
                .customView(R.layout.login_dialog_layout, true)
                .positiveText(R.string.login_submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        final EditText usernameInput = (EditText) dialog.getCustomView().findViewById(R.id.username);
                        final EditText passwordInput = (EditText) dialog.getCustomView().findViewById(R.id.password);
                        final CheckBox checkbox = (CheckBox) dialog.getCustomView().findViewById(R.id.remember_checkbox);
                        startLogin(usernameInput.getEditableText().toString(),
                                passwordInput.getEditableText().toString(),
                                checkbox.isChecked());

                    }
                }).build();

        dialog.show();



    }

    public void startLogin(final String username, final String password, final boolean remembered){

        // Download the announcements using EasyAPI
        class LoginTask extends AsyncTask<Void, Void, Void> {

            final AppaLounge appaObject = new AppaLounge();
            MaterialDialog loggingIn = null;
            String key = null;
            APPALogin login;

            @Override
            protected Void doInBackground(Void... params) {

                login = appaObject.createLoginObject();
                try {
                    login.setParameters(username, password);
                    login.downloadData();
                    key = login.getKey();
                } catch (Exception e) {
                    Log.e("Login Error", e.getMessage());
                }

                return null;

            }

            @Override
            protected void onPostExecute(Void result) {

                loggingIn.dismiss();

                if(key != null){
                    new MaterialDialog.Builder(context)
                            .title(R.string.logged_in_title)
                            .positiveText("OK")
                            .show();
                    userKey = key;
                    loadProfileInformation();
                }
                else {
                    new MaterialDialog.Builder(context)
                            .title(R.string.logged_in_title_bad)
                            .positiveText("OK")
                            .show();
                }

            }

            @Override
            protected void onPreExecute() {

                loggingIn = new MaterialDialog.Builder(context)
                        .title(R.string.logging_in_title)
                        .show();

            }

        }

        new LoginTask().execute();

    }

    public void loadProfileInformation(){



    }

}

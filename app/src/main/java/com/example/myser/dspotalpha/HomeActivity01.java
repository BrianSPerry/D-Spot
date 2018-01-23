package com.example.myser.dspotalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;;

public class HomeActivity01 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private HomeFragment homeFragment = new HomeFragment();
    private AccountFragment profileFragment = new AccountFragment();
    private FeedsFragment feedsFragmentFragment = new FeedsFragment();
    private PreferenceFragment preferenceFragment = new PreferenceFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public static FirebaseAuth firebaseAuthentication;

    private Intent thisIntent;
    private String fragmentToLoad;
    private int numberOfPreferences = 0;
    private int numberOfSelectedPrefs = 0;
    private PreferencesArrayData preferencesArrayData;
    private LinearLayout[] preferenceImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home01);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //ActionBar actionBar = getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        thisIntent = getIntent();
        fragmentToLoad = String.valueOf(thisIntent.getStringExtra(LoginActivity.FRAGMENT_TO_LOAD));
        firebaseAuthentication = FirebaseAuth.getInstance();

        if (fragmentToLoad.equals("HOME")) {
            fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).addToBackStack(null).commit();
        }

        //preferencesArrayData = new PreferencesArrayData(this);
        //gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        //SharedPreferences sharedPreferences = getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        //numberOfPreferences = sharedPreferences.getInt(getResources().getString(R.string.number_of_preferences), 0);

        //initializePrefsImageViewArray();
        //displaySelectedPreferences();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                //return;
                //finish();
                //System.exit(0);
                //android.os.Process.killProcess(android.os.Process.myPid());
            }
            else {
                super.onBackPressed();
            }
            //super.onBackPressed();
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_activity01, menu);
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
            startActivity(new Intent(HomeActivity01.this, SettingsActivity.class));
        }
        if (id == R.id.account) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_profile) {
            //fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, profileFragment).addToBackStack(null).commit();
            startActivity(new Intent(this, ProfileActivity.class));
        }  else if (id == R.id.nav_map) {
            startActivity(new Intent(HomeActivity01.this, MapsActivity.class));
        }  else if (id == R.id.nav_settings) {
            startActivity(new Intent(HomeActivity01.this, SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void displaySelectedPreferences () {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        numberOfSelectedPrefs = sharedPreferences.getInt(getResources().getString(R.string.selected_prefs_amt), 0);

        for (int i = 0; i < numberOfPreferences; i++) {
            if (sharedPreferences.getString(preferencesArrayData.preferenceKeys[i], "default value").equals(preferencesArrayData.preferenceValues[i])) {
                preferenceImages[i].setVisibility(View.VISIBLE);
            }
            else {
                preferenceImages[i].setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        //GridLayout layout = (GridLayout)findViewById(R.id.homeFragmentGridLayout);
        /*GridLayout layout = homeFragment.getGridLayout();

        if (layout != null) {
            Toast.makeText(this, "layout not null! :-)", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "layout is null!!", Toast.LENGTH_SHORT).show();
        }*/
        //getChildren(layout);
    }

    public void loadPreference01(View view) {
        Toast.makeText(this, "Mega Penis!", Toast.LENGTH_SHORT).show();
    }

    public void getChildren(GridLayout layout) {
        ViewGroup.LayoutParams params;
        preferenceImages = new LinearLayout[layout.getChildCount()];
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels / 3;

        for (int i = 0; i < layout.getChildCount(); i++) {
            preferenceImages[i] = (LinearLayout) layout.getChildAt(i);
            params = preferenceImages[i].getLayoutParams();
            params.width = width;

            preferenceImages[i].setLayoutParams(params);
        }
    }

    public void getPhotoFile (View view) {
        profileFragment.getPhotoFile(view);
    }

    public void loadFeed(View view) {
        fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, feedsFragmentFragment).addToBackStack(null).commit();
    }

}

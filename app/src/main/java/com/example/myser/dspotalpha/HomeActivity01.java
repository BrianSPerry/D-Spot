package com.example.myser.dspotalpha;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.lang.ref.PhantomReference;

public class HomeActivity01 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnWindowSizeChanged {

    private HomeFragment homeFragment = new HomeFragment();
    private AccountFragment profileFragment = new AccountFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public static FirebaseAuth firebaseAuthentication;

    private Intent thisIntent;
    private String fragmentToLoad;
    private int numberOfPreferences = 0;
    private int numberOfSelectedPrefs = 0;
    private PreferencesArrayData preferencesArrayData;
    private LinearLayout[] preferenceImages;
    //private GridLayout gridLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, profileFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    //Toast.makeText(HomeActivity01.this, "Double clit!", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(HomeActivity01.this, LoginActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home01);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        thisIntent = getIntent();
        fragmentToLoad = String.valueOf(thisIntent.getStringExtra(LoginActivity.FRAGMENT_TO_LOAD));
        firebaseAuthentication = FirebaseAuth.getInstance();

        if (fragmentToLoad.equals("HOME")) {
            fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).commit();
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
            super.onBackPressed();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_profile) {
            fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, profileFragment).commit();
        }  else if (id == R.id.nav_map) {
            startActivity(new Intent(HomeActivity01.this, MapsActivity.class));
        } else if (id == R.id.nav_prefs) {
            startActivity(new Intent(HomeActivity01.this, PreferencesActivity.class));
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

    private void initializePrefsImageViewArray () {
        preferenceImages = new LinearLayout[numberOfPreferences];

        preferenceImages[0] = (LinearLayout) findViewById(R.id.prefs1);
        preferenceImages[1] = (LinearLayout) findViewById(R.id.prefs2);
        preferenceImages[2] = (LinearLayout) findViewById(R.id.prefs3);
        preferenceImages[3] = (LinearLayout) findViewById(R.id.prefs4);
        preferenceImages[4] = (LinearLayout) findViewById(R.id.prefs5);
        preferenceImages[5] = (LinearLayout) findViewById(R.id.prefs6);

        for (int i = 0; i < preferenceImages.length; i++) {
            preferenceImages[i].setVisibility(View.GONE);
        }
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

    public void logOut (View view) {
        firebaseAuthentication.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void saveInformation (View view) {
        profileFragment.saveInformation(view);
    }

}

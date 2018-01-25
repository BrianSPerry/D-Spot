package com.example.myser.dspotalpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;;import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity01 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private HomeFragment homeFragment = new HomeFragment();
    private AccountFragment profileFragment = new AccountFragment();
    private FeedsFragment feedsFragmentFragment = new FeedsFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public static FirebaseAuth firebaseAuthentication;

    private Intent thisIntent;
    private String fragmentToLoad;

    private GridView gridView;
    private ConstraintLayout constraintLayoutProgressBar;
    private PreferenceFragment prefsFragmentFragment = new PreferenceFragment();
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReferencePrefs;
    private StorageReference storageReference;
    private GridAdapter gridAdapter;
    private int counter = 0;
    private Bundle bundle;
    public static final String SELECTED_CATEGORY_STRING = "SELECTED_CATEGORY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home01);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        thisIntent = getIntent();
        fragmentToLoad = String.valueOf(thisIntent.getStringExtra(LoginActivity.FRAGMENT_TO_LOAD));
        firebaseAuthentication = FirebaseAuth.getInstance();

        initializeHome(savedInstanceState);

        if (fragmentToLoad.equals("HOME")) {
            //fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, homeFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.finish();
            System.exit(0);
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
            Toast.makeText(this, "Nothing to show just yet...", Toast.LENGTH_SHORT).show();
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

    public void getPhotoFile (View view) {
        profileFragment.getPhotoFile(view);
    }

    public void loadFeed(View view) {
        fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, feedsFragmentFragment).addToBackStack(null).commit();
    }

    private void initializeHome (Bundle savedInstanceState) {
        bundle = savedInstanceState;
        if (bundle == null) {
            bundle = new Bundle();
        }
        gridView = (GridView) findViewById(R.id.gridView);
        constraintLayoutProgressBar = (ConstraintLayout) findViewById(R.id.constraintLayoutProgressBar);

        FloatingActionButton fab = (FloatingActionButton) (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Allow the ability to add preferences here...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, prefsFragmentFragment).addToBackStack(null).commit();
                startActivity(new Intent(getApplicationContext(), PreferencesActivity.class));
            }
        });

        firebaseUser = firebaseAuthentication.getCurrentUser();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        getSelectedPreferences();
    }

    private void getSelectedPreferences () {
        gridAdapter = new GridAdapter();
        counter = 0;
        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    gridAdapter.keys.add(childSnapshot.getKey());
                    gridAdapter.values.add(value);
                    gridAdapter.strings.add(value);
                    loadThumbnails(counter);
                    counter++;

                    if (gridAdapter.strings.size() > 0) {
                        constraintLayoutProgressBar.setVisibility(View.GONE);
                    }

                    gridAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadThumbnails (final int index) {
        //even if a preference is not selected,
        //the thumbnail attributed to it
        //gets set to the next selected preference.
        //So if i selected prefs 1, prefs 1 thumbnail is set to it
        //If I don't select prefs 2 but select prefs 3, prefs 2's thumbnail gets set to prefs 3.
        databaseReferencePrefs.child("Preference URL Thumbnails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    if (gridAdapter.keys.size() > 0) {
                        if (childSnapshot.getKey().equals(String.valueOf(gridAdapter.keys.get(index)))) {
                            String value = childSnapshot.getValue().toString();
                            gridAdapter.thumbnailURLs.add(value);
                        }
                    }
                    gridAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class GridAdapter extends BaseAdapter {

        public ArrayList<String> strings = new ArrayList<>();
        public ArrayList<String> thumbnailURLs = new ArrayList<>();
        public HashMap<String, String> preferences = new HashMap<>();

        public ArrayList<String> keys = new ArrayList<>();
        public ArrayList<String> values = new ArrayList<>();

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int i) {
            return strings.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final TextView textView;
            ImageView imageView;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.preference_home_item, viewGroup, false);
            }

            textView = (TextView) view.findViewById(R.id.textView15);
            imageView = (ImageView)view.findViewById(R.id.imageView10);

            textView.setText(strings.get(i));
            if (thumbnailURLs.size() > 0) {
                Picasso.with(getApplicationContext()).load(thumbnailURLs.get(i)).into(imageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //bundle.putString(SELECTED_CATEGORY_STRING, textView.getText().toString());
                    //feedsFragmentFragment.setArguments(bundle);
                    //fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, feedsFragmentFragment).addToBackStack(null).commit();
                    Intent intent = new Intent(getApplicationContext(), FeedsActivity.class);

                    intent.putExtra(SELECTED_CATEGORY_STRING, textView.getText().toString());
                    startActivity(intent);

                    /*if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(textView.getText().toString());
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Null Action Bar", Toast.LENGTH_SHORT).show();
                    }*/
                }
            });

            return view;
        }
    }

}

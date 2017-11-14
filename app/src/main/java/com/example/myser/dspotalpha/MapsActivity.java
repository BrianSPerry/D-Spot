package com.example.myser.dspotalpha;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static String SELECTED_MAP_TYPE = "SELECTED_MAP_TYPE";

    private SearchView searchView;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    private Location location;
    private LatLng userLocation;

    private SharedPreferences sharedPreferences;
    private LocationListener locationListener;
    private SearchView.OnQueryTextListener onQueryListener;

    private int savedMapType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = (SearchView) findViewById(R.id.searchView);

        /*Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPreferences = getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        savedMapType = sharedPreferences.getInt(SELECTED_MAP_TYPE, 0);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MapsActivity.this, "LOCATION CHANGED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Toast.makeText(MapsActivity.this, "STATUS CHANGED ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(MapsActivity.this, "PROVIDER ENABLED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MapsActivity.this, "PROVIDER DISABLED", Toast.LENGTH_SHORT).show();
            }
        };

        criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            location = locationManager.getLastKnownLocation(provider);
            locationManager.removeUpdates(locationListener);
        }

        onQueryListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        };

        searchView.setOnQueryTextListener(onQueryListener);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //criteria = new Criteria();
        //provider = locationManager.getBestProvider(criteria, false);
        //location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            userLocation = new LatLng(location.getLatitude(),location.getLongitude());

            mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            else {
                mMap.setMyLocationEnabled(true);
            }

            setMapType();
        }
    }

    public void search (/*View view*/) {
        String location = searchView.getQuery().toString();

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Enter something, penis!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToCurrentLocation (GoogleMap googleMap) {

    }

    public void changeMapType (View view) {
        savedMapType++;
        if (savedMapType >= 5) {
            savedMapType = 0;
        }

        setMapType();
        //Toast.makeText(this, String.valueOf(mMap.getMapType()), Toast.LENGTH_SHORT).show();
        saveSelectedMapType();
    }

    private void setMapType () {
        switch (savedMapType) {
            case 0:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case 2:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case 3:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case 4:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
        }
    }

    private void saveSelectedMapType () {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SELECTED_MAP_TYPE, savedMapType);
        editor.apply();
    }

}

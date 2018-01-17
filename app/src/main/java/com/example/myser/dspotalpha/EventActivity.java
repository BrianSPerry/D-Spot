package com.example.myser.dspotalpha;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventActivity extends AppCompatActivity {

    public static String LATITUDE = "LATITUDE";
    public static String LONGITUDE = "LONGITUDE";

    private double latitude;
    private double longitude;

    //region TabLayout
    /*private TabLayout tabLayout;
    private ViewPager viewPager;*/
    //endregion
    private TextView title, date, time, website, description, address, location;
    private ImageView imageView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TITLE) != null) {
                actionBar.setTitle(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TITLE));
            }
            else {
                actionBar.setTitle("Selected Destination");
            }
        }

        //region Tab Layout
        /*tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);*/
        //endregion
        imageView = (ImageView) findViewById(R.id.roundFrameImage);

        title = (TextView)findViewById(R.id.eventTitleTextView);
        date = (TextView)findViewById(R.id.dateTextView);
        //time = (TextView)findViewById(R.id.timeTextView);
        //website = (TextView)findViewById(R.id.websiteTextView);
        description = (TextView)findViewById(R.id.descriptionTextView);
        //address = (TextView)findViewById(R.id.addressTextView);
        location = (TextView)findViewById(R.id.locationTextView);

        intent = getIntent();

        title.setText(String.valueOf(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TITLE)));
        date.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DATE));
        //time.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TIME));
        //website.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_WEBSITE));
        description.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DESCRIPTION));
        //address.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DESCRIPTION));
        location.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_LOCATION));

        latitude = intent.getDoubleExtra(FeedsFragment.SELECTED_LATITUDE, 0);
        longitude = intent.getDoubleExtra(FeedsFragment.SELECTED_LONGITUDE, 0);

        //initializeTabLayout();
        Picasso.with(this).load(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_COVER_URL)).into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void openMapWithLocation (View view) {
        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);

        startActivity(intent);
    }

    //region Tab Layout and View Pager
    /*private void initializeTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        initializeViewPager();
    }

    public void initializeViewPager() {
        EventViewPagerAdapter eventViewPagerAdapter = new EventViewPagerAdapter(getSupportFragmentManager());

        eventViewPagerAdapter.AddFragmentPage(new EventInfoFragment(), "Info");
        eventViewPagerAdapter.AddFragmentPage(new EventsFragment(), "Events");
        eventViewPagerAdapter.AddFragmentPage(new EventInfoFragment(), "Reviews");

        viewPager.setAdapter(eventViewPagerAdapter);
    }

    public class EventViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> pageTitles = new ArrayList<>();

        public EventViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage (Fragment fragment, String title) {
            fragments.add(fragment);
            pageTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return super.getPageTitle(position);
            return pageTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }*/
    //endregion
}

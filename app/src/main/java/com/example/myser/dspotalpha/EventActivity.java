package com.example.myser.dspotalpha;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
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
    public static String SELECTED_DESTINATION = "SELECTED_DESTINATION";

    private double latitude;
    private double longitude;

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

        Picasso.with(this).load(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_COVER_URL)).into(imageView);

        setTicketButton();
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

    public void loadWebsite (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_WEBSITE))));
    }

    private void setTicketButton () {
        //check to see if there are events
        //if there are, then set the events button visible with text: "Events"
        //if just a destination with no events, like Pigeon Point, just put "FREE" for now
    }

    private void viewEvents () {

    }

    private void buyTicket () {

    }

    public void viewOrBuyTicket (View view) {
        Intent eventIntent = new Intent(this, EventsListActivity.class);

        eventIntent.putExtra(SELECTED_DESTINATION, "/" + String.valueOf(title.getText()) + "/Events");
        startActivity(eventIntent);
    }

}

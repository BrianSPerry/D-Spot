package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventsListActivity extends AppCompatActivity {

    private String databasePathParent = "Events and Destinations/";
    private String selectedEvent;

    private Intent intent;

    private ListView listView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        selectedEvent = intent.getStringExtra(EventActivity.SELECTED_DESTINATION);

        listView = (ListView)findViewById(R.id.listView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}

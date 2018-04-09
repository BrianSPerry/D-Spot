package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedsActivity extends AppCompatActivity {

    public static String SELECTED_EVENT_TITLE = "SELECTED_EVENT_TITLE";
    public static String SELECTED_EVENT_DATE = "SELECTED_EVENT_DATE";
    public static String SELECTED_EVENT_DESCRIPTION = "SELECTED_EVENT_DESCRIPTION";
    public static String SELECTED_EVENT_COVER_URL = "SELECTED_EVENT_COVER_URL";
    public static String SELECTED_EVENT_TIME = "SELECTED_EVENT_TIME";
    public static String SELECTED_EVENT_WEBSITE = "SELECTED_EVENT_WEBSITE";
    public static String SELECTED_EVENT_LOCATION = "SELECTED_EVENT_LOCATION";

    public static String SELECTED_LATITUDE = "SELECTED_LATITUDE";
    public static String SELECTED_LONGITUDE = "SELECTED_LONGITUDE";

    private ListView listView;
    private TextView categoryTextView;
    private String selectedCategory;
    private Intent intent;
    private CustomListAdapter listAdapter;
    private Bundle bundle;

    private String databasePath = "Events and Destinations/";

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        bundle = savedInstanceState;
        if (bundle == null) {
            bundle = new Bundle();
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intent = getIntent();

        listView = (ListView) findViewById(R.id.listView);
        categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        selectedCategory = intent.getStringExtra(HomeActivity01.SELECTED_CATEGORY_STRING);

        categoryTextView.setText(selectedCategory);

        //Whereas we can setOnClickListeners inside the BaseAdapter, which we did previously,
        //We need to get the specific index of the selected item, not the recycled index.
        //So when views are recycled, they recycle their indexes as well.
        //the setOnItemClickListener keeps track of all items in the list and thus, returns the correct index when an item is selected.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);

                intent.putExtra(SELECTED_EVENT_TITLE, listAdapter.eventInformations.get(i).title);
                intent.putExtra(SELECTED_EVENT_DATE, listAdapter.eventInformations.get(i).date);
                intent.putExtra(SELECTED_EVENT_DESCRIPTION, listAdapter.eventInformations.get(i).description);
                intent.putExtra(SELECTED_EVENT_COVER_URL, listAdapter.eventInformations.get(i).cover);
                intent.putExtra(SELECTED_EVENT_TIME, listAdapter.eventInformations.get(i).time);
                intent.putExtra(SELECTED_EVENT_WEBSITE, listAdapter.eventInformations.get(i).website);
                intent.putExtra(SELECTED_EVENT_LOCATION, listAdapter.eventInformations.get(i).location);

                intent.putExtra(SELECTED_LATITUDE, listAdapter.eventInformations.get(i).latitude);
                intent.putExtra(SELECTED_LONGITUDE, listAdapter.eventInformations.get(i).longitude);

                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        setItemDetails();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (menuItem.getItemId() == R.id.account) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (menuItem.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    private void setItemDetails () {
        listAdapter = new CustomListAdapter();

        databaseReference.child(databasePath + selectedCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    EventInformation value = childSnapshot.getValue(EventInformation.class);
                    listAdapter.eventInformations.add(value);
                    listAdapter.notifyDataSetChanged();
                }
                listView.setAdapter(listAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "FAILED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class CustomListAdapter extends BaseAdapter {

        public ArrayList<EventInformation> eventInformations = new ArrayList<>();

        @Override
        public int getCount() {
            return eventInformations.size();
        }

        @Override
        public Object getItem(int i) {
            return eventInformations.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ImageView coverImageView;
            TextView titleTextView;
            TextView dateTextView;
            TextView description;

            if (view == null) {
                if (i % 2 == 0) {
                    view = getLayoutInflater().inflate(R.layout.feed_list_item, viewGroup, false);
                }
                else {
                    view = getLayoutInflater().inflate(R.layout.feed_list_item_inverted, viewGroup, false);
                }
                //region old onclick method
                /*view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
                //endregion
            }

            description = (TextView) view.findViewById(R.id.textView14);
            dateTextView = (TextView) view.findViewById(R.id.textView13);
            titleTextView = (TextView)view.findViewById(R.id.textView8);
            coverImageView = (ImageView)view.findViewById(R.id.imageView7);

            Picasso.with(getApplicationContext()).load(eventInformations.get(i).cover).into(coverImageView);
            titleTextView.setText(eventInformations.get(i).title);
            dateTextView.setText(eventInformations.get(i).date);
            description.setText(eventInformations.get(i).description);

            return view;
        }
    }

}

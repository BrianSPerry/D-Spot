package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

public class FeedsFragment extends Fragment {

    public static String SELECTED_EVENT_TITLE = "SELECTED_EVENT_TITLE";
    public static String SELECTED_EVENT_DATE = "SELECTED_EVENT_DATE";
    public static String SELECTED_EVENT_DESCRIPTION = "SELECTED_EVENT_DESCRIPTION";
    public static String SELECTED_EVENT_COVER_URL = "SELECTED_EVENT_COVER_URL";
    public static String SELECTED_EVENT_TIME = "SELECTED_EVENT_TIME";
    public static String SELECTED_EVENT_WEBSITE = "SELECTED_EVENT_WEBSITE";
    public static String SELECTED_EVENT_LOCATION = "SELECTED_EVENT_LOCATION";

    public static String SELECTED_LATITUDE = "SELECTED_LATITUDE";
    public static String SELECTED_LONGITUDE = "SELECTED_LONGITUDE";

    private Bundle bundle;
    private ListView listView;

    private EventInformation eventInformation;
    private String databasePath = "Events and Destinations/";
    private String selectedCategory;
    private CustomListAdapter listAdapter;

    private DatabaseReference databaseReference;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        bundle = savedInstanceState;
        listView = view.findViewById(R.id.listView);

        Bundle bundle = getArguments();
        selectedCategory = bundle.getString(HomeFragment.SELECTED_CATEGORY_STRING);

        //Whereas we can setOnClickListeners inside the BaseAdapter, which we did previously,
        //We need to get the specific index of the selected item, not the recycled index.
        //So when views are recycled, they recycle their indexes as well.
        //the setOnItemClickListener keeps track of all items in the list and thus, returns the correct index when an item is selected.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), EventActivity.class);

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
        return view;
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
                Toast.makeText(getActivity(), "FAILED", Toast.LENGTH_SHORT).show();
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
                view = getLayoutInflater(bundle).inflate(R.layout.feed_list_item, viewGroup, false);
                /*view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });*/
            }

            description = (TextView) view.findViewById(R.id.textView14);
            dateTextView = (TextView) view.findViewById(R.id.textView13);
            titleTextView = (TextView)view.findViewById(R.id.textView8);
            coverImageView = (ImageView)view.findViewById(R.id.imageView7);

            Picasso.with(getActivity()).load(eventInformations.get(i).cover).into(coverImageView);
            titleTextView.setText(eventInformations.get(i).title);
            dateTextView.setText(eventInformations.get(i).date);
            description.setText(eventInformations.get(i).description);

            return view;
        }
    }

}

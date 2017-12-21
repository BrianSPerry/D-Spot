package com.example.myser.dspotalpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        //Toast.makeText(getActivity(), bundle.getString(HomeFragment.SELECTED_CATEGORY_STRING), Toast.LENGTH_SHORT).show();
        //listView.setAdapter(new CustomListAdapter());

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ImageView coverImageView;
            TextView titleTextView;
            TextView dateTextView;
            TextView description;

            if (view == null) {
                view = getLayoutInflater(bundle).inflate(R.layout.feed_list_item, viewGroup, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), EventActivity.class));
                    }
                });
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

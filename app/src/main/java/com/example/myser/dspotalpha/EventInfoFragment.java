package com.example.myser.dspotalpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventInfoFragment extends Fragment {

    private TextView title, date, time, website, description, address, location;
    private ImageView cover;

    private Intent intent;

    public EventInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);

        title = (TextView)view.findViewById(R.id.eventTitleTextView);
        date = (TextView)view.findViewById(R.id.dateTextView);
        time = (TextView)view.findViewById(R.id.timeTextView);
        website = (TextView)view.findViewById(R.id.websiteTextView);
        description = (TextView)view.findViewById(R.id.descriptionTextView);
        address = (TextView)view.findViewById(R.id.addressTextView);
        location = (TextView)view.findViewById(R.id.locationTextView);

        intent = getActivity().getIntent();

        title.setText(String.valueOf(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TITLE)));
        date.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DATE));
        time.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_TIME));
        website.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_WEBSITE));
        description.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DESCRIPTION));
        //address.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_DESCRIPTION));
        location.setText(intent.getStringExtra(FeedsFragment.SELECTED_EVENT_LOCATION));

        return view;
    }

}

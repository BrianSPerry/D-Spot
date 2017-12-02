package com.example.myser.dspotalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class FeedsFragment extends Fragment {

    private Bundle bundle;
    private ListView listView;

    public FeedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        bundle = savedInstanceState;
        listView = view.findViewById(R.id.listView);

        listView.setAdapter(new CustomListAdapter());
        return view;
    }

    public class CustomListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater(bundle).inflate(R.layout.feed_list_item, viewGroup, false);
            }
            return view;
        }
    }

}

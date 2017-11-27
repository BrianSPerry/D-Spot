package com.example.myser.dspotalpha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by myser on 23-Nov-17.
 */

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<View> list;

    public CustomListAdapter(Context _context) {
        context = _context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //view = inflater.inflate(R.layout.fragment_preference, viewGroup, false);
        return null;
    }
}

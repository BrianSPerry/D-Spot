package com.example.myser.dspotalpha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private OnWindowSizeChanged windowSizeChanged;

    private int numberOfPreferences = 0;
    private int numberOfSelectedPrefs = 0;
    private PreferencesArrayData preferencesArrayData;
    private LinearLayout[] preferenceImages;
    private GridLayout gridLayout;

    public interface OnWindowSizeChanged {
        public void getChildren(GridLayout layout);
    }

    public GridLayout getGridLayout() {
        //return (GridLayout)getActivity().findViewById(R.id.homeFragmentGridLayout);
        return gridLayout;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            windowSizeChanged = (OnWindowSizeChanged) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnWindowSizeChanged");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Something I made:
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        gridLayout = (GridLayout)getActivity().findViewById(R.id.homeFragmentGridLayout);
        preferencesArrayData = new PreferencesArrayData(getActivity());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        numberOfPreferences = sharedPreferences.getInt(getResources().getString(R.string.number_of_preferences), 0);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Allow the ability to add preferences here...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });*/

        /*initializePrefsImageViewArray();
        displaySelectedPreferences();*/

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void displaySelectedPreferences () {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        numberOfSelectedPrefs = sharedPreferences.getInt(getResources().getString(R.string.selected_prefs_amt), 0);

        for (int i = 0; i < numberOfPreferences; i++) {
            if (sharedPreferences.getString(preferencesArrayData.preferenceKeys[i], "default value").equals(preferencesArrayData.preferenceValues[i])) {
                preferenceImages[i].setVisibility(View.VISIBLE);
            }
            else {
                preferenceImages[i].setVisibility(View.GONE);
            }

        }

    }

    private void initializePrefsImageViewArray () {
        preferenceImages = new LinearLayout[numberOfPreferences];

        preferenceImages[0] = (LinearLayout) getActivity().findViewById(R.id.prefs1);
        preferenceImages[1] = (LinearLayout) getActivity().findViewById(R.id.prefs2);
        preferenceImages[2] = (LinearLayout) getActivity().findViewById(R.id.prefs3);
        preferenceImages[3] = (LinearLayout) getActivity().findViewById(R.id.prefs4);
        preferenceImages[4] = (LinearLayout) getActivity().findViewById(R.id.prefs5);
        preferenceImages[5] = (LinearLayout) getActivity().findViewById(R.id.prefs6);

        for (int i = 0; i < preferenceImages.length; i++) {
            preferenceImages[i].setVisibility(View.GONE);
        }
    }

    public void loadPreference01(View view) {
        Toast.makeText(getActivity(), "Mega Penis!", Toast.LENGTH_SHORT).show();
    }

    public void loadFeed(View view) {

    }

    //region GetChildren
    /*public void getChildren(GridLayout layout) {
        ViewGroup.LayoutParams params;
        preferenceImages = new LinearLayout[layout.getChildCount()];
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels / 3;

        for (int i = 0; i < layout.getChildCount(); i++) {
            preferenceImages[i] = (LinearLayout) layout.getChildAt(i);
            params = preferenceImages[i].getLayoutParams();
            params.width = width;

            preferenceImages[i].setLayoutParams(params);
        }
    }*/
    //endregion

}

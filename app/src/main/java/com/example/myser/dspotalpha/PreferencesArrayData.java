package com.example.myser.dspotalpha;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by myser on 24-Oct-17.
 */

public class PreferencesArrayData {
    public String[] preferenceKeys;
    public String[] preferenceValues;

    public PreferencesArrayData (Activity appCompatActivity) {
        preferenceKeys = new String[6];
        preferenceValues = new String[6];

        //Keys:
        preferenceKeys[0] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs1));
        preferenceKeys[1] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs2));
        preferenceKeys[2] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs3));
        preferenceKeys[3] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs4));
        preferenceKeys[4] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs5));
        preferenceKeys[5] = String.valueOf(appCompatActivity.getResources().getString(R.string.prefs6));

        //Values:
        preferenceValues[0] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_1));
        preferenceValues[1] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_2));
        preferenceValues[2] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_3));
        preferenceValues[3] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_4));
        preferenceValues[4] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_5));
        preferenceValues[5] = String.valueOf(appCompatActivity.getResources().getString(R.string.preference_6));
    }
}

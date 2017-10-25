package com.example.myser.dspotalpha;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private int numberOfPreferences = 0;
    private PreferencesArrayData preferencesArrayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferencesArrayData = new PreferencesArrayData(this);

        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);
        numberOfPreferences = sharedPreferences.getInt(getResources().getString(R.string.number_of_preferences), 0);

        displaySelectedPreferences();
    }

    private void displaySelectedPreferences () {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesActivity.preferenceName, Context.MODE_PRIVATE);

        for (int i = 0; i < numberOfPreferences; i++) {
            if (sharedPreferences.getString(preferencesArrayData.preferenceKeys[i], "default value").equals(preferencesArrayData.preferenceValues[i])) {
                Toast.makeText(this, String.valueOf(sharedPreferences.getString(preferencesArrayData.preferenceKeys[i], "default value")), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Double Penis", Toast.LENGTH_SHORT).show();
            }

        }
    }

}

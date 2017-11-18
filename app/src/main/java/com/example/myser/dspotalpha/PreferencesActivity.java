package com.example.myser.dspotalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    //The name of the SharedPreferences file:
    public static String preferenceName = "com.example.myser.dspotalpha";

    //Array of checkboxes (preferenceKeys):
    private CheckBox[] checkBoxes;
    private ImageView[] imageViews;
    private LinearLayout[] preferenceImages;
    //This value counts the number of unselected checkboxes:
    private int uncheckedCheckboxes = 0;
    private int numberOfSelectedPrefs = 0;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //Set's the name of the SharedPreferences file to the package name in case I change it
        //and forget to change the default text where I declared the variable
        preferenceName = getPackageName();
        gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        initializeCheckboxArray();
        determineActivity();
    }

    @Override
    public void onWindowFocusChanged(boolean focus) {
        getChildren(gridLayout);
    }

    //Initialize and populates the array of checkboxes.
    //Dynamically adds the total number of preferenceKeys (checkboxes) to the SharedPreferences file.
    //I'm doing that so that I can always know exactly how many preferenceKeys to cycle through in loops in other activities.
    private void initializeCheckboxArray () {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        checkBoxes = new CheckBox[6];
        imageViews = new ImageView[6];

        //Checkboxes:
        checkBoxes[0] = (CheckBox)findViewById(R.id.checkBox1);
        checkBoxes[1] = (CheckBox)findViewById(R.id.checkBox2);
        checkBoxes[2] = (CheckBox)findViewById(R.id.checkBox3);
        checkBoxes[3] = (CheckBox)findViewById(R.id.checkBox4);
        checkBoxes[4] = (CheckBox)findViewById(R.id.checkBox5);
        checkBoxes[5] = (CheckBox)findViewById(R.id.checkBox6);

        //Image Views:
        imageViews[0] = (ImageView) findViewById(R.id.imageView1);
        imageViews[1] = (ImageView)findViewById(R.id.imageView2);
        imageViews[2] = (ImageView)findViewById(R.id.imageView3);
        imageViews[3] = (ImageView)findViewById(R.id.imageView4);
        imageViews[4] = (ImageView)findViewById(R.id.imageView5);
        imageViews[5] = (ImageView)findViewById(R.id.imageView6);

        editor.putInt(getResources().getString(R.string.number_of_preferences), checkBoxes.length);
        editor.apply();
    }

    //This method is called when the Select button is clicked:
    public void saveUserPreferences (View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //We set the uncheckedCheckboxes counter to 0 every time this method is called
        //to prevent it from ever incrementing.
        uncheckedCheckboxes = 0;

        //Loops through all checkboxes in the array to determine which is and isn't checked.
        //If one is checked, it gets the tag of that checkbox and uses it as a key which is paired with the value of it's text.
        //The key (the tag) will be used to get the value (the text of the text box) in the SharedPreferences file.
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                numberOfSelectedPrefs++;
                editor.putString(String.valueOf(checkBoxes[i].getTag()), String.valueOf(checkBoxes[i].getText()));
                editor.putInt(getResources().getString(R.string.selected_prefs_amt), numberOfSelectedPrefs);
                editor.apply();
            }
            else {
                //If a checkbox is encountered that is unchecked, it increments the following value by 1.
                //If this value reaches checkboxes.length, that means all checkboxes are unchecked.
                uncheckedCheckboxes++;
            }
        }

        //If the uncheckedCheckboxes variable is above or equal to checkboxes.length, that means all checkboxes were unchecked.
        //If that is the case, it instructs the user to select at least one checkbox.
        //If one ore more checkbox is checked, then it loads the next activity.
        if (uncheckedCheckboxes >= checkBoxes.length) {
            Toast.makeText(this, "Select at least one category before proceeding...", Toast.LENGTH_LONG).show();
        }
        else {
            editor.putBoolean(getResources().getString(R.string.has_selected_preferences), true);
            editor.apply();

            Intent intent = new Intent(this, HomeActivity01.class);
            //Intent intent = new Intent(LoginActivity.this, AccountSetupActivity.class);
            //startActivity(new Intent(this, HomeActivity01.class));
            intent.putExtra(LoginActivity.FRAGMENT_TO_LOAD, LoginActivity.shouldLoadHomeFragmentValue);
            startActivity(intent);
        }
    }

    public void loadPreference (View view) {
        for (int i = 0; i < imageViews.length; i++) {
            if (view.getTag().equals(checkBoxes[i].getTag())) {
                checkBoxes[i].setChecked(!checkBoxes[i].isSelected());
            }
        }
    }

    private void determineActivity () {
        SharedPreferences sharedPreferences = getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(getResources().getString(R.string.has_selected_preferences), false)) {
            Intent intent = new Intent(this, HomeActivity01.class);

            intent.putExtra(LoginActivity.FRAGMENT_TO_LOAD, LoginActivity.shouldLoadHomeFragmentValue);
            startActivity(intent);
        }
        else {
            return;
        }
    }

    private void getChildren(GridLayout layout) {
        ViewGroup.LayoutParams params;
        preferenceImages = new LinearLayout[layout.getChildCount()];
        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels / 3;

        for (int i = 0; i < layout.getChildCount(); i++) {
            preferenceImages[i] = (LinearLayout) layout.getChildAt(i);
            params = preferenceImages[i].getLayoutParams();
            params.width = width;

            preferenceImages[i].setLayoutParams(params);
        }
    }

}

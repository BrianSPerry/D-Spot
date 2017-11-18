package com.example.myser.dspotalpha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreferenceFragment extends Fragment {

    //The name of the SharedPreferences file:
    public static String preferenceName = "com.example.myser.dspotalpha";

    //Array of checkboxes (preferenceKeys):
    private CheckBox[] checkBoxes;
    //This value counts the number of unselected checkboxes:
    private int uncheckedCheckboxes = 0;
    private int numberOfSelectedPrefs = 0;
    //private ImageView[] imageViews;
    //private LinearLayout[] preferenceImages;
    //private GridLayout gridLayout;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        //Set's the name of the SharedPreferences file to the package name in case I change it
        //and forget to change the default text where I declared the variable
        preferenceName = getActivity().getPackageName();
        //gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);

        initializeCheckboxArray(view);
        determineActivity();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = HomeActivity01.firebaseAuthentication.getCurrentUser();

        return view;
    }

    //Initialize and populates the array of checkboxes.
    //Dynamically adds the total number of preferenceKeys (checkboxes) to the SharedPreferences file.
    //I'm doing that so that I can always know exactly how many preferenceKeys to cycle through in loops in other activities.
    private void initializeCheckboxArray (View view) {
        //region SharedPreferences
        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();*/
        //endregion
        checkBoxes = new CheckBox[6];
        //imageViews = new ImageView[6];

        //Checkboxes:
        checkBoxes[0] = (CheckBox)view.findViewById(R.id.checkBox1);
        checkBoxes[1] = (CheckBox)view.findViewById(R.id.checkBox2);
        checkBoxes[2] = (CheckBox)view.findViewById(R.id.checkBox3);
        checkBoxes[3] = (CheckBox)view.findViewById(R.id.checkBox4);
        checkBoxes[4] = (CheckBox)view.findViewById(R.id.checkBox5);
        checkBoxes[5] = (CheckBox)view.findViewById(R.id.checkBox6);

        //Image Views:
        //region ImageViews
        /*imageViews[0] = (ImageView)view.findViewById(R.id.imageView1);
        imageViews[1] = (ImageView)view.findViewById(R.id.imageView2);
        imageViews[2] = (ImageView)view.findViewById(R.id.imageView3);
        imageViews[3] = (ImageView)view.findViewById(R.id.imageView4);
        imageViews[4] = (ImageView)view.findViewById(R.id.imageView5);
        imageViews[5] = (ImageView)view.findViewById(R.id.imageView6);*/
        //endregion
        //region Using Shared Preferences
        /*editor.putInt(getResources().getString(R.string.number_of_preferences), checkBoxes.length);
        editor.apply();*/
        //endregion
    }

    //This method is called when the Select button is clicked:
    public void saveUserPreferences (View view) {
        //region SharedPreferences Declaration
        /*SharedPreferences sharedPreferences = getActivity().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();*/
        //endregion
        //We set the uncheckedCheckboxes counter to 0 every time this method is called
        //to prevent it from ever incrementing.
        uncheckedCheckboxes = 0;

        //Loops through all checkboxes in the array to determine which is and isn't checked.
        //If one is checked, it gets the tag of that checkbox and uses it as a key which is paired with the value of it's text.
        //The key (the tag) will be used to get the value (the text of the text box) in the SharedPreferences file.
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                SelectedPreferences selectedPreferences = new SelectedPreferences(checkBoxes[i].getTag().toString());

                numberOfSelectedPrefs++;
                databaseReference.child(firebaseUser.getUid()).setValue(selectedPreferences);

                //region Shared Preferences Saving
                /*editor.putString(String.valueOf(checkBoxes[i].getTag()), String.valueOf(checkBoxes[i].getText()));
                editor.putInt(getResources().getString(R.string.selected_prefs_amt), numberOfSelectedPrefs);
                editor.apply();*/
                //endregion
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
            Toast.makeText(getActivity(), "Select at least one category before proceeding...", Toast.LENGTH_LONG).show();
        }
        else {
            //region Saving whether or not preferences have been saved
            /*editor.putBoolean(getResources().getString(R.string.has_selected_preferences), true);
            editor.apply();*/
            //endregion
            startActivity(new Intent(getActivity(), HomeActivity01.class));
        }
    }

    //region Method used to check/uncheck checkboxes when an associated ImageView is selected
    /*public void loadPreference (View view) {
        for (int i = 0; i < imageViews.length; i++) {
            if (view.getTag().equals(checkBoxes[i].getTag())) {
                checkBoxes[i].setChecked(!checkBoxes[i].isSelected());
            }
        }
    }*/
    //endregion

    private void determineActivity () {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(getResources().getString(R.string.has_selected_preferences), false)) {
            Intent intent = new Intent(getActivity(), HomeActivity01.class);

            intent.putExtra(LoginActivity.FRAGMENT_TO_LOAD, LoginActivity.shouldLoadHomeFragmentValue);
            startActivity(intent);
        }
        else {
            return;
        }
    }

    //region Method used to resize views depending on screen size and or screen orientation
    /*private void getChildren(GridLayout layout) {
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

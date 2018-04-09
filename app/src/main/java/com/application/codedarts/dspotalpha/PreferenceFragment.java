package com.application.codedarts.dspotalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceFragment extends Fragment {

    //The name of the SharedPreferences file:
    public static String preferenceName = "com.example.myser.dspotalpha";

    private String parentNode = "User Preferences/";
    private String childNode = "/Preference ";

    private ArrayList<String> databasePreferenceValues = new ArrayList<>();
    private CustomAdapter customAdapter;
    private Bundle bundle;
    private ListView listView;

    private FirebaseAuth firebaseAuthentication;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencePrefs;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);
        bundle = savedInstanceState;
        preferenceName = getActivity().getPackageName();

        listView = view.findViewById(R.id.listView);

        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuthentication.getCurrentUser();

        getPreferencesFromDatabase();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Preferences");
        }
        else {
            Toast.makeText(getActivity(), "Null Action Bar", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void getPreferencesFromDatabase () {
        databaseReferencePrefs.child("Preferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customAdapter = new CustomAdapter();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    customAdapter.list.add(value);
                    //databasePreferenceValues.add(value);
                    //customAdapter.list = databasePreferenceValues;
                    customAdapter.notifyDataSetChanged();
                }
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class CustomAdapter extends BaseAdapter {
        public ArrayList<String> list = new ArrayList<>();
        public ArrayList<ViewGroup> viewGroups = new ArrayList<ViewGroup>();

        private ArrayList<View> views = new ArrayList<View>();
        private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        private HashMap<CheckBox, Boolean> checkBoxBooleanHashMap = new HashMap<>();
        private ArrayList<Boolean> checkboxStates = new ArrayList<>();

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
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            CheckBox checkbox;
            if (view == null) {
                view = getLayoutInflater(bundle).inflate(R.layout.preference_selection_list_item, viewGroup, false);
                checkbox = (CheckBox)view.findViewById(R.id.checkBox1);

                views.add(view);
                viewGroups.add((ViewGroup) view);
                checkBoxes.add(checkbox);
            }

            checkbox = (CheckBox)view.findViewById(R.id.checkBox1);
            if (checkboxStates.size() != list.size()) {
                checkboxStates.add(checkbox.isChecked());
            }
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        //databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid() + "/" + "Preference " + String.valueOf(i)).setValue(compoundButton.getText());
                        databaseReferencePrefs.child(parentNode + firebaseUser.getUid() + childNode + String.valueOf(i)).setValue(compoundButton.getText());
                        checkboxStates.set(i, b);
                    }
                    else {
                        //databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid() + "/ " + "Preference " + String.valueOf(i)).removeValue();
                        databaseReferencePrefs.child(parentNode + firebaseUser.getUid() + childNode + String.valueOf(i)).removeValue();
                        checkboxStates.set(i, b);
                    }
                }
            });
            //Log.e("CheckBoxes", "Index value: " + String.valueOf(i)); //0 - 16
            //!Log.e("CheckBoxes", "Views: " + String.valueOf(views.size())); //1 - 11
            //Log.e("CheckBoxes", "List: " + String.valueOf(list.size()) + "\n\n"); //instant 17

            Log.e("CheckBoxes", "Checkbox States: " + String.valueOf(checkboxStates.size())); //1 - 11
            Log.e("CheckBoxes", "Checkbox States: " + String.valueOf(checkboxStates.get(i)));
            checkbox.setChecked(checkboxStates.get(i));
            checkbox.setText(list.get(i));
            return view;
        }
    }

}

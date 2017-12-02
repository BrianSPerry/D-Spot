package com.example.myser.dspotalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreferenceFragment extends Fragment {

    //The name of the SharedPreferences file:
    public static String preferenceName = "com.example.myser.dspotalpha";

    private ArrayList<String> databasePreferenceValues = new ArrayList<>();
    private CustomAdapter customAdapter;
    private Bundle bundle;
    private ListView listView;

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        firebaseUser = HomeActivity01.firebaseAuthentication.getCurrentUser();

        getPreferencesFromDatabase();

        return view;
    }

    private void getPreferencesFromDatabase () {
        databaseReferencePrefs.child("Preferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customAdapter = new CustomAdapter();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    databasePreferenceValues.add(value);
                    customAdapter.list = databasePreferenceValues;
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
        public ArrayList<String> list;
        public ArrayList<ViewGroup> viewGroups = new ArrayList<ViewGroup>();
        private ArrayList<View> views = new ArrayList<View>();
        private ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();

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

                checkbox.setChecked(false);
                views.add(view);
                viewGroups.add((ViewGroup) view);
                checkBoxes.add(checkbox);
            }

            checkbox = (CheckBox)view.findViewById(R.id.checkBox1);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid() + "/" + "Preference " + String.valueOf(i)).setValue(compoundButton.getText());
                    }
                    else {
                        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid() + "/" + "Preference " + String.valueOf(i)).removeValue();
                    }
                }
            });
            checkbox.setText(list.get(i));
            return view;
        }
    }

}

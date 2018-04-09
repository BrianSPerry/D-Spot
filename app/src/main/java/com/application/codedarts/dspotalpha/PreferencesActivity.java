package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PreferencesActivity extends AppCompatActivity {

    private String parentNode = "User Preferences/";
    private String childNode = "/Preference ";

    private ListView listView;
    private CustomAdapter customAdapter;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencePrefs;
    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = (ListView)findViewById(R.id.listView);

        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuthentication.getCurrentUser();

        getPreferencesFromDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.account) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (menuItem.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    private void getPreferencesFromDatabase () {
        databaseReferencePrefs.child("Preferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customAdapter = new CustomAdapter();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    customAdapter.list.add(value);
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
                view = getLayoutInflater().inflate(R.layout.preference_selection_list_item, viewGroup, false);
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
                        databaseReferencePrefs.child(parentNode + firebaseUser.getUid() + childNode + String.valueOf(i)).setValue(compoundButton.getText());
                        checkboxStates.set(i, b);
                    }
                    else {
                        databaseReferencePrefs.child(parentNode + firebaseUser.getUid() + childNode + String.valueOf(i)).removeValue();
                        checkboxStates.set(i, b);
                    }
                }
            });

            checkbox.setChecked(checkboxStates.get(i));
            checkbox.setText(list.get(i));
            return view;
        }
    }

}

package com.example.myser.dspotalpha;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferenceFragment extends Fragment {

    //The name of the SharedPreferences file:
    public static String preferenceName = "com.example.myser.dspotalpha";

    private ArrayList<String> databasePreferenceValues = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

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
        //Set's the name of the SharedPreferences file to the package name in case I change it
        //and forget to change the default text where I declared the variable
        preferenceName = getActivity().getPackageName();

        listView = view.findViewById(R.id.listView);
        //arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.preference_selection_list_item, databasePreferenceValues);
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.preference_selection_list_item, R.id.checkBox1, databasePreferenceValues);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        firebaseUser = HomeActivity01.firebaseAuthentication.getCurrentUser();

        getPreferencesFromDatabase();

        listView.setAdapter(arrayAdapter);
        //listView.setAdapter(new CustomListAdapter(getContext()));

        return view;
    }

    private void getPreferencesFromDatabase () {
        databaseReferencePrefs.child("Preferences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    databasePreferenceValues.add(value);
                    arrayAdapter.notifyDataSetChanged();
                    //Toast.makeText(getActivity(), childSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}

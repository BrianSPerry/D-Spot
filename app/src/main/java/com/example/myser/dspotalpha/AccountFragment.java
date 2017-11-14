package com.example.myser.dspotalpha;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView titleTextView;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText genderEditText;
    private EditText biographyEditText;

    private DatabaseReference databaseReference;
    FirebaseUser user;

    private UserInformation userInformation;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        nameEditText = (EditText)view.findViewById(R.id.nameEditText);
        passwordEditText = (EditText)view.findViewById(R.id.passwordEditText);
        genderEditText = (EditText)view.findViewById(R.id.genderEditText);
        biographyEditText = (EditText)view.findViewById(R.id.biographyEditText);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = HomeActivity01.firebaseAuthentication.getCurrentUser();

        initializeUI();

        return view;
    }

    private void initializeUI () {
        titleTextView.setText("Welcome, " + user.getEmail() + "!");
    }

    public void saveInformation (View view) {
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String bio = biographyEditText.getText().toString();
        userInformation = new UserInformation(name, gender, bio);

        databaseReference.child(user.getUid()).setValue(userInformation);
    }

}

package com.example.myser.dspotalpha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final int CHOOSE_IMAGE_REQUEST = 234;

    public Button saveButton, logOutButton;

    private TextView titleTextView;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText genderEditText;
    private EditText biographyEditText;
    private ImageView profilePhotoImageView;
    private Uri filePath;
    private Bitmap bitmap;

    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private StorageReference storageReference;

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

        saveButton = (Button) view.findViewById(R.id.saveInformationButton);
        logOutButton = (Button) view.findViewById(R.id.logoutButton);
        profilePhotoImageView = (ImageView) view.findViewById(R.id.profilePhotoImageView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = HomeActivity01.firebaseAuthentication.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        initializeUI();

        if (filePath != null) {
            profilePhotoImageView.setImageBitmap(bitmap);
        }

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

    public void getPhotoFile (View view) {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture..."), CHOOSE_IMAGE_REQUEST);
        uploadFile();
    }

    private void uploadFile () {
        if (filePath != null) {
            StorageReference riversRef = storageReference.child("images/profile_photo.jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Photo uploaded...", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), "Unable to upload photo...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE_REQUEST/* && requestCode == getActivity().RESULT_OK && data != null && data.getData() != null*/) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profilePhotoImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void toggleView (View view, boolean on) {
        if (on) {
            view.setVisibility(View.VISIBLE);
        }
        else {
            view.setVisibility(View.GONE);
        }
    }

}

package com.example.myser.dspotalpha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;*/
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final int CHOOSE_IMAGE_REQUEST = 234;

    public Button saveButton, logOutButton;
    public static String userInformationNode = "User Information/";

    private TextView titleTextView;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText genderEditText;
    private EditText biographyEditText;
    private ImageView profilePhotoImageView;
    private Uri filePath;
    private File downloadedImage;
    private Bitmap bitmap;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceLoader;
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
        databaseReferenceLoader = FirebaseDatabase.getInstance().getReference();
        user = HomeActivity01.firebaseAuthentication.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        initializeUI();

        if (filePath != null) {
            profilePhotoImageView.setImageBitmap(bitmap);
        }

        try {
            downloadedImage = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        downloadAndSetThumbnails();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        databaseReferenceLoader.child("User Information").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                //region works
//                /*
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                    UserInformation value = childSnapshot.getValue(UserInformation.class);
//                    Toast.makeText(getActivity(), "DATA CHANGED " + value.name, Toast.LENGTH_LONG).show();
//                }
//                */
//                //endregion
//                //UserInformation name = dataSnapshot.child(userInformationNode + user.getUid()).getValue(UserInformation.class);
//                //UserInformation value = dataSnapshot.getValue(UserInformation.class);
//                //Toast.makeText(getActivity(), "DATA CHANGED " + value.name, Toast.LENGTH_LONG).show();
//
//                //UserInformation value = dataSnapshot.getValue(UserInformation.class);
//                UserInformation value = dataSnapshot.child((user.getUid())).getValue(UserInformation.class);
//                Toast.makeText(getActivity(), "DATA CHANGED: " + value.name, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Toast.makeText(getActivity(), "NO DATA CHANGED", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //I HAVE TO CHECK TO SEE IF THE USER HAS VALID INFO UP ON THEIR PROFILE BEFORE ATTEMPTING TO SET THE INFO TO THE UI.
    //FAILURE TO DO SO WILL RESULT IN CRASHES EVERY TIME THE USER ATTEMPTS TO OPEN THIS FRAGMENT.
    private void initializeUI () {
        titleTextView.setText("Welcome, " + user.getEmail() + "!");

        databaseReferenceLoader.child("User Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserInformation value = dataSnapshot.child((user.getUid())).getValue(UserInformation.class);

                nameEditText.setText(value.name);
                genderEditText.setText(value.gender);
                biographyEditText.setText(value.bio);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "NO DATA CHANGED", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveInformation (View view) {
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String bio = biographyEditText.getText().toString();
        userInformation = new UserInformation(name, gender, bio);

        databaseReference.child(userInformationNode + user.getUid()).setValue(userInformation);
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
            StorageReference riversRef = storageReference.child("Profile Photos/" + user.getUid() + "/profile_photo.jpg");

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

    private void downloadAndSetThumbnails () {
        storageReference.child("Profile Photos/" + user.getUid() + "/profile_photo.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                profilePhotoImageView.setMinimumHeight(dm.heightPixels);
                profilePhotoImageView.setMinimumWidth(dm.widthPixels);
                profilePhotoImageView.setImageBitmap(bm);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        //region Older Download method
        /*StorageReference riversRef = storageReference.child("Preference Default Thumbnails/" + "nightlife_thumb.jpg");
        riversRef.getFile(downloadedImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                //profilePhotoImageView.setImageURI(taskSnapshot.getStorage().getDownloadUrl());
                //Picasso.with(getActivity()).load("http://assets1.ignimgs.com/2017/05/02/darksiders-3---s02-1493750188165_1280w.jpg").into(profilePhotoImageView);
                *//*Glide.with(getActivity())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .into(profilePhotoImageView);*//*
                //Toast.makeText(getActivity(), taskSnapshot.getStorage().getName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), taskSnapshot.getStorage().getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();
                *//*try {
                    //bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(downloadedImage.getName()));
                    //profilePhotoImageView.setImageBitmap(bitmap);
                    profilePhotoImageView.setImageURI(Uri.parse(downloadedImage.getName()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), Uri.parse(downloadedImage.getName()).toString(), Toast.LENGTH_SHORT).show();
                }*//*
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });*/
        //endregion
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

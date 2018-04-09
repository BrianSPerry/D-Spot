package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AccountFragment extends Fragment {

    private static final int CHOOSE_IMAGE_REQUEST = 234;

    public Button saveButton, logOutButton;
    public static String userInformationNode = "User Information/";
    private boolean isUploaded = false;

    private TextView titleTextView;
    private EditText nameEditText;
    private EditText cityCountryTextView;
    private EditText genderEditText;
    private EditText biographyEditText;
    private ImageView profilePhotoImageView;

    private Uri filePath;
    private File downloadedImage;
    private Bitmap bitmap;
    private Handler uploadHandler = new Handler();;
    private Timer timer;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceLoader;
    private FirebaseUser user;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuthentication;

    private UserInformation userInformation;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        titleTextView = (TextView)view.findViewById(R.id.titleTextView);
        nameEditText = (EditText)view.findViewById(R.id.nameEditText);
        cityCountryTextView = (EditText)view.findViewById(R.id.cityCountryEditText);
        genderEditText = (EditText)view.findViewById(R.id.genderEditText);
        biographyEditText = (EditText)view.findViewById(R.id.biographyEditText);

        saveButton = (Button) view.findViewById(R.id.saveInformationButton);
        logOutButton = (Button) view.findViewById(R.id.logoutButton);
        profilePhotoImageView = (ImageView) view.findViewById(R.id.roundFrameImage);

        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceLoader = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuthentication.getCurrentUser();
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

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Account");
        }
        else {
            Toast.makeText(getActivity(), "Null Action Bar", Toast.LENGTH_SHORT).show();
        }

        downloadAndSetThumbnails();

        return view;
    }

    private void initializeUI () {
        //titleTextView.setText("Welcome, " + user.getEmail() + "!");
        databaseReferenceLoader.child("User Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation value = dataSnapshot.child((user.getUid())).getValue(UserInformation.class);

                if (value != null) {
                    nameEditText.setText(value.name);
                    genderEditText.setText(value.gender);
                    cityCountryTextView.setText(value.country);
                    biographyEditText.setText(value.bio);
                    titleTextView.setText("Welcome, " + value.name + "!");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void saveInformation (View view) {
        String name = nameEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String bio = biographyEditText.getText().toString();
        String country = cityCountryTextView.getText().toString();
        userInformation = new UserInformation(name, gender, bio, country);

        databaseReference.child(userInformationNode + user.getUid()).setValue(userInformation);
    }

    public void getPhotoFile (View view) {
        Intent intent = new Intent();

        Log.e("PHOTO_UPLOAD", "getPhotoFile(View) called...");
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a profile picture..."), CHOOSE_IMAGE_REQUEST);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e("PHOTO_UPLOAD", "run() called...");
                uploadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("PHOTO_UPLOAD", "run() inner, called...");
                        if (filePath != null) {
                            Log.e("PHOTO_UPLOAD", "File path not null...");
                            uploadFile();
                            Log.e("PHOTO_UPLOAD", "callbacks are taking place");
                            if (isUploaded) {
                                //uploadHandler.removeCallbacks(this);
                                Log.e("PHOTO_UPLOAD", "photo uploaded...");
                                timer.cancel();
                            }
                        }
                        if (isUploaded) {
                            Log.e("PHOTO_UPLOAD", "Photo uploaded outer...");
                            //uploadHandler.removeCallbacks(this);
                            timer.cancel();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void uploadFile () {
        if (filePath != null) {
            StorageReference riversRef = storageReference.child("Profile Photos/" + user.getUid() + "/profile_photo.jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Photo uploaded...", Toast.LENGTH_SHORT).show();
                            isUploaded = true;
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
                //getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

                if (bm != null) {
                    profilePhotoImageView.setMinimumHeight(dm.heightPixels);
                    profilePhotoImageView.setMinimumWidth(dm.widthPixels);
                    profilePhotoImageView.setImageBitmap(bm);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
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

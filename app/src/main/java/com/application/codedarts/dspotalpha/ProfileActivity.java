package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView cityCountryTextView;
    private ImageView profilePhotoImageView;

    private RoundedBitmapDrawable roundedBitmapDrawable;

    private FirebaseAuth firebaseAuthentication;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceLoader;
    private FirebaseUser user;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = (TextView)findViewById(R.id.firstNameLastNameTextView);
        cityCountryTextView = (TextView)findViewById(R.id.cityCountryTextView);
        profilePhotoImageView = (ImageView)findViewById(R.id.profilePhotoImageView);
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.profilePhotoBackground)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.profileStatusBar));
        }

        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceLoader = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuthentication.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        initializeUI();
        downloadAndSetThumbnails();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                this.finish();
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public void editProfile (View view) {
        startActivity(new Intent(this, AccountActivity.class));
    }

    private void initializeUI () {
        databaseReferenceLoader.child("User Information").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation value = dataSnapshot.child((user.getUid())).getValue(UserInformation.class);

                if (value != null) {
                    nameTextView.setText(value.name);
                    cityCountryTextView.setText(value.country);

                    if (value.country != null) {
                        if (value.country.equals("")) {
                            cityCountryTextView.setText("Unknown Country");
                        }
                    }
                    else {
                        cityCountryTextView.setText("Unknown Country");
                    }
                }
                else {
                    nameTextView.setText(user.getEmail());
                    cityCountryTextView.setText("Unknown Country");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void downloadAndSetThumbnails () {
        storageReference.child("Profile Photos/" + user.getUid() + "/profile_photo.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();

                roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bm);

                roundedBitmapDrawable.setCircular(true);

                if (bm != null) {
                    profilePhotoImageView.setMinimumHeight(dm.heightPixels);
                    profilePhotoImageView.setMinimumWidth(dm.widthPixels);
                    profilePhotoImageView.setImageDrawable(roundedBitmapDrawable);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void seeBoughtTickets (View view) {
        startActivity(new Intent(this, PurchasedTicketsActivity.class));
    }

    public void  seeUpcomingEvents (View view) {
        Toast.makeText(this, "Will display upcoming events in categories you've subscribed to.", Toast.LENGTH_LONG).show();
    }

    public void seePastEvents (View view) {
        Toast.makeText(this, "Will display past events in categories you've subscribed to.", Toast.LENGTH_LONG).show();
    }

    public void seeFollowing (View view) {
        Toast.makeText(this, "Will display...something...", Toast.LENGTH_LONG).show();
    }

}

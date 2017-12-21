package com.example.myser.dspotalpha;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.myser.dspotalpha.R.id.profilePhotoImageView;

public class HomeFragment extends Fragment {

    private FeedsFragment feedsFragmentFragment = new FeedsFragment();
    private PreferenceFragment prefsFragmentFragment = new PreferenceFragment();
    private FragmentManager fragmentManager;

    private GridView gridView;
    private ConstraintLayout constraintLayoutProgressBar;

    public static final String SELECTED_CATEGORY_STRING = "SELECTED_CATEGORY";
    private Bundle bundle;
    private GridAdapter gridAdapter;
    public ArrayList<String> strings = new ArrayList<>();

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReferencePrefs;
    private StorageReference storageReference;
    private URL url;
    private File file;
    private ArrayList<File> files = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bundle = savedInstanceState;
        if (bundle == null) {
            bundle = new Bundle();
        }
        //Toast.makeText(getActivity(), (bundle == null) ? "Null!" : "Not Null", Toast.LENGTH_SHORT).show();
        fragmentManager = getActivity().getSupportFragmentManager();
        gridView = view.findViewById(R.id.gridView);
        constraintLayoutProgressBar = view.findViewById(R.id.constraintLayoutProgressBar);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Allow the ability to add preferences here...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, prefsFragmentFragment).addToBackStack(null).commit();
            }
        });

        try {
            file = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        firebaseUser = HomeActivity01.firebaseAuthentication.getCurrentUser();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        getSelectedPreferences();
        //downloadAndSetThumbnails();

        return view;
    }

    private void getSelectedPreferences () {
        gridAdapter = new GridAdapter();
        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gridAdapter = new GridAdapter();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    gridAdapter.strings.add(value);

                    if (gridAdapter.strings.size() > 0) {
                        constraintLayoutProgressBar.setVisibility(View.GONE);
                    }
                    gridAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //even if a preference is not selected,
        //the thumbnail attributed to it
        //gets set to the next selected preference.
        //So if i selected prefs 1, prefs 1 thumbnail is set to it
        //If I don't select prefs 2 but select prefs 3, prefs 2's thumbnail gets set to prefs 3.
        databaseReferencePrefs.child("Preference URL Thumbnails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    gridAdapter.thumbnailURLs.add(value);
                    gridAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadAndSetThumbnails () {
        StorageReference riversRef = storageReference.child("Preference Default Thumbnails/" + "nightlife_thumb.jpg");
        riversRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded data to local file
                //Toast.makeText(getActivity(), taskSnapshot.getStorage().getPath(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), taskSnapshot.getStorage().getName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), taskSnapshot.getStorage().getDownloadUrl().toString(), Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }

    public class GridAdapter extends BaseAdapter {

        public ArrayList<String> strings = new ArrayList<>();
        public ArrayList<String> thumbnailURLs = new ArrayList<>();

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int i) {
            return strings.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final TextView textView;
            ImageView imageView;

            if (view == null) {
                view = getLayoutInflater(bundle).inflate(R.layout.preference_home_item, viewGroup, false);
            }

            textView = (TextView) view.findViewById(R.id.textView15);
            imageView = (ImageView)view.findViewById(R.id.imageView10);

            textView.setText(strings.get(i));
            if (thumbnailURLs.size() > 0) {
                Picasso.with(getActivity()).load(thumbnailURLs.get(i)).into(imageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString(SELECTED_CATEGORY_STRING, textView.getText().toString());
                    feedsFragmentFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, feedsFragmentFragment).addToBackStack(null).commit();
                }
            });

            return view;
        }
    }

}

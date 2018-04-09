package com.application.codedarts.dspotalpha;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
    private int counter = 0;
    private Thread thumbnailLoaderThread;

    private FirebaseAuth firebaseAuthentication;
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

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        }
        else {
            Toast.makeText(getActivity(), "Null Action Bar", Toast.LENGTH_SHORT).show();
        }

        firebaseAuthentication = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuthentication.getCurrentUser();
        databaseReferencePrefs = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        getSelectedPreferences();
        //downloadAndSetThumbnails();

        return view;
    }

    private void getSelectedPreferences () {
        gridAdapter = new GridAdapter();
        counter = 0;
        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    gridAdapter.keys.add(childSnapshot.getKey());
                    gridAdapter.values.add(value);
                    gridAdapter.strings.add(value);
                    loadThumbnails(counter);
                    counter++;

                    if (gridAdapter.strings.size() > 0) {
                        constraintLayoutProgressBar.setVisibility(View.GONE);
                    }

                    /*thumbnailLoaderThread = new Thread() {
                        @Override
                        public void run () {
                            loadThumbnails();
                        }
                    };

                    thumbnailLoaderThread.start();*/
                    gridAdapter.notifyDataSetChanged();
                }
                gridView.setAdapter(gridAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadThumbnails (final int index) {
        //even if a preference is not selected,
        //the thumbnail attributed to it
        //gets set to the next selected preference.
        //So if i selected prefs 1, prefs 1 thumbnail is set to it
        //If I don't select prefs 2 but select prefs 3, prefs 2's thumbnail gets set to prefs 3.
        databaseReferencePrefs.child("Preference URL Thumbnails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    if (gridAdapter.keys.size() > 0) {
                        if (childSnapshot.getKey().equals(String.valueOf(gridAdapter.keys.get(index)))) {
                            String value = childSnapshot.getValue().toString();
                            gridAdapter.thumbnailURLs.add(value);
                        }
                    }
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
        public HashMap<String, String> preferences = new HashMap<>();

        public ArrayList<String> keys = new ArrayList<>();
        public ArrayList<String> values = new ArrayList<>();

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

                    if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(textView.getText().toString());
                    }
                    else {
                        Toast.makeText(getActivity(), "Null Action Bar", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return view;
        }
    }

}

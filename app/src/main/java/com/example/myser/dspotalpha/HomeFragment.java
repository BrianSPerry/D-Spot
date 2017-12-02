package com.example.myser.dspotalpha;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FeedsFragment feedsFragmentFragment = new FeedsFragment();
    private FragmentManager fragmentManager;

    private GridView gridView;
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
        fragmentManager = getActivity().getSupportFragmentManager();
        gridView = view.findViewById(R.id.gridView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Allow the ability to add preferences here...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
        downloadAndSetThumbnails();

        return view;
    }

    private void getSelectedPreferences () {
        databaseReferencePrefs.child("User Preferences/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gridAdapter = new GridAdapter();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String value = childSnapshot.getValue().toString();
                    gridAdapter.strings.add(value);
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
            TextView textView;
            ImageView imageView;

            if (view == null) {
                view = getLayoutInflater(bundle).inflate(R.layout.preference_home_item, viewGroup, false);
            }

            textView = (TextView) view.findViewById(R.id.textView15);
            imageView = (ImageView)view.findViewById(R.id.imageView10);

            textView.setText(strings.get(i));
            imageView.setImageResource(R.drawable.side_nav_bar);
            //imageView.setImageResource(R.drawable.ic_dashboard_black_24dp);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction().replace(R.id.linearLayoutContent, feedsFragmentFragment).commit();
                }
            });

            return view;
        }
    }

}

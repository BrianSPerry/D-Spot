package com.example.codedarts.dspotapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class feed extends AppCompatActivity {

    private String[] categoryNames = {
            "tempo",
            "Calypso",
            "Chutney Soca",
            "Pan",
            "Groovy"
    };

    private String[] categoryIDs = {
            "tempo",
            "calypso",
            "chutneySoca",
            "pan",
            "groovy"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSelectedCategories();
    }

    private void getSelectedCategories () {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        for (int i = 0; i < categoryNames.length; i++) {
            if (sharedPreferences.getString(categoryIDs[i], categoryNames[i]) != null) {
                Toast.makeText(this, categoryNames[i] + " seems to exist!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, categoryNames[i] + " DOES NOT EXIST!", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

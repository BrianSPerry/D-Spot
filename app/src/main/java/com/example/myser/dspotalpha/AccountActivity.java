package com.example.myser.dspotalpha;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private AccountFragment accountFragment = new AccountFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    public static FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentManager.beginTransaction().replace(R.id.constraintLayout, accountFragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.constraintLayout, accountFragment).addToBackStack(null).commit();

        firebaseAuthentication = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void saveInformation (View view) {
        accountFragment.saveInformation(view);
    }

    public void logOut (View view) {
        firebaseAuthentication.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

}

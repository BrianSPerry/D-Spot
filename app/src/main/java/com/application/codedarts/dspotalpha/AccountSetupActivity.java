package com.application.codedarts.dspotalpha;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AccountSetupActivity extends AppCompatActivity {

    private AccountFragment accountFragment;
    private PreferenceFragment preferenceFragment;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        accountFragment = new AccountFragment();
        preferenceFragment = new PreferenceFragment();
        homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout, accountFragment).commit();
    }

    public void saveInformation (View view) {
        accountFragment.saveInformation(view);
        fragmentManager.beginTransaction().replace(R.id.frameLayout, preferenceFragment).commit();
    }

    public void getPhotoFile (View view) {
        accountFragment.getPhotoFile(view);
    }

}

package com.example.myser.dspotalpha;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccountSetupActivity extends AppCompatActivity {

    private int fragmentIndex = 0;
    private Button button;
    private AccountFragment accountFragment;
    private HomeFragment homeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);

        accountFragment = new AccountFragment();
        homeFragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();

        button = (Button)findViewById(R.id.nextButton);
    }

    public void openFragment (View view) {
        fragmentIndex++;
        fragmentManager.beginTransaction().replace(R.id.frameLayout, accountFragment).commit();
        disableButtons();
    }

    private void disableButtons() {
        accountFragment.toggleView(accountFragment.saveButton, false);
        accountFragment.toggleView(accountFragment.logOutButton, false);
    }

    public void continueCreatingProfile () {

    }

}

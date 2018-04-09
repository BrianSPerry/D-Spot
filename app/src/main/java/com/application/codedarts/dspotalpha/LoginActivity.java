package com.application.codedarts.dspotalpha;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextView emailTextViewLogin, passwordTextViewLogin, emailTextViewSignUp, passwordTextViewSignUp, loadingText;
    private LinearLayout loginLayout, signUpLayout;
    private ConstraintLayout progressViewGridLayout;
    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = (LinearLayout)findViewById(R.id.loginLayout);
        signUpLayout = (LinearLayout)findViewById(R.id.signupLayout);
        emailTextViewLogin = (EditText)findViewById(R.id.emailEditTextLogin);
        passwordTextViewLogin = (EditText)findViewById(R.id.passwordEditTextLogin);
        emailTextViewSignUp = (EditText)findViewById(R.id.emailEditTextSignUp);
        passwordTextViewSignUp = (EditText)findViewById(R.id.passwordEditTextSignUp);
        loadingText = (TextView)findViewById(R.id.loadingText);
        progressViewGridLayout = (ConstraintLayout)findViewById(R.id.progressViewCustom);
        firebaseAuthentication = FirebaseAuth.getInstance();

        if (firebaseAuthentication.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity01.class);

            startActivity(intent);
        }
    }

    private void registerUserViaEmail(String email, String password, final String className) {
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter an email address...", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter a password...", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Please enter an email address and password...", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        progressViewGridLayout.setVisibility(View.VISIBLE);
        loadingText.setText("Creating account...");
        firebaseAuthentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressViewGridLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Successfully created your account!", Toast.LENGTH_SHORT).show();
                    //region Fancy Code For Starting Next Activity
                    /*try {
                        startActivity(new Intent(LoginActivity.this, Class.forName(className).getClass()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    //endregion
                    startActivity(new Intent(LoginActivity.this, AccountSetupActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this, "Registration failed. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginViaEmail (String email, String password) {
        if (TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter an email address...", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter a password...", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Please enter an email address and password...", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        progressViewGridLayout.setVisibility(View.VISIBLE);
        loadingText.setText("Signing In...");
        firebaseAuthentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressViewGridLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Successfully signed in!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity01.class);
                    //Intent intent = new Intent(LoginActivity.this, AccountSetupActivity.class);

                    startActivity(intent);
                }
            }
        });
    }

    public void login (View view) {
        loginViaEmail(emailTextViewLogin.getText().toString().trim(), passwordTextViewLogin.getText().toString().trim());
    }

    public void register (View view) {
        registerUserViaEmail(emailTextViewSignUp.getText().toString().trim(), passwordTextViewSignUp.getText().toString().trim(), "PreferencesActivity");
    }

    public void goToRegister (View view) {
        loginLayout.setVisibility(View.GONE);
        signUpLayout.setVisibility(View.VISIBLE);
    }

    public void goToLogin (View view) {
        signUpLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
    }

}

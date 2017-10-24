package com.example.codedarts.dspotapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public CheckBox check1, check2, check3,check4,check5;

    private Button selected;
    private static int checkedCechkboxIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        check1 = (CheckBox) findViewById(R.id.tempo);
        check2 = (CheckBox) findViewById(R.id.calypso);
        check3 = (CheckBox) findViewById(R.id.chutneySoca);
        check4 = (CheckBox) findViewById(R.id.pan);
        check5 = (CheckBox) findViewById(R.id.groovy);
        selected = (Button) findViewById(R.id.buttn_slct);

    }

    public void methodListener(View view){
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final CheckBox[] checkArray = {check1, check2, check3, check4, check5};

        for (int i = 0; i < checkArray.length; i++) {
            if (checkArray[i].isChecked()) {
                editor.putString(String.valueOf(checkArray[i].getId()), String.valueOf(checkArray[i].getText()));
            }
            else {
                checkedCechkboxIndex++;
            }
        }

        if (checkedCechkboxIndex >= checkArray.length) {
            Toast.makeText(this, "You haveen't selected any categorwrgg", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.commit();
            selectedCheckbox();
        }
    }

    private void selectedCheckbox(){
        Intent intent = new Intent(this, feed.class);
        startActivity(intent);
    }


}

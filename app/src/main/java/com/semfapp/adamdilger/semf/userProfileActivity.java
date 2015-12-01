package com.semfapp.adamdilger.semf;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class userProfileActivity extends AppCompatActivity {

    EditText name, contactNum, email, supName, supEmail, ccHazard, ccIncedent, ccProtect, ccTake5;
    Button saveSettingButton, clearSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.perm_identity_white);
        toolbar.setTitle("User Profile Settings");
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPreferences = getSharedPreferences("userProfileData", MODE_PRIVATE);


        //Create EditText fields
        saveSettingButton = (Button)findViewById(R.id.up_button_save);
        clearSettingsButton = (Button)findViewById(R.id.up_button_clear);
        name = (EditText)findViewById(R.id.up_text_name);
        contactNum = (EditText)findViewById(R.id.up_text_contact_num);
        email = (EditText)findViewById(R.id.up_text_email);
        supName = (EditText)findViewById(R.id.up_text_sup_name);
        supEmail = (EditText)findViewById(R.id.up_text_sup_email);
        ccHazard = (EditText)findViewById(R.id.up_text_cc_hazard);
        ccIncedent = (EditText)findViewById(R.id.up_text_cc_incedent);
        ccProtect = (EditText)findViewById(R.id.up_text_cc_protect);
        ccTake5 = (EditText)findViewById(R.id.up_text_cc_take5);

        //set Text to values in SharedPreferences
        name.setText(sharedPreferences.getString("name", ""));
        contactNum.setText(sharedPreferences.getString("contactNum",""));
        email.setText(sharedPreferences.getString("email",""));
        supName.setText(sharedPreferences.getString("supName",""));
        supEmail.setText(sharedPreferences.getString("supEmail",""));
        ccHazard.setText(sharedPreferences.getString("ccHazard",""));
        ccIncedent.setText(sharedPreferences.getString("ccIncedent",""));
        ccProtect.setText(sharedPreferences.getString("ccProtect",""));
        ccTake5.setText(sharedPreferences.getString("ccTake5", ""));


        //Button Saves new values to SharedPreferences
        saveSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("name", name.getText().toString());
                editor.putString("contactNum", contactNum.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("supName", supName.getText().toString());
                editor.putString("supEmail", supEmail.getText().toString());
                editor.putString("ccHazard", ccHazard.getText().toString());
                editor.putString("ccIncedent", ccIncedent.getText().toString());
                editor.putString("ccProtect", ccProtect.getText().toString());
                editor.putString("ccTake5", ccTake5.getText().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "User Settings Saved", Toast.LENGTH_SHORT).show();

                closeActivity();

            }
        });
        clearSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("name");
                editor.remove("contactNum");
                editor.remove("email");
                editor.remove("supName");
                editor.remove("supEmail");
                editor.remove("ccHazard");
                editor.remove("ccIncedent");
                editor.remove("ccProtect");
                editor.remove("ccTake5");

                editor.commit();

                name.setText("");
                contactNum.setText("");
                email.setText("");
                supName.setText("");
                supEmail.setText("");
                ccHazard.setText("");
                ccIncedent.setText("");
                ccProtect.setText("");
                ccTake5.setText("");

                Toast.makeText(getApplicationContext(), "User Settings Cleared", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void closeActivity() {
        this.finish();
    }


}

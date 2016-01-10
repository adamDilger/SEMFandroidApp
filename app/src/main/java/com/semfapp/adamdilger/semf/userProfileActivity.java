/*

Copyright (C) 2015  Adam Dilger

This program is free software: you can redistribute it and/or modify it under the terms of the GNU
    Affero General Public License as published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version. 

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
    without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.
    If not, see <http://www.gnu.org/licenses/>.

 */

package com.semfapp.adamdilger.semf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class userProfileActivity extends AppCompatActivity {

    public static String USER_SETTINGS_SHARED_PREF = "userProfileData";
    public static String NAME = "name";
    public static String CONTACT_NUM = "contactNum";
    public static String EMAIL = "email";
    public static String SUPERVISOR_NAME = "supName";
    public static String SUPERVISOR_EMAIL = "supEmail";
    public static String CC_HAZARD = "ccHazard";
    public static String CC_INCEDENT = "ccIncedent";
    public static String CC_PROTECT = "ccProtect";
    public static String CC_TAKE5 = "ccTake5";



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

        final SharedPreferences sharedPreferences = getSharedPreferences(USER_SETTINGS_SHARED_PREF, MODE_PRIVATE);


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
        name.setText(sharedPreferences.getString(NAME, ""));
        contactNum.setText(sharedPreferences.getString(CONTACT_NUM,""));
        email.setText(sharedPreferences.getString(EMAIL,""));
        supName.setText(sharedPreferences.getString(SUPERVISOR_NAME,""));
        supEmail.setText(sharedPreferences.getString(SUPERVISOR_EMAIL,""));
        ccHazard.setText(sharedPreferences.getString(CC_HAZARD,""));
        ccIncedent.setText(sharedPreferences.getString(CC_INCEDENT,""));
        ccProtect.setText(sharedPreferences.getString(CC_PROTECT,""));
        ccTake5.setText(sharedPreferences.getString(CC_TAKE5, ""));


        //Button Saves new values to SharedPreferences
        saveSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(NAME, name.getText().toString());
                editor.putString(CONTACT_NUM, contactNum.getText().toString());
                editor.putString(EMAIL, email.getText().toString());
                editor.putString(SUPERVISOR_NAME, supName.getText().toString());
                editor.putString(SUPERVISOR_EMAIL, supEmail.getText().toString());
                editor.putString(CC_HAZARD, ccHazard.getText().toString());
                editor.putString(CC_INCEDENT, ccIncedent.getText().toString());
                editor.putString(CC_PROTECT, ccProtect.getText().toString());
                editor.putString(CC_TAKE5, ccTake5.getText().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "User Settings Saved", Toast.LENGTH_SHORT).show();

                closeActivity();

            }
        });
        clearSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(userProfileActivity.this);
                builder.setMessage("Are you sure you want to clear all user data?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.remove(NAME);
                        editor.remove(CONTACT_NUM);
                        editor.remove(EMAIL);
                        editor.remove(SUPERVISOR_NAME);
                        editor.remove(SUPERVISOR_EMAIL);
                        editor.remove(CC_HAZARD);
                        editor.remove(CC_INCEDENT);
                        editor.remove(CC_PROTECT);
                        editor.remove(CC_TAKE5);

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

                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "User Settings Cleared", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void closeActivity() {
        this.finish();
    }


}

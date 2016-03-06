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
    public static String CC_HAZARD = "ccHazard";
    public static String CC_INCEDENT = "ccIncedent";
    public static String CC_PROTECT = "ccProtect";
    public static String CC_TAKE5 = "ccTake5";



    EditText name, contactNum, email, ccHazard, ccTake5;
    Button saveSettingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.perm_identity_white);
        toolbar.setTitle("User Profile Settings");
        setSupportActionBar(toolbar);

        final SharedPreferences sharedPreferences = getSharedPreferences(USER_SETTINGS_SHARED_PREF, MODE_PRIVATE);

        System.out.println(sharedPreferences.getString(CC_HAZARD, "Def"));
        System.out.println(sharedPreferences.getString(CC_TAKE5, "Def"));

        //Create EditText fields
        saveSettingButton = (Button)findViewById(R.id.up_button_save);
        name = (EditText)findViewById(R.id.up_text_name);
        contactNum = (EditText)findViewById(R.id.up_text_contact_num);
        email = (EditText)findViewById(R.id.up_text_email);
        ccHazard = (EditText)findViewById(R.id.up_text_cc_hazard);
        ccTake5 = (EditText)findViewById(R.id.up_text_cc_take5);

        //set Text to values in SharedPreferences
        name.setText(sharedPreferences.getString(NAME, ""));
        contactNum.setText(sharedPreferences.getString(CONTACT_NUM,""));
        email.setText(sharedPreferences.getString(EMAIL,""));
        ccHazard.setText(sharedPreferences.getString(CC_HAZARD,""));
        ccTake5.setText(sharedPreferences.getString(CC_TAKE5, ""));

        //set disabled textFields
        ccHazard.setEnabled(false);
        ccTake5.setEnabled(false);

        //Button Saves new values to SharedPreferences
        saveSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(NAME, name.getText().toString());
                editor.putString(CONTACT_NUM, contactNum.getText().toString());
                editor.putString(EMAIL, email.getText().toString());
                editor.putString(CC_HAZARD, ccHazard.getText().toString());
                editor.putString(CC_TAKE5, ccTake5.getText().toString());

                editor.commit();

                Toast.makeText(getApplicationContext(), "User Settings Saved", Toast.LENGTH_SHORT).show();

                closeActivity();
            }
        });
    }

    private void closeActivity() {
        this.finish();
    }


}

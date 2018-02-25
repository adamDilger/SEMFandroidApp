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

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final int PROTECTPLAN = 0;
    private final int HAZARDID = 1;
    private final int NONCONFORMANCE = 2;
    private final int TAKE5 = 3;
    private final int SITEINSTRUCTION = 4;
    private final int INCEDENT = 5;

    private static final int MY_PERMISSIONS_REQUEST = 123;

    TextView hazardIdText, take5Text, siteInstructionText, incedentReport;
    public static Date currentDate;
    public static Pdf pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.semf_logo_icon);
        setSupportActionBar(toolbar);

        //Set Default email values, these will remain unchanged
        SharedPreferences sharedPreferences = getSharedPreferences(userProfileActivity.USER_SETTINGS_SHARED_PREF, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userProfileActivity.CC_HAZARD, "hazard@semf.com.au");
        editor.putString(userProfileActivity.CC_TAKE5, "take5@semf.com.au");
        editor.commit();

        //If First Open, open Tutorial Dialog
        sharedPreferences = getSharedPreferences(TutorialDialog.TUTORIAL_SHARED_PREF_CODE, MODE_PRIVATE);

        if (sharedPreferences.getBoolean(TutorialDialog.TUTORIAL_BOOLEAN_CODE, false) == false) {
            TutorialDialog tutorialDialog = new TutorialDialog();

            tutorialDialog.show(getSupportFragmentManager(), "tutTag");

            sharedPreferences.edit().putBoolean(TutorialDialog.TUTORIAL_BOOLEAN_CODE, true).commit();
        }

        //CHECK FOR STORAGE AND CAMERA PERMISSIONS
        checkPermissionsGranted();

        //create date object
        currentDate = new Date();
        pdf = new Pdf();

        //copyright text setup. Add version number to end of string
        TextView copyrightText = (TextView) findViewById(R.id.copyright_text);
        String versionName = "";


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        String output = String.format("\u00a9 2017 Adam Dilger (V%s)", versionName);
        copyrightText.setText(output);


//        protectPlanText = (TextView)findViewById(R.id.home_button_protect);
//        protectPlanText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkForUserData(PROTECTPLAN)) {
//                    Intent i = new Intent(getApplicationContext(), protectPlanActivity.class);
//                    startActivity(i);
//                }
//            }
//        });

        hazardIdText = (TextView)findViewById(R.id.home_button_hazard);
        hazardIdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkForUserData(HAZARDID)) {
                    Intent i = new Intent(getApplicationContext(), hazardIdActivity.class);
                    startActivity(i);
                }

            }
        });

        take5Text = (TextView)findViewById(R.id.home_button_take5);
        take5Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkForUserData(TAKE5)) {
                    Intent i = new Intent(getApplicationContext(), Take5Activity.class);
                    startActivity(i);
                }
            }
        });
        siteInstructionText = (TextView)findViewById(R.id.home_button_site_instruct);
        siteInstructionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkForUserData(SITEINSTRUCTION)) {
                    Intent i = new Intent(getApplicationContext(), SiteInstructionActivity.class);
                    startActivity(i);
                }
            }
        });
        incedentReport = (TextView)findViewById(R.id.home_button_incident);
        incedentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), IncidentReportActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_user_profile) {
            Intent i = new Intent(getApplicationContext(), userProfileActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.menu_more_info) {
            DialogFragment dialog = new MoreInfoDialog();
            dialog.show(getSupportFragmentManager(), "tag");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        HazardIdData.getInstance().exitDelete();
        SiteInstructionData.getInstance().exitDelete();
    }

    public static class MoreInfoDialog extends DialogFragment
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {

            View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_more_info, null);

            return new AlertDialog.Builder(getActivity())
                    .setView(v)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().dismiss();
                        }
                    })
                    .create();
        }
    }

    //checks if CC or supervisor email for form is null. if so, shows Toast
    private boolean checkForUserData(int code) {
        String output = "";
        boolean failed = false;

        SharedPreferences sharedPreferences = getSharedPreferences(userProfileActivity.USER_SETTINGS_SHARED_PREF, MODE_PRIVATE);


        switch (code) {
            case PROTECTPLAN:
                if (sharedPreferences.getString(userProfileActivity.CC_PROTECT, "") == "" ||
                        sharedPreferences.getString(userProfileActivity.CC_PROTECT, null) == null) {
                    failed = true;
                    output = "Protect Plan";
                }
                break;
            case HAZARDID:
                if (sharedPreferences.getString(userProfileActivity.CC_HAZARD, "") == "" ||
                        sharedPreferences.getString(userProfileActivity.CC_HAZARD, null) == null) {
                    failed = true;
                    output = "Hazard ID";
                }
                break;
            case TAKE5:
                if (sharedPreferences.getString(userProfileActivity.CC_TAKE5, "") == "" ||
                        sharedPreferences.getString(userProfileActivity.CC_TAKE5, null) == null) {
                    failed = true;
                    output = "Take 5";
                }
                break;
            case INCEDENT:
                if (sharedPreferences.getString(userProfileActivity.CC_INCEDENT, "") == "" ||
                        sharedPreferences.getString(userProfileActivity.CC_INCEDENT, null) == null) {
                    failed = true;
                    output = "Incedent Report";
                }
                break;
        }

        //if output has changed, one of the values is null
        if (output != "") {
            output = String.format("Please update UserData. \nCC email needed for: %s", output);
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        }

        return failed;
    }

    private void checkPermissionsGranted() {

        //if any of the permissions fail, request permissions
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                ) {

            //request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Snackbar.make(hazardIdText, "Permissions Granted!", Snackbar.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setMessage("Permissions are required for this application to run...")
                            .setTitle("Application unable to run")
                            .setPositiveButton("Grant Permissions", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkPermissionsGranted();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void deleteAllTempFiles() {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }
}
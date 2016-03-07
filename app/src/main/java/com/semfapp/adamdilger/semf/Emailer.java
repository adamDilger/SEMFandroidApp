package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adamdilger on 7/01/2016.
 */
public class Emailer {

    public static int EMAILER_REQUEST_CODE = 654;

    public static int PROTECT_PLAN_CODE = 0;
    public static int TAKE_5_CODE = 1;
    public static int HAZARD_ID_CODE = 2;
    public static int INCEDENT_CODE = 3;
    public static int SITE_INSTRUCTION_CODE = 4;
    public static int NON_CONFORMANCE_CODE = 5;

    Context context;

    public Emailer(Context context) {
        this.context = context;
    }

    public static String getSubject(int code, @Nullable String jobNumber) {
        String date = new SimpleDateFormat("yyyyMMdd").format(MainActivity.currentDate);
        String jobType = "";

        switch (code) {
            case 0:
                jobType = "PROTECT PLAN";
                break;
            case 1:
                jobType = "TAKE5";
                break;
            case 2:
                jobType = "HAZARD ID";
                break;
            case 4:
                jobType = "SITE INSTRUCTION";
                break;
        }

        if (jobNumber != null && jobNumber != "") {
            return String.format("%s - %s - %s", date, jobType, jobNumber);
        } else {
            return String.format("%s - %s", date, jobType);
        }
    }

    /*
        Code
        0 : Protect Plan
        1 : Take 5
        2 : Hazard Id
        3 : Incedent
        4 : Site Instruction
        5 : Non Conformance

        recipientEmail is for forms that require email to be sent to unique recipeint
     */
    public Intent emailAttatchmentIntent(int code, File pdfAttatchment, @Nullable String recipientEmail, String subject) {

        Uri uri = Uri.fromFile(pdfAttatchment);

        String userEmail;
        String ccEmail = "";

        SharedPreferences sharedPreferences = context.getSharedPreferences("userProfileData", context.MODE_PRIVATE);

        //set supervisor/user email
        userEmail = sharedPreferences.getString(userProfileActivity.EMAIL, "default");

        switch (code) {
            case 0:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_PROTECT, "default");
                break;
            case 1:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_TAKE5, "default");
                break;
            case 2:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_HAZARD, "default");
                break;
            case 3:
                //No emailing feature for incident procedure
                break;
            case 4:
                break;
        }

        //create recipients array, adding in recipientEmail if != null
        String[] recipients;
        if (recipientEmail != null) {
            recipients = new String[]{userEmail, recipientEmail};
        } else {
            recipients = new String[]{userEmail};
        }

        /* TO VIEW AS PDF
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        return intent;
        */

        if (uri != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            emailIntent.setType("text/plain");
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
            emailIntent.putExtra(Intent.EXTRA_CC, new String[] {ccEmail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            return emailIntent;
        } else {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}

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
    public Intent emailAttatchmentIntent(int code, File pdfAttatchment, @Nullable String recipientEmail) {

        Uri uri = Uri.fromFile(pdfAttatchment);

        String userEmail = "";
        String supervisorEmail = "";
        String ccEmail = "";
        String subject = "";

        SharedPreferences sharedPreferences = context.getSharedPreferences("userProfileData", context.MODE_PRIVATE);

        //set supervisor/user email
        supervisorEmail = sharedPreferences.getString(userProfileActivity.SUPERVISOR_EMAIL, "default");
        userEmail = sharedPreferences.getString(userProfileActivity.EMAIL, "default");

        switch (code) {
            case 0:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_PROTECT, "default");
                subject = "Protect Plan";
                break;
            case 1:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_TAKE5, "default");
                subject = "Take 5";
                break;
            case 2:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_HAZARD, "default");
                subject = "Hazard ID";
                break;
            case 3:
                ccEmail = sharedPreferences.getString(userProfileActivity.CC_INCEDENT, "default");
                subject = "Incedent Report";
                break;
            case 4:
                subject = "Site Instruction";
                break;
            case 5:
                subject = "Non Conformance";
                break;
        }


        //if there is a recipient email, change supervisor email to recipient
        if (recipientEmail != null) {
            supervisorEmail = recipientEmail;
        }

        //Date for subject
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        subject = subject + " - " + sdf.format(MainActivity.currentDate);


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
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{supervisorEmail, userEmail});
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

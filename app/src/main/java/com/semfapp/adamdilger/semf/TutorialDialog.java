package com.semfapp.adamdilger.semf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by adamdilger on 8/01/2016.
 */
public class TutorialDialog extends DialogFragment {
    public static String TUTORIAL_SHARED_PREF_CODE = "tutorialCode";
    public static String TUTORIAL_BOOLEAN_CODE = "tutorialBooleanCode";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Welcome!");

        View v = getActivity().getLayoutInflater().inflate(R.layout.tutorial, null);
        builder.setView(v);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}

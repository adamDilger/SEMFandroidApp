package com.semfapp.adamdilger.semf;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by adamdilger on 3/03/16.
 */
public class Take5RiskMatrixDialog extends DialogFragment {

//    public static Take5RiskMatrixDialog newInstance() {
//        Take5RiskMatrixDialog fragment = new Take5RiskMatrixDialog();
//        return fragment;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.take5_risk_matrix_dialog, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Risk Matrix")
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create();
    }
}


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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5AddElementDialog extends DialogFragment {

    static final String ONE_EXTRA = "ONE_EXTRA";
    static final String RATING_EXTRA = "RATING_EXTRA";
    static final String TWO_EXTRA = "TWO_EXTRA";
    String mOne, mTwo;
    String mRating = "NA";
    private EditText t1, t3;
    private RadioGroup radioGroup;
    private Button riskMatrixButton;

    public static Take5AddElementDialog newInstance() {
        Take5AddElementDialog fragment = new Take5AddElementDialog();
        return fragment;
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(ONE_EXTRA, mOne);
        i.putExtra(RATING_EXTRA, mRating);
        i.putExtra(TWO_EXTRA, mTwo);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.take5_edit_element_dialog, null);

        t1 = (EditText) v.findViewById(R.id.element_text_one);
        t1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOne = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioLow:
                        mRating = Take5RiskElement.Rating.LOW.toString();
                        break;
                    case R.id.radioMed:
                        mRating = Take5RiskElement.Rating.MED.toString();
                        break;
                    case R.id.radioHigh:
                        mRating = Take5RiskElement.Rating.HIGH.toString();
                        break;
                }
            }
        });
        t3 = (EditText) v.findViewById(R.id.element_text_two);
        t3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTwo = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        riskMatrixButton = (Button)v.findViewById(R.id.risk_matrix_button);
        riskMatrixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Take5RiskMatrixDialog dialog = new Take5RiskMatrixDialog();

                dialog.show(getActivity().getFragmentManager(), "tag");
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Add New Item")
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .setNegativeButton(
                        android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onViewCreated(view, savedInstanceState);
    }
}

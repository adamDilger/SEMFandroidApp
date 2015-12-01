package com.semfapp.adamdilger.semf;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5EditElementDialog extends DialogFragment {
    static final String POS_EXTRA = "POS EXTRA";
    static final String ONE_EXTRA = "ONE_EXTRA";
    static final String RATING_EXTRA = "RATING_EXTRA";
    static final String TWO_EXTRA = "TWO_EXTRA";
    int mPos;
    String mOne, mTwo, mRating;
    private EditText t1, t3;
    private RadioGroup radioGroup;

    public static Take5EditElementDialog newInstance(int pos, String one, String two, String rating) {
        Bundle args = new Bundle();
        args.putInt(POS_EXTRA, pos);
        args.putString(ONE_EXTRA, one);
        args.putString(RATING_EXTRA, rating);
        args.putString(TWO_EXTRA, two);
        Take5EditElementDialog fragment = new Take5EditElementDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode) {

        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(POS_EXTRA, mPos);
        i.putExtra(ONE_EXTRA, mOne);
        i.putExtra(RATING_EXTRA, mRating);
        i.putExtra(TWO_EXTRA, mTwo);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    private void sendDelete(int resultCode) {

        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(POS_EXTRA, mPos);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mPos = getArguments().getInt(POS_EXTRA);
        mOne = getArguments().getString(ONE_EXTRA);
        mRating = getArguments().getString(RATING_EXTRA);
        mTwo = getArguments().getString(TWO_EXTRA);

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
        if (mOne != null) {
            t1.setText(mOne);
        }
        if (mRating != null) {
            Take5RiskElement.Rating rating = Take5RiskElement.Rating.valueOf(mRating);
            switch (rating) {
                case LOW:
                    mRating = Take5RiskElement.Rating.LOW.toString();
                    break;
                case MED:
                    mRating = Take5RiskElement.Rating.MED.toString();
                    break;
                case HIGH:
                    mRating = Take5RiskElement.Rating.HIGH.toString();
            }
        }
        if (mTwo != null) {
            t3.setText(mTwo);
        }


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Edit Item")
                .setPositiveButton(
                        android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .setNeutralButton(
                        "DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendDelete(Activity.RESULT_FIRST_USER);
                            }
                        })
                .create();
    }
}

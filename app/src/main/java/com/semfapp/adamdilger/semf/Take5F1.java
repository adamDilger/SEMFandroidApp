package com.semfapp.adamdilger.semf;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Adam on 12/07/2015.
 */
public class Take5F1 extends Fragment {

    EditText[] mEditTexts;
    String[] mEditTextValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditTextValues = Take5Data.get(getActivity()).getEditTexts();
        mEditTexts = new EditText[4];
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_take5_f1, parent, false);

        mEditTexts[0] = (EditText) v.findViewById(R.id.jobReferenceText);
        mEditTexts[1] = (EditText) v.findViewById(R.id.locationText);
        mEditTexts[2] = (EditText) v.findViewById(R.id.taskText);
        mEditTexts[3] = (EditText) v.findViewById(R.id.namesText);


        for (int x = 0; x < mEditTexts.length; x++) {

            final int index = x;
            mEditTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mEditTextValues[index] = s.toString();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return v;
    }
}

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

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
            mEditTexts[x].setText(mEditTextValues[x]);

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

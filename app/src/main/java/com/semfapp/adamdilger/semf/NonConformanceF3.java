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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NonConformanceF3 extends Fragment {

    NonConformanceData data;
    EditText actions;
    TextView textView;
    Button saveButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = NonConformanceData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_save, container, false);

        actions = (EditText)v.findViewById(R.id.question_save_editText);
        saveButton = (Button)v.findViewById(R.id.question_save_button);
        textView = (TextView)v.findViewById(R.id.fragment_question_textview);
        textView.setText(R.string.non_conformance_actions);

        actions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setActions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NonConformanceActivity)getActivity()).createPdf();
            }
        });

        return v;
    }
}
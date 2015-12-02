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
import android.widget.LinearLayout;
import android.widget.RadioButton;

/**
 * Created by adamdilger on 24/11/2015.
 */
public class SiteInstructionF1 extends Fragment {

    SiteInstructionData data;
    RadioButton radioYes, radioNo;
    LinearLayout jobNumberLayout;
    EditText jobNumber, site, siteLocation, recipient, recipientEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = SiteInstructionData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_site_instruction_f1, container, false);

        radioYes = (RadioButton) v.findViewById(R.id.site_instruction_radio_yes);
        radioNo = (RadioButton) v.findViewById(R.id.site_instruction_radio_no);
        jobNumberLayout = (LinearLayout) v.findViewById(R.id.job_number_layout);
        jobNumber = (EditText)v.findViewById(R.id.site_instruction_job_number_editText);
        site = (EditText)v.findViewById(R.id.site_instruction_site_editText);
        siteLocation = (EditText)v.findViewById(R.id.site_instruction_site_location_editText);
        recipient = (EditText)v.findViewById(R.id.site_instruction_recipient_editText);
        recipientEmail = (EditText)v.findViewById(R.id.site_instruction_recipient_email_editText);

        radioYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobNumberLayout.setVisibility(View.VISIBLE);
                radioNo.setChecked(false);
                radioYes.setChecked(true);
                jobNumber.requestFocus();
            }
        });
        radioNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobNumberLayout.setVisibility(View.GONE);
                radioNo.setChecked(true);
                radioYes.setChecked(false);
            }
        });

        jobNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setJobNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        site.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setSite(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        siteLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setLocation(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recipient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setRecipient(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recipientEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setRecipientEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return v;
    }
}

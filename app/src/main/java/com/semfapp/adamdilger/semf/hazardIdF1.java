package com.semfapp.adamdilger.semf;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class hazardIdF1 extends Fragment {

    HazardIdData data;
    RadioButton radioYes, radioNo;
    LinearLayout jobNumberLayout;
    EditText jobNumberEditText, siteLocationEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = HazardIdData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hazard_id_f1, container, false);

        radioYes = (RadioButton)v.findViewById(R.id.hazard_id_radio_yes);
        radioNo = (RadioButton)v.findViewById(R.id.hazard_id_radio_no);
        jobNumberLayout = (LinearLayout)v.findViewById(R.id.job_number_layout);

        radioYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobNumberLayout.setVisibility(View.VISIBLE);
                radioNo.setChecked(false);
                radioYes.setChecked(true);
                jobNumberEditText.requestFocus();
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

        jobNumberEditText = (EditText)v.findViewById(R.id.hazard_id_job_number_editText);
        jobNumberEditText.setText(data.getProjectNumber());
        siteLocationEditText = (EditText)v.findViewById(R.id.hazard_id_location_editText);
        siteLocationEditText.setText(data.getLocation());

        jobNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setProjectNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        siteLocationEditText.addTextChangedListener(new TextWatcher() {
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

        return v;
    }
}

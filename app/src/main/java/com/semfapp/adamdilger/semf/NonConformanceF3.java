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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
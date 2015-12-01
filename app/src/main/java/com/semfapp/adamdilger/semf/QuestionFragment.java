package com.semfapp.adamdilger.semf;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by adamdilger on 19/11/2015.
 */
public class QuestionFragment extends Fragment{

    public static String QUESTION_TEXT_CODE = "questionfragmenttextcode";

    private int textId;
    private TextView textView;
    private EditText editText;
    Communicator comm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comm = (Communicator)getActivity();

        textId = getArguments().getInt(QUESTION_TEXT_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        textView = (TextView)v.findViewById(R.id.fragment_question_textview);
        editText = (EditText)v.findViewById(R.id.fragment_question_editText);

        textView.setText(textId);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                comm.saveText(s.toString(), textId);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
}

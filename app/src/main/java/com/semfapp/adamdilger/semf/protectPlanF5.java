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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class protectPlanF5 extends Fragment {

    private EditText f5EditText;
    private Button saveButton;
    private TextView textView;
    private protectPlanData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = protectPlanData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_save, container, false);

        textView = (TextView)v.findViewById(R.id.fragment_question_textview);
        f5EditText = (EditText)v.findViewById(R.id.question_save_editText);
        saveButton = (Button)v.findViewById(R.id.question_save_button);

        textView.setText(R.string.protect_plan_q5);
        f5EditText.setText(data.getRamifications());
        f5EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setRamifications(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((protectPlanActivity)getActivity()).createPdf();

//                try {
//                    Document document = Jsoup.parse(getActivity().getAssets().open("protectPlan.html"), "utf-8", "http://www.example.com");
//
//                    Element body = document.body();
//
//                    Elements lists = body.select(".list_box");          //Lists html Elements
//                    ArrayList<String[]> arrayList = data.getArray();    //editText string arrays
//
//                    //for each Element in lists, add each bullet from arrayList.string[] as a <p>
//                    for (int x = 0; x < lists.size(); x++) {
//                        String f = "";
//
//                        for (String bullet : arrayList.get(x)) {
//                            f += "<p>" + bullet + "</p>";
//                        }
//
//                        lists.get(x).html(f);
//                    }
//
//                    ((protectPlanActivity)getActivity()).createPdf(document.html(), "Protect Plan");
//                } catch (Exception e) {
//
//                }
            }
        });

        return v;
    }
}

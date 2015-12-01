package com.semfapp.adamdilger.semf;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.semfapp.adamdilger.semf.Take5Data.CheckBoxData;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5F5 extends Fragment {

    private ArrayList<CheckBoxData> mCheckBoxs;
    private ListView mListView;
    private MyListAdapter mListAdapter;
    Button savePdfButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckBoxs = Take5Data.get(getActivity()).getSectionThreeCheckBoxs();
        mListAdapter = new MyListAdapter(mCheckBoxs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_take5_save_list, parent, false);

        mListView = (ListView) v.findViewById(R.id.empty_list_view);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBoxData a = (CheckBoxData) parent.getAdapter().getItem(position);
                a.checked();
                mListAdapter.notifyDataSetChanged();
            }
        });

        savePdfButton = (Button)v.findViewById(R.id.take5_save_pdf_button);
        savePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Take5Activity)getActivity()).createPdf();
            }
        });

        return v;
    }

    public class MyListAdapter extends ArrayAdapter<CheckBoxData> {
        public MyListAdapter(ArrayList<CheckBoxData> elements) { super(getActivity(), 0, elements);}
        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        //reuse fragmentTwo listlayout
                        .inflate(R.layout.take5_f2_list_item, null);
            }

            // Configure the view for this Crime
            final CheckBoxData c = getItem(position);
            TextView textOne = (TextView)convertView.findViewById(R.id.fragment_two_list_heading);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.fragment_two_list_check);

            textOne.setText(c.getString());
            checkBox.setChecked(c.isYes());
            return convertView;
        }
    }
}

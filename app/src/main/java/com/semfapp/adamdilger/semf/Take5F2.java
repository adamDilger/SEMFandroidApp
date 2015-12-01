package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import com.semfapp.adamdilger.semf.Take5Data.CheckBoxData;

import java.util.ArrayList;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5F2 extends Fragment{

    private ArrayList<CheckBoxData> mCheckBoxs;

    private ListView mListView;
    private MyListAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckBoxs = Take5Data.get(getActivity()).getSectionOneCheckBoxs();

        mListAdapter = new MyListAdapter(mCheckBoxs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_take5_empty_list, parent, false);

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

        return v;
    }

    public class MyListAdapter extends ArrayAdapter<CheckBoxData> {
        public MyListAdapter(ArrayList<CheckBoxData> elements) { super(getActivity(), 0, elements);}
        @Override
        public View getView (int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.take5_f2_list_item, null);
            }

            // Configure the view for this Crime
            final CheckBoxData c = getItem(position);
            TextView textOne =
                    (TextView)convertView.findViewById(R.id.fragment_two_list_heading);
            textOne.setText(c.getString());
            CheckBox checkBox = (CheckBox)
                    convertView.findViewById(R.id.fragment_two_list_check);
            checkBox.setChecked(c.isYes());
            return convertView;
        }
    }
}

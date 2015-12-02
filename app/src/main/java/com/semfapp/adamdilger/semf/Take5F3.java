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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Take5F3 extends Fragment {

    ArrayList<CheckBoxData> mCheckBoxs;
    ListView mListView;
    MyListAdapter mListAdapter;
    String[] subHeadings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckBoxs = Take5Data.get(getActivity()).getSectionTwoCheckBoxs();
        subHeadings = getResources().getStringArray(R.array.examples);
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
                        .inflate(R.layout.take5_f3_list_item, null);
            }

            // Configure the view for this Crime
            final CheckBoxData c = getItem(position);
            TextView textOne =
                    (TextView)convertView.findViewById(R.id.fragment_three_list_heading);
            textOne.setText(c.getString());
            TextView textTwo =
                    (TextView)convertView.findViewById(R.id.fragment_three_list_sub);
            textTwo.setText(subHeadings[position]);
            CheckBox checkBox = (CheckBox)
                    convertView.findViewById(R.id.fragment_three_list_check);
            checkBox.setChecked(c.isYes());
            return convertView;
        }
    }
}

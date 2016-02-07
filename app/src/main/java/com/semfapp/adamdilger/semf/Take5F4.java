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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5F4 extends ListFragment {

    public final int REQUEST_EDIT_ITEM = 0;
    public final int REQUEST_ADD_ITEM = 1;

    Take5Data data;
    ArrayList<Take5RiskElement> mRiskElements;
    Button mAddButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = Take5Data.get(getActivity());
        mRiskElements = data.getRiskElements();
        ListAdapter adapter = new ListAdapter(mRiskElements);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflator,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflator.inflate(R.layout.fragment_take5_f4_list, container, false);

        mAddButton = (Button) v.findViewById(R.id.buttonAddElement);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                Take5AddElementDialog dialog = Take5AddElementDialog.newInstance();
                dialog.setTargetFragment(Take5F4.this, REQUEST_ADD_ITEM);
                dialog.show(fm, "datedialog");
            }
        });

        return v;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Take5RiskElement c = ((ListAdapter)getListAdapter()).getItem(position);

        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        Take5EditElementDialog dialog = Take5EditElementDialog.newInstance(position, c.getOne(), c.mTwo, c.getRating().toString());
        dialog.setTargetFragment(Take5F4.this, REQUEST_EDIT_ITEM);
        dialog.show(fm, "datedialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int pos = data.getIntExtra(Take5EditElementDialog.POS_EXTRA, 0);
        String one = data.getStringExtra(Take5EditElementDialog.ONE_EXTRA);
        String rating = data.getStringExtra(Take5EditElementDialog.RATING_EXTRA);
        String two = data.getStringExtra(Take5EditElementDialog.TWO_EXTRA);
        if (resultCode == Activity.RESULT_OK) {
            //edit/update item details
            if (requestCode == REQUEST_EDIT_ITEM) {
                mRiskElements.get(pos).set(one, two, Take5RiskElement.Rating.valueOf(rating));
                ((ListAdapter) getListAdapter()).notifyDataSetChanged();
            }

            //add new item
            if (requestCode == REQUEST_ADD_ITEM) {
                if (one != null || two  != null || rating != null) {
                    mRiskElements.add(new Take5RiskElement(one, two, Take5RiskElement.Rating.valueOf(rating)));
                }
                ((ListAdapter)getListAdapter()).notifyDataSetChanged();
            }
        }

        //delete pos item
        if (resultCode == Activity.RESULT_FIRST_USER) {
            mRiskElements.remove(pos);
            ListAdapter listAdapter = (ListAdapter)getListAdapter();
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class ListAdapter extends ArrayAdapter<Take5RiskElement> {

        public ListAdapter(ArrayList<Take5RiskElement> elements) {
            super(getActivity(), 0, elements);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.take5_risk_element_item, null);
            }

            // Configure the view for this Crime
            Take5RiskElement r = getItem(position);
            TextView textOne =
                    (TextView)convertView.findViewById(R.id.list_item_one);
            textOne.setText(r.getOne());
            TextView textTwo =
                    (TextView)convertView.findViewById(R.id.list_item_risk);
            textTwo.setText(r.getRating().toString());
            TextView textThree =
                    (TextView)convertView.findViewById(R.id.list_item_two);
            textThree.setText(r.getTwo());
            return convertView;
        }
    }
}

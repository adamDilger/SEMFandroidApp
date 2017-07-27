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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5F4 extends ListFragment {
    private static final String TAG = "Take5F4";

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
                Intent i = new Intent(getActivity(), Take5RiskElementActivity.class);
                startActivityForResult(i, 123);
            }
        });

        return v;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Take5RiskElement c = ((ListAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), Take5RiskElementActivity.class);

        i.putExtra(Take5RiskElementActivity.POS_EXTRA, position);
        i.putExtra(Take5RiskElementActivity.ONE_EXTRA, c.getOne());
        i.putExtra(Take5RiskElementActivity.RATING_EXTRA, c.getRating().toString());
        i.putExtra(Take5RiskElementActivity.TWO_EXTRA, c.mTwo);
        i.putExtra(Take5RiskElementActivity.IMAGE_PATH_EXTRA, c.imagePath);

        startActivityForResult(i, 123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            Log.d(TAG, "onActivityResult: data == null");
            return;
        }

        if (resultCode == Activity.RESULT_OK) {
            int pos = data.getIntExtra(Take5RiskElementActivity.POS_EXTRA, -1);
            String one = data.getStringExtra(Take5RiskElementActivity.ONE_EXTRA);
            String rating = data.getStringExtra(Take5RiskElementActivity.RATING_EXTRA);
            String two = data.getStringExtra(Take5RiskElementActivity.TWO_EXTRA);
            String imagePath = data.getStringExtra(Take5RiskElementActivity.IMAGE_PATH_EXTRA);

            Take5RiskElement element;

            if (pos == -1) {
                //new item
                element = new Take5RiskElement(one, two, Take5RiskElement.Rating.NA);

                if (rating != null) {
                    if (!rating.equals("")) {
                        element.mRating = Take5RiskElement.Rating.valueOf(rating);
                    }
                }

                mRiskElements.add(element);
            } else {
                //editing item
                element = mRiskElements.get(pos);
                element.set(one, two, Take5RiskElement.Rating.valueOf(rating));
            }


            element.imagePath = imagePath;

        } else if (resultCode == Activity.RESULT_FIRST_USER) {
            int pos = data.getIntExtra(Take5RiskElementActivity.POS_EXTRA, -1);
            Log.d(TAG, "onActivityResult: Deleting" + String.format("pos: %d", pos));

            //pos == -1 if item has not been added yet
            //therefore just discard it
            if (pos != -1) {
                mRiskElements.remove(pos);
            }
        }

        //notify adapter of data change
        ((ListAdapter)getListAdapter()).notifyDataSetChanged();
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
            TextView textOne = (TextView)convertView.findViewById(R.id.list_item_one);
            TextView textTwo = (TextView)convertView.findViewById(R.id.list_item_risk);
            TextView textThree = (TextView)convertView.findViewById(R.id.list_item_two);
            View imageCount = convertView.findViewById(R.id.image_count_view);

            Take5RiskElement r = getItem(position);

            textOne.setText(r.getOne());
            textTwo.setText(r.getRating().toString());
            textThree.setText(r.getTwo());

            if (r.imagePath != null) {
                imageCount.setVisibility(View.VISIBLE);
            } else {
                imageCount.setVisibility(View.GONE);
            }

            return convertView;
        }
    }
}

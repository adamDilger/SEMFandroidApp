package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adamdilger on 4/02/16.
 */
public class Take5CheckListAdapter extends RecyclerView.Adapter<Take5CheckListAdapter.Take5CheckViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Take5Data.CheckBoxData> array;

    public Take5CheckListAdapter(Context context, ArrayList<Take5Data.CheckBoxData> array) {
        this.layoutInflater = LayoutInflater.from(context);
        this.array = array;
    }

    @Override
    public Take5CheckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.take5_list_item, parent, false);
        Take5CheckViewHolder holder = new Take5CheckViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final Take5CheckViewHolder holder, int position) {
        final Take5Data.CheckBoxData current = array.get(position);

        // set heading and subheading if applicable
        holder.heading.setText(current.getHeading());

        if (current.getSubHeading() == null) {
            //set subheading layout to GONE
            holder.subHeading.setVisibility(View.GONE);
        } else {
            //set subheading text
            holder.subHeading.setText(current.getSubHeading());
        }

        //set checkbox value if applicable
        Take5Data.CheckValue checkValue = current.getCheckValue();

        if (checkValue == Take5Data.CheckValue.YES) {
            holder.radioButtonYes.setChecked(true);
        } else if (checkValue == Take5Data.CheckValue.NO) {
            holder.radioButtonNo.setChecked(false);
        }

        //set checkbox listeners
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.take5_radio_yes) {
                    current.setIsYes(Take5Data.CheckValue.YES);
                } else if (checkedId == R.id.take5_radio_no) {
                    current.setIsYes(Take5Data.CheckValue.NO);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    class Take5CheckViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView subHeading;
        RadioButton radioButtonYes, radioButtonNo;
        RadioGroup radioGroup;

        public Take5CheckViewHolder(View itemView) {
            super(itemView);

            heading = (TextView) itemView.findViewById(R.id.take5_list_heading);
            subHeading = (TextView) itemView.findViewById(R.id.take5_list_subheading);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            radioButtonYes= (RadioButton) itemView.findViewById(R.id.take5_radio_yes);
            radioButtonNo = (RadioButton) itemView.findViewById(R.id.take5_radio_no);
        }
    }
}

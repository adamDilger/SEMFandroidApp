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

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Adam on 18/07/2015.
 */
public class Take5Data {

    private static Take5Data sTake5Data;

    private Date mDate;
    private String[] labels;                        //label array from R.strings
    private ArrayList<CheckBoxData> mCheckBoxs;     //each checkbox entry
    private ArrayList<Take5RiskElement> mRiskElements;   //elements for list
    private String[] mEditTexts;                    //text entries from user

    private Take5Data(Context appContext) {
        labels = appContext.getResources().getStringArray(R.array.labels);

        mCheckBoxs = new ArrayList<>();
        mRiskElements = new ArrayList<>();

        //creates all checkboxs with unique id, adds string, and sets all unchecked
        for (int x = 0; x < labels.length; x++) {
            mCheckBoxs.add(new CheckBoxData(x, labels[x], false));
        }

        /**
         * [0] = job references
         * [1] = location
         * [2] = task
         * [3] = names
         */
        mEditTexts = new String[4];
        mDate = newDate();
    }

    public static Take5Data get(Context c) {
        if (sTake5Data == null) {
            sTake5Data = new Take5Data(c.getApplicationContext());
        }
        return sTake5Data;
    }


    /**
     * returns checkboxes for fragments to use.
     * getCheckBoxes returns all checkboxes
     * Section one contains the first 7 boxes (under item 1 on sheet)
     * Two the next 11 (under item 2)
     * Three holds the last three boxes (items 3,4 and 5)
     */
    public ArrayList<CheckBoxData> getCheckBoxs() {
        return mCheckBoxs;
    }
    public ArrayList<CheckBoxData> getSectionOneCheckBoxs() {
        return new ArrayList<>(mCheckBoxs.subList(0,7));
    }
    public ArrayList<CheckBoxData> getSectionTwoCheckBoxs() {
        return new ArrayList<>(mCheckBoxs.subList(7,18));
    }
    public ArrayList<CheckBoxData> getSectionThreeCheckBoxs() {
        return new ArrayList<>(mCheckBoxs.subList(18,21));
    }

    public ArrayList<Take5RiskElement> getRiskElements() {
        return mRiskElements;
    }

    public String[] getEditTexts() {
        return mEditTexts;
    }

    public Date newDate() {
        mDate = new Date();
        return mDate;
    }

    public Date getDate() {
        if (mDate == null){
            mDate = newDate();
        }
        return mDate;
    }

    public void clearData() {
        mDate = newDate();
        for (CheckBoxData c:mCheckBoxs) {
            c.setIsYes(false);
        }
        mRiskElements.clear();
        for (int x = 0; x < mEditTexts.length; x++) {
            mEditTexts[x] = null;
        }
    }

    public class CheckBoxData {
        private int mId;
        private String mString;
        private boolean mIsYes;

        public CheckBoxData(int id, String string, boolean isYes) {
            mId = id;
            mString = string;
            mIsYes = isYes;
        }

        public boolean isYes() {
            return mIsYes;
        }

        public int getId() {
            return mId;
        }

        public String getString() {
            return mString;
        }

        public void setIsYes(boolean isYes) {
            mIsYes = isYes;
        }

        public void checked() {
            if (mIsYes) {
                mIsYes = false;
            } else {
                mIsYes = true;
            }
        }
    }

    public static void exitDelete() {
        sTake5Data = null;
    }
}

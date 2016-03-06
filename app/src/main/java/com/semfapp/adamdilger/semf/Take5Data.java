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

    /*
        CheckValue
        Value of checkboxes. NA means neither yes or no has been checked
     */
    public enum CheckValue {
        YES,
        NO,
        NA
    }

    private static Take5Data sTake5Data;

    private Date mDate;

    private ArrayList<CheckBoxData> mCheckBoxsSection1;     //each checkbox entry
    private ArrayList<CheckBoxData> mCheckBoxsSection2;     //each checkbox entry
    private ArrayList<CheckBoxData> mCheckBoxsSection3;     //each checkbox entry

    private ArrayList<Take5RiskElement> mRiskElements;   //elements for list
    private String[] mEditTexts;                    //text entries from user

    private Take5Data(Context appContext) {
        String[] section1Labels = appContext.getResources().getStringArray(R.array.section1);
        String[] section2Labels = appContext.getResources().getStringArray(R.array.section2);
        String[] section2SubLabels = appContext.getResources().getStringArray(R.array.section2subheadings);
        String[] section3Labels = appContext.getResources().getStringArray(R.array.section3);

        mCheckBoxsSection1 = new ArrayList<>();
        mCheckBoxsSection2 = new ArrayList<>();
        mCheckBoxsSection3 = new ArrayList<>();
        mRiskElements = new ArrayList<>();

        //creates all checkboxs with unique id, adds string, and sets all unchecked
        for (int x = 0; x < section1Labels.length; x++) {
            mCheckBoxsSection1.add(new CheckBoxData(x, section1Labels[x], null, CheckValue.NA));
        }

        for (int x = 0; x < section2Labels.length; x++) {
            mCheckBoxsSection2.add(
                    new CheckBoxData(x, section2Labels[x], section2SubLabels[x], CheckValue.NA));
        }

        for (int x = 0; x < section3Labels.length; x++) {
            mCheckBoxsSection3.add(new CheckBoxData(x, section3Labels[x], null, CheckValue.NA));
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

    // GETTERS FOR CHECKBOX DATA ARRAYS
    public ArrayList<CheckBoxData> getSectionOneCheckBoxs() {
        return mCheckBoxsSection1;
    }
    public ArrayList<CheckBoxData> getSectionTwoCheckBoxs() {
        return mCheckBoxsSection2;
    }
    public ArrayList<CheckBoxData> getSectionThreeCheckBoxs() {
        return mCheckBoxsSection3;
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

    public class CheckBoxData {
        private int mId;
        private String mHeading;
        private String mSubHeading;
        private CheckValue mCheckValue;

        public CheckBoxData(int id, String heading, String subheading, CheckValue checkValue) {
            mId = id;
            mHeading = heading;
            mSubHeading = subheading;
            mCheckValue = checkValue;
        }

        public CheckValue getCheckValue() {
            return mCheckValue;
        }

        public int getId() {
            return mId;
        }

        public String getHeading() {
            return mHeading;
        }

        public String getSubHeading() {return mSubHeading; }

        public void setIsYes(CheckValue isYes) {
            mCheckValue = isYes;
        }
    }

    public boolean allFieldsFilled() {

        for (CheckBoxData x : mCheckBoxsSection1) {
            if (x.getCheckValue() == CheckValue.NA) {
                return false;
            }
        }

        for (CheckBoxData x : mCheckBoxsSection2) {
            if (x.getCheckValue() == CheckValue.NA) {
                return false;
            }
        }

        for (CheckBoxData x : mCheckBoxsSection3) {
            if (x.getCheckValue() == CheckValue.NA) {
                return false;
            }
        }

        return true;
    }

    public static void exitDelete() {
        sTake5Data = null;
    }
}

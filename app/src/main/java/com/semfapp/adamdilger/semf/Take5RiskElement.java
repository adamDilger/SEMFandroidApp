package com.semfapp.adamdilger.semf;

/**
 * Created by adamdilger on 23/11/2015.
 */
public class Take5RiskElement {
    String mOne, mTwo;
    Rating mRating;
    enum Rating {
        LOW,
        MED,
        HIGH,
        NA
    }

    public Take5RiskElement(String one, String two, Rating rating) {
        mOne = one;
        mTwo = two;
        mRating = rating;
    }

    public Rating getRating() {return mRating;}

    public String getOne() {
        return mOne;
    }

    public String getTwo() {
        return mTwo;
    }

    public void set(String s, String f, Rating c) {
        mOne = s;
        mTwo  = f;
        mRating = c;
    }
}
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
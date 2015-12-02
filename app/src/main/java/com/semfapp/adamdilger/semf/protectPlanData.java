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

import java.util.ArrayList;

/**
 * Created by adamdilger on 16/11/2015.
 */
public class protectPlanData {
    private static protectPlanData ourInstance;
    private String plan;
    private String effect;
    private String wrong;
    private String prevented;
    private String ramifications;

    private protectPlanData() {
        plan = "";
        effect = "";
        wrong = "";
        prevented = "";
        ramifications = "";
    }

    public static protectPlanData getInstance() {
        if (ourInstance == null) {
            ourInstance = new protectPlanData();
        }

        return ourInstance;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getPrevented() {
        return prevented;
    }

    public void setPrevented(String prevented) {
        this.prevented = prevented;
    }

    public String getRamifications() {
        return ramifications;
    }

    public void setRamifications(String ramifications) {
        this.ramifications = ramifications;
    }

    public ArrayList<String[]> getArray() {

        ArrayList<String[]> array = new ArrayList<String[]>();

        array.add(plan.split(System.lineSeparator()));
        array.add(effect.split(System.lineSeparator()));
        array.add(wrong.split(System.lineSeparator()));
        array.add(prevented.split(System.lineSeparator()));
        array.add(ramifications.split(System.lineSeparator()));

        return array;
    }

    public void exitDelete() {
        ourInstance = null;
    }
}

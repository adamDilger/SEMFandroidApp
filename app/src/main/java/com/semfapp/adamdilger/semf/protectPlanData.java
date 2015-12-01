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

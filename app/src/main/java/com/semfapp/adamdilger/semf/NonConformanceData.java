package com.semfapp.adamdilger.semf;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by adamdilger on 22/11/2015.
 */
public class NonConformanceData {
    private static NonConformanceData ourInstance;
    private String jobNumber;
    private String site;
    private String location;
    private String recipient;
    private String recipientEmail;
    private String description;
    private String actions;

    private NonConformanceData() {
        jobNumber = "";
        site = "";
        location = "";
        recipient = "";
        recipientEmail = "";
        description = "";
        actions = "";
    }

    public static NonConformanceData getInstance() {
        if (ourInstance == null) {
            ourInstance = new NonConformanceData();
        }

        return ourInstance;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
}
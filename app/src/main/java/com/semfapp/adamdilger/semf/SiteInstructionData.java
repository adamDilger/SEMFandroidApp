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

import android.graphics.Bitmap;
import android.os.Environment;

import com.itextpdf.text.Image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adamdilger on 24/11/2015.
 */
public class SiteInstructionData {
    private static SiteInstructionData ourInstance;
    private String jobNumber;
    private String site;
    private String location;
    private String recipient;
    private String recipientEmail;
    private String description;
    private PhotoLibrary images;

    private SiteInstructionData () {
        jobNumber = "";
        site = "";
        location = "";
        recipient = "";
        recipientEmail = "";
        description = "";
        images = new PhotoLibrary();
    }

    public static SiteInstructionData getInstance() {
        if (ourInstance == null) {
            ourInstance = new SiteInstructionData ();
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

    public void addImage(File image) {
        try {
            images.addImage(image);
        } catch (Exception e) {

        }
    }

    public Image getImage(int index) {
        return images.getImage(index);
    }

    public Bitmap getImageBitmap(int index) {
        return images.getBitmap(index);
    }

    public ArrayList<ImageFile> getImageArray() {
        return images.library;

    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        image.deleteOnExit();
        return image;
    }

    public PhotoLibrary getImages() {
        return images;
    }

    public void exitDelete() {
        images.deleteFiles();
        ourInstance = null;
    }
}
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
 * Created by adamdilger on 16/11/2015.
 */
public class HazardIdData {
    private static HazardIdData ourInstance;
    private String projectNumber;
    private String location;
    private String hazard;
    private String ramifications;
    private String resolution;
    private PhotoLibrary images;

    private HazardIdData() {
        location = "";
        hazard = "";
        ramifications = "";
        resolution = "";
        images = new PhotoLibrary();
    }

    public static HazardIdData getInstance() {
        if (ourInstance == null) {
            ourInstance = new HazardIdData();
        }

        return ourInstance;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {

        this.projectNumber = projectNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHazard() {
        return hazard;
    }

    public void setHazard(String hazard) {
        this.hazard = hazard;
    }

    public String getRamifications() {
        return ramifications;
    }

    public void setRamifications(String ramifications) {
        this.ramifications = ramifications;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public ArrayList<String[]> getArray() {

        ArrayList<String[]> array = new ArrayList<String[]>();

        array.add(location.split(System.lineSeparator()));
        array.add(hazard.split(System.lineSeparator()));
        array.add(ramifications.split(System.lineSeparator()));
        array.add(resolution.split(System.lineSeparator()));

        return array;
    }

    public int getImageArraySize() {
        return images.size();
    }


    public void addImage(File image){
        images.addImage(image);
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

    private void deleteImages() {

    }

    public PhotoLibrary getImages() {
        return images;
    }

    public void exitDelete() {
        images.deleteFiles();
        ourInstance = null;
    }

}

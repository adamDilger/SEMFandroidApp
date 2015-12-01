package com.semfapp.adamdilger.semf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;

import com.itextpdf.text.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by adamdilger on 19/11/2015.
 */
public class PhotoLibrary {
    ArrayList<ImageFile> library;

    public PhotoLibrary() {
        library = new ArrayList<>();

    }

    public int size() {
        return library.size();
    }

    public void addImage(File image) {
        ExifInterface exifInterface = null;

        try {
            exifInterface = new ExifInterface(image.getPath());
        } catch (Exception e) {
            System.out.println("ERROR :" + e.toString());
        }
        ImageFile imageFile = new ImageFile(image);

        if (exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL) == ExifInterface.ORIENTATION_ROTATE_90){
            imageFile.getImage().setRotationDegrees(270);
        } else if (exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL) == ExifInterface.ORIENTATION_ROTATE_180) {
            imageFile.getImage().setRotationDegrees(180);
        }

        library.add(imageFile);
    }

    public ImageFile getImageFile(int index) {
        return library.get(index);
    }

    public void deleteImageFile(int position) {
        library.get(position).getImageFile().delete();
        library.remove(position);
    }

    public Image getImage(int index) {
        return library.get(index).getImage();
    }

    public Bitmap getBitmap(int index) {
        return library.get(index).getBitmap();
    }

    public void deleteFiles() {

        for (ImageFile i : library) {
            i.getImageFile().delete();
            System.out.println("IMAGE DELETED");
        }

        library.clear();
    }
}

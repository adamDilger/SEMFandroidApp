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
import android.media.ExifInterface;

import com.itextpdf.text.Image;

import java.io.File;
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

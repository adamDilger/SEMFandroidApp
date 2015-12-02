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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.itextpdf.text.Image;

import java.io.File;

/**
 * Created by adamdilger on 20/11/2015.
 */
public class ImageFile {

    File imageFile;
    Image image;
    Bitmap thumbnail;
    String name;

    public ImageFile(File file) {
        imageFile = file;

        try {
            ExifInterface exifInterface = new ExifInterface(file.getPath());

            exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        } catch (Exception r) {}

        name = file.getName();

        image = null;
        thumbnail = null;
    }

    public File getImageFile() {
        return imageFile;
    }

    public Image getImage() {

        if (image == null) {
            try {
                String path = imageFile.getPath();

                image = Image.getInstance(path);
            } catch (Exception e) {}
        }

        return image;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        if (thumbnail == null) {

            String filePath = imageFile.getPath();
            ExifInterface exifInterface = null;
            Matrix matrix = new Matrix();
            int rotation = 0;

            try {
                exifInterface = new ExifInterface(filePath);

                if (exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 1) == ExifInterface.ORIENTATION_ROTATE_90){
                    rotation = 90;
                } else if (exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 1) == ExifInterface.ORIENTATION_ROTATE_180) {
                    rotation = 180;
                }

                matrix.setRotate(rotation);

            } catch (Exception r) {}

            float destWidth = 480;
            float destHeight = 800;
            // Read in the dimensions of the image on disk
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            float srcWidth = options.outWidth;
            float srcHeight = options.outHeight;
            int inSampleSize = 1;
            if (srcHeight > destHeight || srcWidth > destWidth) {
                if (srcWidth > srcHeight) {
                    inSampleSize = Math.round(srcHeight / destHeight);
                } else {
                    inSampleSize = Math.round(srcWidth / destWidth);
                }
            }
            options = new BitmapFactory.Options();
            options.inSampleSize = inSampleSize;

            Bitmap b = BitmapFactory.decodeFile(filePath, options);

            thumbnail = Bitmap.createBitmap(b, 0,0,b.getWidth(), b.getHeight(), matrix, false);
        }

        return thumbnail;
    }
}

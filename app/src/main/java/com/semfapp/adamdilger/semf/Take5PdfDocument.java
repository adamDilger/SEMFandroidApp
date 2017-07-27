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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.support.v4.graphics.BitmapCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.semfapp.adamdilger.semf.Take5Data.CheckBoxData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adam on 12/07/2015.
 */
public class Take5PdfDocument {
    private static final String TAG = "Take5PdfDocument";

    //point values for PDF drawing
    private final int A4_WIDTH = 595;
    private final int A4_HEIGHT = 842;
    private final int BOX_WIDTH = 14;
//    private final float FONT12 = 3.4f;
//    private final float FONT14 = 4f;

    private final float FONT11;
    private final float FONT12;
    private final float FONT14;
    private final float FONT16;

    private Canvas can;
    private Context mContext;
    public Typeface impact, roboto, carlitoBold;
    private ArrayList<CheckBoxData> mCheckBoxSectionOne;
    private ArrayList<CheckBoxData> mCheckBoxSectionTwo;
    private ArrayList<CheckBoxData> mCheckBoxSectionThree;
    private ArrayList<Take5RiskElement> mRiskElements;
    private String[] mEditTextValues;
    private Date mDate;
    private String mDateString = "";
    private String mTimeString = "";

    public Take5PdfDocument(Context appContext) {
        can = new Canvas();
        mContext = appContext;

//        FONT11 = mContext.getResources().getDimensionPixelSize(R.dimen.font_11);
//        FONT12 = mContext.getResources().getDimensionPixelSize(R.dimen.font_12);
//        FONT14 = mContext.getResources().getDimensionPixelSize(R.dimen.font_14);
//        FONT16 = mContext.getResources().getDimensionPixelSize(R.dimen.font_16);

        FONT11 = 10;
        FONT12 = 11;
        FONT14 = 13;
        FONT16 = 15;

        Take5Data d = Take5Data.get(appContext);

        mCheckBoxSectionOne = d.getSectionOneCheckBoxs();
        mCheckBoxSectionTwo = d.getSectionTwoCheckBoxs();
        mCheckBoxSectionThree = d.getSectionThreeCheckBoxs();
        mEditTextValues = d.getEditTexts();
        mDate = d.getDate();
        mRiskElements = d.getRiskElements();
        setDateTime(mDate);

        //Create custom font Typefaces
        impact = Typeface.createFromAsset(appContext.getAssets(), "impact.ttf");
        roboto = Typeface.createFromAsset(appContext.getAssets(), "Roboto-LightItalic.ttf");
        carlitoBold = Typeface.createFromAsset(appContext.getAssets(), "Carlito-Bold.ttf");
    }

    public PdfDocument createDocument() {
        //create new document
        PdfDocument document = new PdfDocument();

        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT,1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        can = page.getCanvas();
        Paint paint = new Paint();
        paint.setStrokeWidth(0.5f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        can.drawRect(1, 1, 594, 395, paint);

        /**
         * Page one Text Fields
         */
        drawText("Job Reference:", 9, 21, FONT14, carlitoBold);
        drawText("Date:", 354, 21, FONT14, carlitoBold);
        drawText("Time:", 473, 21, FONT14, carlitoBold);
        drawText("Location:", 9, 48, FONT14, carlitoBold);
        drawText("Task:", 261, 48, FONT14, carlitoBold);

        paint.setPathEffect(new DashPathEffect(new float[]{1.5f, 1}, 0));
        can.drawLine(85, 36, 350, 36, paint);
        can.drawLine(386, 36, 468, 36, paint);
        can.drawLine(506, 36, 585, 36, paint);
        can.drawLine(59, 60, 256, 60, paint);
        can.drawLine(291, 60, 585, 60, paint);

        if (mEditTextValues[0] != null) {
            drawText(mEditTextValues[0], 98, 19, FONT14, roboto);
        }
        if (mEditTextValues[1] != null) {
            drawText(mEditTextValues[1], 65, 44, FONT14, roboto);
        }
        if (mEditTextValues[2] != null) {
            drawText(mEditTextValues[2],297,44,FONT14, roboto);
        }
        drawText(mDateString, 390, 19, FONT14, roboto);
        drawText(mTimeString, 509, 19, FONT14, roboto);


        /**
         * Section One (Stop, step back...)
         */
        drawHeader1(8, 69, 202, "Stop, step back and think", 1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.35f);
        paint.setPathEffect(null);
        int left, top;

        //draw left boxes
        left = 177;
        top = 108;
        for (int x = 0; x < mCheckBoxSectionOne.size(); x++) {
            Take5Data.CheckValue isYes = mCheckBoxSectionOne.get(x).getCheckValue();
            float topLoc = top + (x * 33);

            if (x < 1) {    //allowing for different 1st row size
                drawBox(left, topLoc, paint, true, isYes);
            } else {
                float newTop = topLoc - 6;
                if (x < 3) {
                    drawBox(left, newTop, paint, false, isYes);
                } else {
                    drawBox(left, newTop, paint, true, isYes);
                }
            }
        }

        DashPathEffect pathEffect = new DashPathEffect(new float[]{1, 1.5f}, 0);

        drawText(mCheckBoxSectionOne.get(0).getHeading(),13, 113, FONT12, carlitoBold);

        for (int x = 1; x < mCheckBoxSectionOne.size(); x++) {
            int height = 132 + ((x - 1) * 33);
            drawText(mCheckBoxSectionOne.get(x).getHeading(), 13, height, FONT12, carlitoBold);
            paint.setPathEffect(pathEffect);
            can.drawLine(10, height - 6, 222, height - 6, paint);
            paint.setPathEffect(null);
        }

        /**
         * Section Two (Identidy the Hazards...)
         */
        drawHeader1(230, 69, 345, "Identify the hazard(s)", 2);

        //draw right boxes
        left = 542;
        top = 104;
        for (int x = 0; x < mCheckBoxSectionTwo.size(); x++) {
            float topLoc = top + (x * 20.7f);
            Take5Data.CheckValue isYes = mCheckBoxSectionTwo.get(x).getCheckValue();
            drawBox(left, topLoc, paint, false, isYes);
        }



        for (int x = 0; x < mCheckBoxSectionTwo.size(); x++) {
            float height = 105 + (x * 20.7f);
            drawText(mCheckBoxSectionTwo.get(x).getHeading(), 238, height + 3, FONT12, carlitoBold);
            if (x > 0) {
                paint.setPathEffect(pathEffect);
                can.drawLine(238, height - 4, 581, height - 4, paint);
                paint.setPathEffect(null);
            }
        }

        /**
         * draw section 3,4,5 (including checkboxes)
         */
        drawSmallCircle(8, 331, "Assess the level of risk", 3);
        drawSmallCircle(202, 331, "Control the hazards", 4);
        drawSmallCircle(398, 331, "Proceed safely", 5);

        /**
         * Draw Page Two
         */
        int xLoc = 7;
        int yLoc = 420;
        int height;
        int width = 565;
        int RADIUS = 14;
        float INNER_RADIUS = 13;
        int centre = yLoc + 7 + RADIUS;
        width = xLoc + width;
        int middle = width -  340;
        can.drawRect(1, 420, 594, 420 + 395, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(impact);
        paint.setTextSize(12);

        can.drawCircle(xLoc + RADIUS, centre, RADIUS, paint);
        can.drawRect(xLoc + RADIUS, centre - RADIUS, width, centre + RADIUS, paint);
        can.drawCircle(width, centre, RADIUS, paint);
        paint.setColor(Color.WHITE);
        can.drawCircle(xLoc + RADIUS, centre, INNER_RADIUS, paint);
        can.drawCircle(width, centre, INNER_RADIUS, paint);
        can.drawRect(middle, centre - INNER_RADIUS, width, centre + INNER_RADIUS, paint);
        paint.setColor(Color.BLACK);
        can.drawCircle(middle, centre, RADIUS, paint);

        paint.setColor(Color.WHITE);
        can.drawText("SAFE WORK METHOD STATEMENT (SWMS)", xLoc + 31, centre + 5, paint);
        paint.setTextSize(16);
        paint.setColor(Color.BLACK);
        can.drawText(String.valueOf(4), xLoc + RADIUS - 4, centre + 6, paint);

        height = 50;
        drawText("What are the hazards and risks?", 25, yLoc + height, FONT12, carlitoBold);
        drawText("Risk\nRating", 267, yLoc + 47, FONT12, carlitoBold);
        drawText("How will hazards and risks be controlled?",
                319, yLoc + height, FONT12, carlitoBold);

        paint.setPathEffect(pathEffect);
        can.drawLine(262, yLoc + 45, 262, yLoc + 320, paint);
        can.drawLine(302, yLoc + 45, 302, yLoc + 320, paint);
        paint.setPathEffect(null);

        float currentItemHeight = yLoc + 75;
        float padding = 5;

        for (int x = 0; x < mRiskElements.size(); x++) {
            int textHeight = (int)currentItemHeight;
            float totalItemHeight = drawRiskElement(textHeight, mRiskElements.get(x));
            currentItemHeight += totalItemHeight + padding;
        }

        paint.setPathEffect(pathEffect);
        height = yLoc + 350;
        drawText("Name/s:", 12, height, FONT12, carlitoBold);
        drawText(mEditTextValues[3], 55, height - 3, FONT14, roboto);
        paint.setPathEffect(pathEffect);
        can.drawLine(50, height + 12, 580, height + 12, paint);
        paint.setPathEffect(null);


        height = yLoc + 372;
        drawText("Signatures:", 12, height, FONT12, carlitoBold);
        drawText("Date:", 468, height,FONT12, carlitoBold);
        drawText(mDateString, 497, height - 3, FONT14, roboto);
        paint.setPathEffect(pathEffect);
        can.drawLine(60, height + 12, 464, height + 12, paint);
        can.drawLine(492, height + 12, 580, height + 12, paint);
        paint.setPathEffect(null);

        // finish the page
        document.finishPage(page);

        int imagePageCount = 2;

        for (Take5RiskElement risk : mRiskElements) {

            if (risk.imagePath != null) {

                // crate a page description
                PdfDocument.PageInfo pageInfo1 = new PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, imagePageCount).create();

                PdfDocument.Page imagePage = document.startPage(pageInfo1);
                Canvas canvas = imagePage.getCanvas();

                try {

                    Bitmap original = BitmapFactory.decodeFile(risk.imagePath);

                    Bitmap b = resize(original, canvas.getWidth() - 100, canvas.getHeight() - 100);
                    canvas.drawBitmap(b, 50, 60, new Paint());
//                    canvas.drawText(risk.getOne(), 50, 40, new Paint());

                    Path textPath = new Path();
                    textPath.moveTo(50, 50);
                    textPath.lineTo(canvas.getWidth() - 100, 50);

                    canvas.drawTextOnPath(risk.getOne(), textPath, 0, 0, new Paint());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                document.finishPage(imagePage);

                imagePageCount++;

                new File(risk.imagePath).delete();
            }
        }

        // add more pages
        return document;
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {

        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        Log.d(TAG, "resize: " + String.format("width: %d, height: %d, ratioBitmap: %f, ratioMax: %f", width, height, ratioBitmap, ratioMax));

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        if (ratioMax > 1) {
            finalWidth = (int) ((float)maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float)maxWidth / ratioBitmap);
        }

        if (finalHeight > maxHeight) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
            finalHeight = maxHeight;
        }

        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    /**
     * Draw Section titles for sec 1 and 2
     * @param xLoc left location
     * @param yLoc top location
     * @param width size from xLoc, to middle of end circle
     * @param string header string
     * @param index header number
     */
    private void drawHeader1(int xLoc, int yLoc, int width, String string, int index) {
        int RADIUS = 14;
        float INNER_RADIUS = 13;
        int centre = yLoc + RADIUS;
        width = xLoc + width;
        int middle = width -  54;
        Paint paint = new Paint();
        Paint paintW = new Paint();
        paintW.setTypeface(impact);
        paint.setTypeface(impact);
        paintW.setColor(Color.WHITE);
        paintW.setTextSize(FONT12);
        paint.setTextSize(FONT12);

        can.drawCircle(xLoc + RADIUS, centre, RADIUS, paint);
        can.drawRect(xLoc + RADIUS, centre - RADIUS, width, centre + RADIUS, paint);
        can.drawCircle(width, centre, RADIUS, paint);
        can.drawCircle(xLoc + RADIUS, centre, INNER_RADIUS, paintW);
        can.drawCircle(width, centre, INNER_RADIUS, paintW);
        can.drawRect(middle, centre - INNER_RADIUS, width, centre + INNER_RADIUS, paintW);
        can.drawCircle(middle, centre, RADIUS, paint);

        can.drawText(string, xLoc + 31, centre + 5, paintW);
        can.drawText("YES   NO", width - 35, centre + 5, paint);
        paint.setTextSize(FONT16);
        can.drawText(String.valueOf(index), xLoc + RADIUS - 4, centre + 6, paint);

    }

    /**
     * Draw headers, for sec 3,4,5
     * @param xLoc left location
     * @param yLoc top location
     * @param string header string
     * @param index header index
     */
    private void drawSmallCircle(int xLoc, int yLoc, String string, int index) {
        int RADIUS = 14;
        float INNER_RADIUS = 13;
        int centre = yLoc + RADIUS;
        int width = xLoc + 176;
        Paint paint = new Paint();
        Paint paintW = new Paint();
        paintW.setTypeface(impact);
        paint.setTypeface(impact);
        paintW.setColor(Color.WHITE);
        paintW.setTextSize(FONT12);
        paint.setTextSize(FONT12);

        can.drawCircle(xLoc + RADIUS, centre, RADIUS, paint);
        can.drawRect(xLoc + RADIUS, centre - RADIUS, width, centre + RADIUS, paint);
        can.drawCircle(width, centre, RADIUS, paint);
        can.drawCircle(xLoc + RADIUS, centre, INNER_RADIUS, paintW);
        can.drawText(string, xLoc + 29, centre + 4.5f, paintW);
        paint.setTextSize(FONT16);
        can.drawText(String.valueOf(index), xLoc + RADIUS - 4, centre + 6, paint);

        //drawSmallCircle(8, 331, "Assess the level of risk", 3);
        drawText(mCheckBoxSectionThree.get(index - 3).getHeading(), xLoc + 8, yLoc + 35, FONT12, carlitoBold);

        drawText("YES", xLoc + 95, yLoc + 35, FONT14, impact);
        drawText("NO", xLoc + 138, yLoc + 35, FONT14, impact);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(.5f);
        can.drawRect(xLoc + 118, yLoc + 36, xLoc + 118 + 14, yLoc + 36 + 14, paint);

        paint.setStrokeWidth(1.5f);
        can.drawRect(xLoc + 156, yLoc + 36, xLoc + 156 + 14, yLoc + 36 + 14, paint);

        Take5Data.CheckValue isYes = mCheckBoxSectionThree.get(index - 3).getCheckValue();
        if (isYes == Take5Data.CheckValue.YES) {
            drawCheck(xLoc + 118, yLoc + 36, paint);
        } else if (isYes == Take5Data.CheckValue.NO) {
            drawCheck(xLoc + 156, yLoc + 36, paint);
        }
    }

    /**
     * Draw checkbox
     * @param xLoc left location
     * @param yLoc top location
     * @param paint Paint object
     */
    private void drawCheck(float xLoc, float yLoc, Paint paint) {
        float x1[] = {2.3f, 6f, 11.5f};
        float y1[] = {8f, 11f, 2.2f};
        Path path = new Path();
        path.moveTo(xLoc + x1[0], yLoc + y1[0]);
        path.lineTo(xLoc + x1[1], yLoc + y1[1]);
        path.lineTo(xLoc + x1[2], yLoc + y1[2]);
        paint.setStrokeWidth(3);
        can.drawPath(path, paint);
    }

    /**
     * Draw Two Checkboxes
     * @param left left location of leftmost box
     * @param top top location
     * @param paint Paint Object
     * @param isSecondBold true if second square should be bold
     * @param isYes true if Yes checkbox is ticked
     */
    private void drawBox(float left, float top, Paint paint,
                        boolean isSecondBold, Take5Data.CheckValue isYes) {

        int right = (int) left + BOX_WIDTH;
        int bottom = (int) top + BOX_WIDTH;

        if (isSecondBold) {
            paint.setStrokeWidth(.7f);
            can.drawRect(left, top, right, bottom, paint);
            paint.setStrokeWidth(1.5f);
            can.drawRect(left + 22, top, right + 22, bottom, paint);
        } else {
            paint.setStrokeWidth(1.5f);
            can.drawRect(left, top, right, bottom, paint);
            paint.setStrokeWidth(.7f);
            can.drawRect(left + 22, top, right + 22, bottom, paint);
        }

        if (isYes == Take5Data.CheckValue.YES) {
            drawCheck(left, top, paint);
        } else if (isYes == Take5Data.CheckValue.NO) {
            drawCheck(left + 22, top, paint);
        }
        paint.setStrokeWidth(.5f);
    }

    public void drawText(String string, float xLoc, float yLoc, float textSize, Typeface typeface) {

        if (string != null) {
            TextPaint mTextPaint=new TextPaint();
            mTextPaint.setTypeface(typeface);
            mTextPaint.setTextSize(textSize);

            StaticLayout mTextLayout = new StaticLayout(string, mTextPaint, ((int)(595 * 0.28)), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

            can.save();
            // calculate x and y position where your text will be placed

            can.translate(xLoc, yLoc);
            mTextLayout.draw(can);
            can.restore();
        }
    }

    public void setDateTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeformat = new SimpleDateFormat("hh:mm " + "aa".toLowerCase());

        mDateString = dateFormat.format(date);
        mTimeString = timeformat.format(date);
    }

    public float drawRiskElement(int height, Take5RiskElement element) {
        final float singleLineHeight = 12.5f;
        float totalItemHeight;

        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(roboto);
        textPaint.setTextSize(FONT11);
        textPaint.setColor(Color.BLACK);

        String oneString = "";
        String twoString = "";

        if (element.getOne() != null) {
            oneString = element.getOne();
        }
        if (element.getTwo() != null) {
            twoString = element.getTwo();
        }

        StaticLayout one = new StaticLayout(oneString, textPaint, 235, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout rating = new StaticLayout(element.getRating().toString(), textPaint, 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        StaticLayout two = new StaticLayout(twoString, textPaint, 250, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        if (one.getLineCount() > two.getLineCount()) {
            totalItemHeight = singleLineHeight * one.getLineCount();
        } else {
            totalItemHeight = singleLineHeight * two.getLineCount();
        }

        can.save();
        can.translate(15, height);
        one.draw(can);
        can.restore();

        can.save();
        can.translate(320, height);
        two.draw(can);
        can.restore();

        can.save();
        can.translate(270, height);
        rating.draw(can);
        can.restore();

        return totalItemHeight;
    }
}

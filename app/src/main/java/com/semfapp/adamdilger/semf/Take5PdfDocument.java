package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.semfapp.adamdilger.semf.Take5Data.CheckBoxData;

/**
 * Created by Adam on 12/07/2015.
 */
public class Take5PdfDocument {
    //point values for PDF drawing
    private final int A4_WIDTH = 595;
    private final int A4_HEIGHT = 842;
    private final int BOX_WIDTH = 14;
    private final float FONT12 = 3.4f;
    private final float FONT14 = 4f;

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

        Take5Data d = Take5Data.get(appContext.getApplicationContext());

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
            boolean isYes = mCheckBoxSectionOne.get(x).isYes();
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

        drawText(mCheckBoxSectionOne.get(0).getString(),13, 107, FONT12, carlitoBold);
        for (int x = 1; x < mCheckBoxSectionOne.size(); x++) {
            int height = 127 + ((x - 1) * 33);
            drawText(mCheckBoxSectionOne.get(x).getString(), 13, height, FONT12, carlitoBold);
            paint.setPathEffect(new DashPathEffect(new float[]{1, 2}, 0));
            can.drawLine(10, height - 2, 222, height - 2, paint);
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
            boolean isYes = mCheckBoxSectionTwo.get(x).isYes();
            drawBox(left, topLoc, paint, false, isYes);
        }

        for (int x = 0; x < mCheckBoxSectionTwo.size(); x++) {
            float height = 101 + (x * 20.7f);
            drawText(mCheckBoxSectionTwo.get(x).getString(), 238, height + 3, FONT12, carlitoBold);
            if (x > 0) {
                paint.setPathEffect(new DashPathEffect(new float[]{1, 2}, 0));
                can.drawLine(238, height, 581, height, paint);
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

        paint.setPathEffect(new DashPathEffect(new float[]{1, 1}, 0));
        can.drawLine(262, yLoc + 45, 262, yLoc + 320, paint);
        can.drawLine(302, yLoc + 45, 302, yLoc + 320, paint);
        paint.setPathEffect(null);

        for (int x = 0; x < mRiskElements.size(); x++) {
            int textHeight = yLoc + 75 + (x * 40);
            drawRiskElement(textHeight, mRiskElements.get(x));
        }

        paint.setPathEffect(new DashPathEffect(new float[] {1.5f,1}, 0));
        height = yLoc + 350;
        drawText("Name/s:", 12, height, FONT12, carlitoBold);
        drawText(mEditTextValues[3], 52, height - 3, FONT14, roboto);
        paint.setPathEffect(new DashPathEffect(new float[]{1, 1}, 0));
        can.drawLine(50, height + 12, 580, height + 12, paint);
        paint.setPathEffect(null);


        height = yLoc + 372;
        drawText("Signatures:", 12, height, FONT12, carlitoBold);
        drawText("Date:", 468, height,FONT12, carlitoBold);
        drawText(mDateString, 494, height - 3, FONT14, roboto);
        paint.setPathEffect(new DashPathEffect(new float[]{1, 1}, 0));
        can.drawLine(60, height + 12, 464, height + 12, paint);
        can.drawLine(492, height + 12, 580, height + 12, paint);
        paint.setPathEffect(null);

        // finish the page
        document.finishPage(page);

        // add more pages
        return document;
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
        paintW.setTextSize(12);
        paint.setTextSize(12);

        can.drawCircle(xLoc + RADIUS, centre, RADIUS, paint);
        can.drawRect(xLoc + RADIUS, centre - RADIUS, width, centre + RADIUS, paint);
        can.drawCircle(width, centre, RADIUS, paint);
        can.drawCircle(xLoc + RADIUS, centre, INNER_RADIUS, paintW);
        can.drawCircle(width, centre, INNER_RADIUS, paintW);
        can.drawRect(middle, centre - INNER_RADIUS, width, centre + INNER_RADIUS, paintW);
        can.drawCircle(middle, centre, RADIUS, paint);

        can.drawText(string, xLoc + 31, centre + 5, paintW);
        can.drawText("YES   NO", width - 35, centre + 5, paint);
        paint.setTextSize(16);
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
        paintW.setTextSize(12);
        paint.setTextSize(12);

        can.drawCircle(xLoc + RADIUS, centre, RADIUS, paint);
        can.drawRect(xLoc + RADIUS, centre - RADIUS, width, centre + RADIUS, paint);
        can.drawCircle(width, centre, RADIUS, paint);
        can.drawCircle(xLoc + RADIUS, centre, INNER_RADIUS, paintW);
        can.drawText(string, xLoc + 31, centre + 5, paintW);
        paint.setTextSize(16);
        can.drawText(String.valueOf(index), xLoc + RADIUS - 4, centre + 6, paint);

        //drawSmallCircle(8, 331, "Assess the level of risk", 3);
        drawText(mCheckBoxSectionThree.get(index - 3).getString(), xLoc + 8, yLoc + 35, FONT12, carlitoBold);

        drawText("YES", xLoc + 95, yLoc + 33, FONT14, impact);
        drawText("NO", xLoc + 138, yLoc + 33, FONT14, impact);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(.5f);
        can.drawRect(xLoc + 118, yLoc + 36, xLoc + 118 + 14, yLoc + 36 + 14, paint);

        paint.setStrokeWidth(1.5f);
        can.drawRect(xLoc + 156, yLoc + 36, xLoc + 156 + 14, yLoc + 36 + 14, paint);

        boolean isYes = mCheckBoxSectionThree.get(index - 3).isYes();
        if (isYes) {
            drawCheck(xLoc + 118, yLoc + 36, paint);
        } else {
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
                        boolean isSecondBold, boolean isYes) {

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

        if (isYes) {
            drawCheck(left, top, paint);
        } else {
            drawCheck(left + 22, top, paint);
        }
        paint.setStrokeWidth(.5f);
    }

    public void drawText(String string, float xLoc, float yLoc, float textSize, Typeface typeface) {
        TextView textv = new TextView(mContext);
        textv.layout(0, 0, 200, 160);
        textv.setText(string);
        textv.setTextSize(textSize);
        textv.setTypeface(typeface);
        textv.setTextColor(Color.BLACK);
        can.save();
        can.translate(xLoc, yLoc);
        textv.draw(can);
        can.restore();
    }

    public void setDateTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeformat = new SimpleDateFormat("hh:mm " + "aa".toLowerCase());

        mDateString = dateFormat.format(date);
        mTimeString = timeformat.format(date);
    }

    public void drawRiskElement(int height, Take5RiskElement element) {
        TextView textv = new TextView(mContext);
        TextView textm = new TextView(mContext);
        TextView textg = new TextView(mContext);

        textv.setText(element.getOne());
        textv.layout(0, 0, 230, 180);
        textv.setWidth(160);
        textv.setTextSize(FONT14);
        textv.setTypeface(roboto);
        textv.setTextColor(Color.BLACK);
        textm.setText(element.getTwo());
        textm.setTextSize(FONT14);
        textm.setTypeface(roboto);
        textm.setTextColor(Color.BLACK);
        textm.layout(0, 0, 250, 160);
        if (element.getRating().toString() == "NA") {
            textg.setText(" -");
        } else {
            textg.setText(element.getRating().toString());
        }
        textg.setTextSize(FONT14);
        textg.setTypeface(roboto);
        textg.setTextColor(Color.BLACK);
        textg.layout(0, 0, 100, 100);

        can.save();
        can.translate(30, height);
        textv.draw(can);
        can.restore();

        can.save();
        can.translate(320, height);
        textm.draw(can);
        can.restore();

        can.save();
        can.translate(270, height);
        textg.draw(can);
        can.restore();
    }
}

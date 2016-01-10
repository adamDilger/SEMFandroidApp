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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import org.jsoup.Jsoup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by adamdilger on 18/11/2015.
 */
public class Pdf {

    Activity activity;
    String htmlString;
    String filePath;
    ArrayList<ImageFile> images;

    // NEW ASYNCTASK METHODS

    public void createPdfToFile(Activity activity,
                             String htmlString,
                             String filePath,
                             @Nullable ArrayList<ImageFile> images) {
        this.activity = activity;
        this.htmlString = htmlString;
        this.images = images;
        this.filePath = filePath;

        new HtmlPdfConvertTask().execute();
    }


    //Writes pdf to class variable 'filePath'
    //uses asynctask
    //calls Communicator.finishedCreatingPdf() when finished
    private class HtmlPdfConvertTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        Communicator communicator = (Communicator)activity;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Creating PDF");
            progressDialog.setMessage("Wait while loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            createPDFNew(filePath,
                    htmlString,
                    images);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            communicator.finishedCreatingPdf();
        }
    }

    private void createPDFNew(String filePath, String htmlString, @Nullable ArrayList<ImageFile> images) {

        File file = null;

        try {
            file = new File(filePath);

            // step 1
            Document document = new Document();

            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            writer.setInitialLeading(12.5f);

            // step 3
            document.open();

            // step 4

            // CSS
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(activity.getAssets().open("styles.css"));
            cssResolver.addCss(cssFile);

            // HTML
            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            // Pipelines

            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            // XML Worker
            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);


            Drawable d = activity.getResources().getDrawable(R.drawable.logo_icon);
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();

            Image image = Image.getInstance(bitmapData);
            image.setAbsolutePosition(35, 763);
            image.scalePercent(12);
            document.add(image);

            p.parse(new ByteArrayInputStream(htmlString.getBytes(StandardCharsets.UTF_8)));

            if (images != null) {
                System.out.println("Adding IMage");

                for (int x = 0; x < images.size(); x++) {
                    Image cursor = images.get(x).getImage();

                    float ratio = cursor.getPlainHeight() / cursor.getPlainWidth();

                    float imgWidth = document.getPageSize().getWidth() - 100;
                    float imgHeight = document.getPageSize().getHeight() - 100;


                    cursor.scaleToFit(new Rectangle(imgWidth, imgHeight));

                    document.add(cursor);
                }
            }


            // step 5
            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createFilePath(Activity activity, String fileName) {
        File file = new File(activity.getExternalFilesDir(null), fileName + ".pdf");
        return file.getPath();
    }

    public static org.jsoup.nodes.Document getTemplate(Context context, @Nullable String jobNumber) {
        org.jsoup.nodes.Document documentTemplate = null;
        try {
            documentTemplate = Jsoup.parse(context.getAssets().open("template.html"), "utf-8", "http://www.example.com");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }

        String date = "Date: " + new SimpleDateFormat("d MMM yyyy").format(MainActivity.currentDate);
        documentTemplate.getElementById("date").text(date);

        if (jobNumber != "" && jobNumber != null) {
            documentTemplate.getElementById("job_number").text("Job Number: " + jobNumber);
        } else {
            documentTemplate.getElementById("job_number").text("-");
        }


        return documentTemplate;
    }
}

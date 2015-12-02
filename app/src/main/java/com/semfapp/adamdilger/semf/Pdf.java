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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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

    /*
    * Creates pdf from html given.
    * @param htmlString html for PDF creation
    * @param fileName fileName for pdf
    */
    public File createPDF(Context application, String htmlString, String fileName, @Nullable ArrayList<ImageFile> images) {

        File file = null;

        try {
            file = new File(application.getExternalFilesDir(null), fileName + ".pdf");

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
            CssFile cssFile = XMLWorkerHelper.getCSS(application.getAssets().open("styles.css"));
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


            Drawable d = application.getResources().getDrawable(R.drawable.logo_icon);
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
                for (int x = 0; x < images.size(); x++) {
                    Image cursor = images.get(x).getImage();

                    float imgWidth = document.getPageSize().getWidth() - 150;
                    float imgHeight = document.getPageSize().getHeight() - 150;


                    cursor.scaleToFit(new Rectangle(imgWidth, imgHeight));

                    document.add(cursor);
                }
            }


            // step 5
            document.close();


        } catch (Exception e) {
            Toast.makeText(application.getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
            System.out.println("Error: " + e.toString());
        }

        return file;
    }

    public static org.jsoup.nodes.Document getTemplate(Context context, @Nullable String jobNumber) {
        org.jsoup.nodes.Document documentTemplate = null;
        try {
            documentTemplate = Jsoup.parse(context.getAssets().open("template.html"), "utf-8", "http://www.example.com");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }

        System.out.printf("JobNum: " + jobNumber);

        String date = "Date: " + new SimpleDateFormat("d MMM yyyy").format(new Date());
        documentTemplate.getElementById("date").text(date);

        if (jobNumber != "" && jobNumber != null) {
            documentTemplate.getElementById("job_number").text("Job Number: " + jobNumber);
        } else {
            documentTemplate.getElementById("job_number").text("-");
        }


        return documentTemplate;
    }
}

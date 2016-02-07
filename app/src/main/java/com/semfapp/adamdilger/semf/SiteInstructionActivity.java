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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.inputmethod.InputMethodManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

/**
 * Created by adamdilger on 24/11/2015.
 */
public class SiteInstructionActivity extends AbstractTabLayoutFragment implements Communicator {

    private SiteInstructionData data;
    private InputMethodManager imm;
    public File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = SiteInstructionData.getInstance();

        super.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imm = (InputMethodManager)(getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE));
    }

    @Override
    String setToolbarTitle() {
        return "Site Instruction";
    }

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch (index) {
            case 0:
                return new SiteInstructionF1();
            case 1:
                return getQuestionFragment(R.string.description);
            case 2:
                return new SiteInstructionF3();
            default:
                return null;
        }
    }

    @Override
    int getFragmentCount() {
        return 3;
    }

    @Override
    public void saveText(String text, int code) {
        switch (code) {
            case R.string.description:
                data.setDescription(text);
                break;
        }
    }

    @Override
    public void addNewImage(File image){
        data.addImage(image);
    }

    @Override
    public void finishedCreatingPdf() {
        //create email intent
        Emailer emailer = new Emailer(getApplicationContext());
        Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.SITE_INSTRUCTION_CODE, pdfAttatchment, data.getRecipientEmail());

        //start email intent
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
    }

    public void createPdf() {
        Document documentTemplate = Pdf.getTemplate(getApplicationContext(), data.getJobNumber());
        try {
            Document body = Jsoup.parse(getAssets().open("siteInstruction.html"), "utf-8", "http://www.example.com");

            Element site = body.getElementById("site");
            Element siteLocation = body.getElementById("site_location");
            Element recipient = body.getElementById("recipient");
            Element recipientEmail = body.getElementById("recipient_email");
            Element description = body.getElementById("description_list");

            String[] descriptionArray;
            descriptionArray = data.getDescription().split(System.lineSeparator());

            site.text(data.getSite());
            siteLocation.text(data.getLocation());
            recipient.html("<p>" + data.getRecipient() + "</p>");
            recipientEmail.html("<p>" + data.getRecipientEmail() + "</p>");

            //add each bullet from arrays as a <p>
            for (int x = 0; x < descriptionArray.length; x++) {
                String f = "";

                for (String bullet : descriptionArray) {
                    f += "<p>" + bullet + "</p>";
                }

                description.html(f);
            }

            documentTemplate.getElementById("main").html(body.html());
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }

        String filePath = MainActivity.pdf.createFilePath(this, "Site Instruction");

        MainActivity.pdf.createPdfToFile(this, documentTemplate.html(), filePath, data.getImageArray());

        pdfAttatchment = new File(filePath);
    }
}
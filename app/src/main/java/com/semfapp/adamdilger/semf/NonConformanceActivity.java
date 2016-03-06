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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;

public class NonConformanceActivity extends AbstractTabLayoutFragment implements Communicator {

    private NonConformanceData data;
    private File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = NonConformanceData.getInstance();
    }

    @Override
    String setToolbarTitle() {
        return "Non Conformance";
    }

    @Override
    int getFragmentCount() {
        return 3;
    }

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch (index) {
            case 0:
                return new NonConformanceF1();
            case 1:
                return getQuestionFragment(R.string.description);
            case 2:
                return new NonConformanceF3();
            default:
                return null;
        }
    }

    @Override
    public void saveText(String text, int code) {
        switch (code) {
            case R.string.description:
                data.setDescription(text);
                break;
            case R.string.non_conformance_actions:
                data.setActions(text);
                break;
        }
    }

    @Override
    public void addNewImage(File image){}

    @Override
    public void finishedCreatingPdf() {
        //create email intent
        Emailer emailer = new Emailer(getApplicationContext());
        Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.NON_CONFORMANCE_CODE, pdfAttatchment, data.getRecipientEmail(), null);

        //start email intent
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
    }

    public void createPdf() {
        Document document = Pdf.getTemplate(getApplicationContext(), data.getJobNumber());

        try {
            Document body = Jsoup.parse(getAssets().open("nonConformance.html"), "utf-8", "http://www.example.com");

            Element site = body.getElementById("site");
            Element siteLocation = body.getElementById("site_location");
            Element recipient = body.getElementById("recipient");
            Element recipientEmail = body.getElementById("recipient_email");
            Element description = body.getElementById("description_list");
            Element actions = body.getElementById("actions_list");

            String[] descriptionArray, actionsArray;
            descriptionArray = data.getDescription().split(System.lineSeparator());
            actionsArray = data.getActions().split(System.lineSeparator());

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

            for (int x = 0; x < actionsArray.length; x++) {
                String f = "";

                for (String bullet : actionsArray) {
                    f += "<p>" + bullet + "</p>";
                }

                actions.html(f);
            }

            document.getElementById("main").html(body.html());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }

        String filePath = MainActivity.pdf.createFilePath(this, "Non Conformance");

        MainActivity.pdf.createPdfToFile(this, document.html(), filePath, null);

        pdfAttatchment = new File(filePath);
    }
}
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

public class protectPlanActivity extends AbstractTabLayoutFragment implements Communicator {

    private protectPlanData data;
    public File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = protectPlanData.getInstance();
    }

    String setToolbarTitle() {
        return "Protect Plan";
    }

    @Override
    public void saveText(String text, int code) {
        switch (code) {
            case R.string.protect_plan_q1:
                data.setPlan(text);
                break;
            case R.string.protect_plan_q2:
                data.setEffect(text);
                break;
            case R.string.protect_plan_q3:
                data.setWrong(text);
                break;
            case R.string.protect_plan_q4:
                data.setPrevented(text);
                break;
            case R.string.protect_plan_q5:
                data.setRamifications(text);
                break;
        }
    }

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch(index) {
            case 0: return getQuestionFragment(R.string.protect_plan_q1);
            case 1: return getQuestionFragment(R.string.protect_plan_q2);
            case 2: return getQuestionFragment(R.string.protect_plan_q3);
            case 3: return getQuestionFragment(R.string.protect_plan_q4);
            case 4: return new protectPlanF5();
            default: return null;
        }
    }

    @Override
    int getFragmentCount() {
        return 5;
    }

    @Override
    public void addNewImage(File image) {}

    @Override
    public void finishedCreatingPdf() {
        //create email intent
        Emailer emailer = new Emailer(getApplicationContext());
        Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.PROTECT_PLAN_CODE, pdfAttatchment, null);

        //start email intent
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
    }

    public void createPdf() {
        Document documentTemplate = null;
        Element body = null;

        try {
            documentTemplate = Pdf.getTemplate(getApplicationContext(),null);

            body = Jsoup.parse(getAssets().open("protectPlan.html"), "utf-8", "http://www.example.com");

            Elements lists = body.select(".list_box");          //Lists html Elements
            ArrayList<String[]> arrayList = data.getArray();    //editText string arrays

            //for each Element in lists, add each bullet from arrayList.string[] as a <p>
            for (int x = 0; x < lists.size(); x++) {
                String f = "";

                for (String bullet : arrayList.get(x)) {
                    f += "<p>" + bullet + "</p>";
                }

                lists.get(x).html(f);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
        }

        documentTemplate.getElementById("main").html(body.html());

        String filePath = MainActivity.pdf.createFilePath(this, "Protect Plan");

        MainActivity.pdf.createPdfToFile(this, documentTemplate.html(), filePath, null);

        pdfAttatchment = new File(filePath);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        data.exitDelete();
    }
}
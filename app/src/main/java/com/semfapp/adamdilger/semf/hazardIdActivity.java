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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

public class hazardIdActivity extends AppCompatActivity implements Communicator {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private HazardIdData data;
    public File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hazard Identification");
        setSupportActionBar(toolbar);

        data = HazardIdData.getInstance();


        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new hazardIdF1();
                case 1:
                    return getQuestionFragment(R.string.hazard_id_hazard_observed);
                case 2:
                    return getQuestionFragment(R.string.hazard_id_limit_ramifications);
                case 3:
                    return getQuestionFragment(R.string.hazard_id_resolution);
                case 4:
                    return new hazardIdF5();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    private QuestionFragment getQuestionFragment(int questionId) {
        QuestionFragment qf = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(QuestionFragment.QUESTION_TEXT_CODE, questionId);
        qf.setArguments(args);
        return qf;
    }

    @Override
    public void saveText(String text, int code) {
        switch (code) {
            case R.string.hazard_id_hazard_observed:
                data.setHazard(text);
                break;
            case R.string.hazard_id_limit_ramifications:
                data.setRamifications(text);
                break;
            case R.string.hazard_id_resolution:
                data.setResolution(text);
                break;
        }
    }

    @Override
    public void addNewImage(File image) {
        data.addImage(image);
    }

    public void createPdf() {
        Document documentTemplate = Pdf.getTemplate(getApplicationContext(), data.getProjectNumber());

        try {
            Document body = Jsoup.parse(getAssets().open("hazardId.html"), "utf-8", "http://www.example.com");

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

            documentTemplate.getElementById("main").html(body.html());
        } catch (Exception e) {

        }

        Pdf pdf = new Pdf();
        pdfAttatchment = pdf.createPDF(getApplicationContext(), documentTemplate.html(), "Hazard ID", data.getImageArray());
        pdfAttatchment.deleteOnExit();

        /*
        Uri uri = Uri.fromFile(pdfAttatchment);

        SharedPreferences sharedPreferences = getSharedPreferences("userProfileData", MODE_PRIVATE);
        String supEmail = sharedPreferences.getString("supEmail", "default");
        String ccHazard = sharedPreferences.getString("ccHazard", "default");


        if (uri != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {supEmail});
            emailIntent.putExtra(Intent.EXTRA_CC, new String[] {ccHazard});
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
            Toast.makeText(hazardIdActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
        }

        */

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfAttatchment), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
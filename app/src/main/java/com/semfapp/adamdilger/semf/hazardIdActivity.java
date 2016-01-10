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
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

public class hazardIdActivity extends AppCompatActivity implements Communicator {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private HazardIdData data;
    private InputMethodManager imm;
    private File pdfAttatchment;

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

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 4) {
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

    private class ScreenSlidePagerAdapter extends PagerAdapterTemplate {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Submit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createPdf();
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
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

    @Override
    public void finishedCreatingPdf() {
        //create email intent
        Emailer emailer = new Emailer(getApplicationContext());
        Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.HAZARD_ID_CODE, pdfAttatchment, null);

        //start email intent
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
    }

    public void createPdf() {
        Document documentTemplate = Pdf.getTemplate(getApplicationContext(), data.getProjectNumber());

        try {
            Document body = Jsoup.parse(getAssets().open("hazardIdentification.html"), "utf-8", "http://www.example.com");

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

        String filePath = MainActivity.pdf.createFilePath(this, "Hazard ID");

        MainActivity.pdf.createPdfToFile(this, documentTemplate.html(), filePath, null);

        pdfAttatchment = new File(filePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Emailer.EMAILER_REQUEST_CODE
                && resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}
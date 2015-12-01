package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NonConformanceActivity extends AppCompatActivity implements Communicator {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private NonConformanceData data;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Non Conformance");
        setSupportActionBar(toolbar);

        data = NonConformanceData.getInstance();


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
        public int getCount() {
            return 3;
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

        Pdf pdf = new Pdf();
        File file = pdf.createPDF(getApplicationContext(), document.html(), "Non Conformance", null);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
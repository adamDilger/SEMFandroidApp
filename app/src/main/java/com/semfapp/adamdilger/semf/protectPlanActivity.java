package com.semfapp.adamdilger.semf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

public class protectPlanActivity extends AppCompatActivity implements Communicator {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private protectPlanData data;
    public File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Protect Plan");
        setSupportActionBar(toolbar);

        data = protectPlanData.getInstance();

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
            switch(position) {
                case 0: return getQuestionFragment(R.string.protect_plan_q1);
                case 1: return getQuestionFragment(R.string.protect_plan_q2);
                case 2: return getQuestionFragment(R.string.protect_plan_q3);
                case 3: return getQuestionFragment(R.string.protect_plan_q4);
                case 4: return new protectPlanF5();
                default: return null;
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
    public void addNewImage(File image) {}

    public void createPdf() {
        Document documentTemplate = null;
        Element body = null;

        try {
            documentTemplate = Pdf.getTemplate(getApplicationContext(),null);

            body = Jsoup.parse(getAssets().open("protectPlan.html"), "utf-8", "http://www.example.com");

            Elements lists = body.select(".list_box");      //Lists html Elements
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

        Pdf pdf = new Pdf();
        pdfAttatchment = pdf.createPDF(getApplicationContext(), documentTemplate.html(), "Protect Plan", null);

        /*
        Uri uri = Uri.fromFile(pdfAttatchment);

        SharedPreferences sharedPreferences = getSharedPreferences("userProfileData", MODE_PRIVATE);
        String supEmail = sharedPreferences.getString("supEmail", "default");
        String ccHazard = sharedPreferences.getString("ccProtect", "default");


        if (uri != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {supEmail});
            emailIntent.putExtra(Intent.EXTRA_CC, new String[] {ccHazard});
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }
        */

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(pdfAttatchment), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        data.exitDelete();
    }
}
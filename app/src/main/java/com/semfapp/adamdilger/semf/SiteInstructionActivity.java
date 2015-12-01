package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adamdilger on 24/11/2015.
 */
public class SiteInstructionActivity extends AppCompatActivity implements Communicator {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private SiteInstructionData data;
    private InputMethodManager imm;
    public File pdfAttatchment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Site Instruction");
        setSupportActionBar(toolbar);

        data = SiteInstructionData.getInstance();


        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 2){
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
        pdfAttatchment.deleteOnExit();
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
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
        }
    }

    @Override
    public void addNewImage(File image){
        data.addImage(image);
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


        Pdf pdf = new Pdf();
        pdfAttatchment = pdf.createPDF(getApplicationContext(), documentTemplate.html(), "Site Instruction", data.getImageArray());

        /*
        Uri uri = Uri.fromFile(pdfAttatchment);

        SharedPreferences sharedPreferences = getSharedPreferences("userProfileData", MODE_PRIVATE);
        String supEmail = sharedPreferences.getString("supEmail", "default");
        String ccHazard = sharedPreferences.getString("ccSiteInstruction", "default");


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
}
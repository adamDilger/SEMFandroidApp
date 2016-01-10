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
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Take5Activity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private Take5PdfDocument pdfDocument;
    private File pdfAttatchment;
    private InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protect_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Take 5");
        setSupportActionBar(toolbar);

        pdfDocument = new Take5PdfDocument(getApplicationContext());

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1){
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

    private class ScreenSlidePagerAdapter extends PagerAdapterTemplate {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Take5F1();
                case 1:
                    return new Take5F2();
                case 2:
                    return new Take5F3();
                case 3:
                    return new Take5F4();
                case 4:
                    return new Take5F5();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    public void createPdf() {
        PdfDocument document = pdfDocument.createDocument();

        // write the document
        String date = new SimpleDateFormat("dd-MM-yy").format(MainActivity.currentDate);
        pdfAttatchment = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), date + " SWMS.pdf");

        FileOutputStream fos;

        try {
            pdfAttatchment.createNewFile();
            fos = new FileOutputStream(pdfAttatchment);
            document.writeTo(fos);
            document.close();
            fos.close();
        } catch (Exception e) {
            new Toast(getApplicationContext())
                    .makeText(getApplicationContext(),
                            "Error:\n" + e.toString(),
                            Toast.LENGTH_SHORT)
                    .show();
        }

        //create email intent
        Emailer emailer = new Emailer(getApplicationContext());
        Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.TAKE_5_CODE, pdfAttatchment, null);

        //start email intent
        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (pdfAttatchment != null) {
            pdfAttatchment.delete();
        }
        pdfDocument = null;
        Take5Data.exitDelete();
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
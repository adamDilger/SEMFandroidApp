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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Take5Activity extends AbstractTabLayoutFragment {

    private Take5PdfDocument pdfDocument;
    private File pdfAttatchment;
    private Take5Data data;
    private InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = Take5Data.get(this);
        pdfDocument = new Take5PdfDocument(getApplicationContext());

        super.getViewPager().setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
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
        return "Take 5";
    }

    @Override
    int getFragmentCount() {
        return 5;
    }

    @Override
    Fragment getFragmentAtIndex(int index) {
        switch (index) {
            case 0:
                return new Take5F1();
            case 1:
                Fragment fragment = new Take5RecyclerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Take5RecyclerFragment.ARRAY_IDENTIFIER_CODE, Take5RecyclerFragment.SECTION_ONE_CODE);
                fragment.setArguments(bundle);

                return fragment;
            case 2:
                Fragment fragmentTwo = new Take5RecyclerFragment();
                Bundle bundleTwo = new Bundle();
                bundleTwo.putInt(Take5RecyclerFragment.ARRAY_IDENTIFIER_CODE, Take5RecyclerFragment.SECTION_TWO_CODE);
                fragmentTwo.setArguments(bundleTwo);

                return fragmentTwo;
            case 3:
                return new Take5F4();
            case 4:
                Fragment fragmentThree = new Take5RecyclerFragment();
                Bundle bundleThree = new Bundle();
                bundleThree.putInt(Take5RecyclerFragment.ARRAY_IDENTIFIER_CODE, Take5RecyclerFragment.SECTION_THREE_CODE);
                fragmentThree.setArguments(bundleThree);

                return fragmentThree;
            default:
                return null;
        }
    }

    public void createPdf() {

        //if all fields have not been entered, create an alertdialog
        if (data.allFieldsFilled()) {
            PdfDocument document = pdfDocument.createDocument();

            // write the document
            String name = Emailer.getSubject(Emailer.TAKE_5_CODE, data.getEditTexts()[0]);
            pdfAttatchment = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), name + ".pdf");

            FileOutputStream fos;

            try {
//                pdfAttatchment.mkdirs();
                pdfAttatchment.createNewFile();
                fos = new FileOutputStream(pdfAttatchment);
                document.writeTo(fos);
                document.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();

                new Toast(getApplicationContext())
                        .makeText(getApplicationContext(),
                                "Error6:\n" + e.toString(),
                                Toast.LENGTH_SHORT)
                        .show();
            }

            //create email intent
            Emailer emailer = new Emailer(getApplicationContext());
            Intent emailIntent = emailer.emailAttatchmentIntent(Emailer.TAKE_5_CODE, pdfAttatchment, null, name);

            //start email intent
            startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), Emailer.EMAILER_REQUEST_CODE);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Oops..");
            builder.setMessage("Not all fields have been filled");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
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
}
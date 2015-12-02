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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/**
 * Created by adamdilger on 24/11/2015.
 */
public class SiteInstructionF3 extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 2;

    SiteInstructionData data;
    Button takePictureButton, saveButton;
    Communicator comm;
    RecyclerView recyclerView;
    ImageViewerAdapter imageViewerAdapter;
    PhotoLibrary imageFileArray;
    String imageFileCurrentPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = SiteInstructionData.getInstance();
        comm = (Communicator)getActivity();
        imageFileArray = data.getImages();

        if (savedInstanceState != null) {
            imageFileCurrentPath = savedInstanceState.getString("filePath");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_capture_image_gallery, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        imageViewerAdapter = new ImageViewerAdapter(getContext(), imageFileArray);
        recyclerView.setAdapter(imageViewerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        takePictureButton = (Button)v.findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        saveButton = (Button)v.findViewById(R.id.save_pdf_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SiteInstructionActivity)getActivity()).createPdf();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            comm.addNewImage(new File(imageFileCurrentPath));
            imageViewerAdapter.notifyDataSetChanged();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        File photoFile = null;
        try {
            photoFile = data.createImageFile();
            imageFileCurrentPath = photoFile.getPath();
        } catch (Exception ex) {
            Toast.makeText(getActivity().getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            System.out.println(ex.toString());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("filePath", imageFileCurrentPath);
    }
}

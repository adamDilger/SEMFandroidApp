package com.semfapp.adamdilger.semf;

import android.content.Intent;
import android.media.ExifInterface;
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

public class hazardIdF5 extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    HazardIdData data;
    Button takePictureButton, saveButton;
    Communicator comm;
    RecyclerView recyclerView;
    ImageViewerAdapter imageViewerAdapter;
    PhotoLibrary photoLib;
    String imageFileCurrentPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = HazardIdData.getInstance();
        comm = (Communicator)getActivity();
        photoLib = data.getImages();

        if (savedInstanceState != null) {
            imageFileCurrentPath =  savedInstanceState.getString("filePath");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_capture_image_gallery, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        imageViewerAdapter = new ImageViewerAdapter(getContext(), photoLib);
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
                ((hazardIdActivity) getActivity()).createPdf();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

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

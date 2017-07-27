package com.semfapp.adamdilger.semf;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.log.LoggerDefault;

/**
 * Created by adamdilger on 25/7/17.
 */

public class Take5RiskElementActivity extends AppCompatActivity {
    private static final String TAG = "Take5RiskActivity";

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_EDIT_PHOTO = 2;

    static final String POS_EXTRA = "POS EXTRA";
    static final String ONE_EXTRA = "ONE_EXTRA";
    static final String RATING_EXTRA = "RATING_EXTRA";
    static final String TWO_EXTRA = "TWO_EXTRA";
    static final String IMAGE_PATH_EXTRA = "IMAGE_PATH_EXTRA";

    String mImageName;
    String mCurrentPhotoPath;
    boolean isEditing = false;
    boolean pictureTaken = false;

    int mPos = -1;      //if left as -1, risk element is new (being added)
    String mOne = "";
    String mTwo = "";
    String mRating = "";
    TextView toolbarTitle;
    Button cancelButton, saveButton, riskMatrixButton, addPictureButton;
    EditText t1, t3;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take5_riskelement);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        cancelButton = (Button) findViewById(R.id.cancel_action);
        saveButton = (Button) findViewById(R.id.save_action);
        riskMatrixButton = (Button) findViewById(R.id.risk_matrix_button);
        addPictureButton = (Button) findViewById(R.id.add_picture_button);
        t1 = (EditText) findViewById(R.id.element_text_one);
        t3 = (EditText) findViewById(R.id.element_text_two);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        if (getIntent().getExtras() != null) {
            setupForEditing();
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getString(IMAGE_PATH_EXTRA) != null) {
                mCurrentPhotoPath = savedInstanceState.getString(IMAGE_PATH_EXTRA);
                toggleImageTaken(true);
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(RESULT_OK);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    //ask for confirmation

                    AlertDialog.Builder builder = new AlertDialog.Builder(Take5RiskElementActivity.this);
                    builder.setTitle("Delete Item");
                    builder.setMessage("Are you sure you want to delete item?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sendResult(RESULT_FIRST_USER);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    finish();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioLow:
                        mRating = Take5RiskElement.Rating.LOW.toString();
                        break;
                    case R.id.radioMed:
                        mRating = Take5RiskElement.Rating.MED.toString();
                        break;
                    case R.id.radioHigh:
                        mRating = Take5RiskElement.Rating.HIGH.toString();
                        break;
                }
            }
        });

        riskMatrixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Take5RiskMatrixDialog dialog = new Take5RiskMatrixDialog();

                dialog.show(getFragmentManager(), "tag");
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pictureTaken) {
                    Intent i = new Intent(Take5RiskElementActivity.this, Take5ImageViewActivity.class);

                    i.putExtra(Take5ImageViewActivity.IMAGE_PATH_EXTRA, mCurrentPhotoPath);

                    startActivityForResult(i, REQUEST_EDIT_PHOTO);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
    }

    private void setupForEditing() {
        isEditing = true;

        mPos = getIntent().getIntExtra(POS_EXTRA, -1);
        mOne = getIntent().getStringExtra(ONE_EXTRA);
        mRating = getIntent().getStringExtra(RATING_EXTRA);
        mTwo = getIntent().getStringExtra(TWO_EXTRA);
        mCurrentPhotoPath = getIntent().getStringExtra(IMAGE_PATH_EXTRA);

        Log.d(TAG, "onCreate: " + String.format("pos: %d, one: %s, rating: %s, two: %s, image: %s", mPos, mOne, mRating, mTwo, mCurrentPhotoPath));

        toolbarTitle.setText("Edit Item");

        cancelButton.setText("Delete");
        t1.setText(mOne);
        t3.setText(mTwo);

        Take5RiskElement.Rating rating = Take5RiskElement.Rating.valueOf(mRating);

        switch (rating) {
            case LOW:
                radioGroup.check(R.id.radioLow);
                break;
            case MED:
                radioGroup.check(R.id.radioMed);
                break;
            case HIGH:
                radioGroup.check(R.id.radioHigh);
                break;
        }

        if (mCurrentPhotoPath != null) {
            toggleImageTaken(true);
        }
    }

    private void toggleImageTaken(boolean taken) {
        if (taken) {
            addPictureButton.setText("Edit Picture");
            pictureTaken = true;
        } else {
            addPictureButton.setText("Add Picture");
            mCurrentPhotoPath = null;
            pictureTaken = false;
        }
    }

    private void sendResult(int resultCode) {

        Intent i = new Intent();

        i.putExtra(POS_EXTRA, mPos);
        i.putExtra(ONE_EXTRA, t1.getText().toString());
        i.putExtra(RATING_EXTRA, mRating);
        i.putExtra(TWO_EXTRA, t3.getText().toString());
        i.putExtra(IMAGE_PATH_EXTRA, mCurrentPhotoPath);
        setResult(resultCode, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d(TAG, "onActivityResult: " + String.format("requestCode: %d, resultCode: %d", requestCode, resultCode));

        //System.out.println("mCurrentPhotoPath = " + mCurrentPhotoPath);

        if (resultCode == RESULT_OK) {
            toggleImageTaken(true);

            try {
                //File old = new File(mCurrentPhotoPath);
                //Log.d(TAG, "onActivityResult: ORIGINAL SIZE: " + old.length() + " | " + old.getAbsolutePath());

                //long startTime = System.currentTimeMillis();

                Bitmap original = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Bitmap scaled = Bitmap.createScaledBitmap(original, original.getWidth()/2, original.getHeight()/2, false);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                Bitmap rotated = Bitmap.createBitmap(scaled, 0,0,scaled.getWidth(), scaled.getHeight(), matrix, false);

                //long endTime = System.currentTimeMillis();

                //Log.d(TAG, "onActivityResult: COMPRESS AND ROTATE: " + ((endTime - startTime) / 1000.0) + " seconds");

                //overwrite image with compressed image.
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                rotated.compress(Bitmap.CompressFormat.JPEG, 60, byteStream);


                mCurrentPhotoPath = getFilesDir() + mImageName;
                FileOutputStream stream = new FileOutputStream(mCurrentPhotoPath);
                stream.write(byteStream.toByteArray());
                stream.close();

                //File newFile = new File(mCurrentPhotoPath);
                //Log.d(TAG, "onActivityResult: ORIGINAL SIZE: " + newFile.length() + " | " + newFile.getAbsolutePath() + "\n\n");

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_FIRST_USER) {
            toggleImageTaken(false);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
                System.out.println(ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));

                //mCurrentPhotoPath = photoFile.getPath();

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                mImageName,     /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getPath();
        return image;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(IMAGE_PATH_EXTRA, mCurrentPhotoPath);
    }
}

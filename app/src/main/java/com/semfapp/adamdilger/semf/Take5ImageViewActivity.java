package com.semfapp.adamdilger.semf;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by adamdilger on 26/7/17.
 */

public class Take5ImageViewActivity extends AppCompatActivity {

    public static String IMAGE_PATH_EXTRA = "IMAGE_PATH_EXTRA";

    private Button deleteButton, saveButton;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take5_imageview);

        deleteButton = (Button) findViewById(R.id.delete_action);
        saveButton = (Button) findViewById(R.id.save_action);
        imageView = (ImageView) findViewById(R.id.image_view);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask for confirmation

                AlertDialog.Builder builder = new AlertDialog.Builder(Take5ImageViewActivity.this);
                builder.setTitle("Delete Picture");
                builder.setMessage("Are you sure you want to delete picture?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePicture();
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
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
//            String imagePath = getIntent().getStringExtra(IMAGE_PATH_EXTRA);
//
//            Bitmap b = BitmapFactory.decodeFile(imagePath);
//
//            Matrix matrix = new Matrix();
//            matrix.postRotate(90);
//
//            imageView.setImageBitmap(Bitmap.createBitmap(b, 0,0,b.getWidth(), b.getHeight(), matrix, false));

            String imagePath = getIntent().getStringExtra(IMAGE_PATH_EXTRA);
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } catch (NullPointerException e) {
            e.printStackTrace();
            finish();
        }
    }

    private void deletePicture() {
        setResult(RESULT_FIRST_USER);
        finish();
    }
}

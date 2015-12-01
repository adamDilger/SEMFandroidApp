package com.semfapp.adamdilger.semf;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by adamdilger on 20/11/2015.
 */
public class ImageViewerAdapter extends RecyclerView.Adapter<ImageViewerAdapter.MyViewHolder> {

    private LayoutInflater layoutInflator;
    private PhotoLibrary dataArray;
    private Context context;


    public ImageViewerAdapter(Context context, PhotoLibrary array) {
        layoutInflator = LayoutInflater.from(context);
        this.context = context;
        dataArray = array;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflator.inflate(R.layout.recycler_image_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ImageFile current = dataArray.getImageFile(position);
        final int pos = position;   //has to be final for some reason

        holder.imageView.setImageBitmap(current.getBitmap());
        holder.textView.setText("Image: " + current.getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Delete Image");
                alertDialog.setMessage("Do you want to delete image?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dataArray.deleteImageFile(pos);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        Button deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            deleteButton = (Button)itemView.findViewById(R.id.list_item_delete_button);
            imageView = (ImageView)itemView.findViewById(R.id.list_item_image);
            textView = (TextView)itemView.findViewById(R.id.list_item_text_view);
        }
    }
}

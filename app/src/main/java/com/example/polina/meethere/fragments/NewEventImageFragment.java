package com.example.polina.meethere.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.polina.meethere.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;


public class NewEventImageFragment extends android.support.v4.app.Fragment {


    private static int RESULT_LOAD_IMAGE = 109;
    private ImageView imageView;
    private ImageButton imageButton;

    public static NewEventImageFragment newInstance() {
        NewEventImageFragment fragment = new NewEventImageFragment();

        return fragment;
    }

    public NewEventImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_new_event_image, container, false);
        imageView = (ImageView) v.findViewById(R.id.event_image);
        imageButton = (ImageButton)v.findViewById(R.id.image_button);

        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event_mid, menu);
    }


    public void loadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.err.println("INTENT DATA: " + data + " |||" + data.getExtras());
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            decodeUri(uri);
//            if (uri != null) {
//                String path = Utils.getRealPathFromURI(getContext(), uri);
//                InputStream inputStream = Utils.getThumbnailImage(uri, getContext());
//               String imageURI = S3Helper.uploadImage(path);
//               String miniImageURI = S3Helper.uploadImage(inputStream);
//
//                System.out.println(" image URI" + imageURI + "image mini uri" + miniImageURI);
//
//            }
            imageButton.setAlpha(.5f);


        }
    }


    public  Bitmap getBitMap(){
        return  ((BitmapDrawable)(imageView).getDrawable()).getBitmap();
    }


    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            imageView.setImageBitmap(bitmap);



        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }}
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
import com.example.polina.meethere.model.App;

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
            imageView.setImageBitmap(((App)getActivity().getApplication()).decodeUri(uri));
            imageButton.setAlpha(.5f);


        }
    }


    public  Bitmap getBitMap(){
        return  ((BitmapDrawable)(imageView).getDrawable()).getBitmap();
    }


   }
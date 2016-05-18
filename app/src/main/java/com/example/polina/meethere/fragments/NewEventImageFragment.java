package com.example.polina.meethere.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.R;


public class NewEventImageFragment extends android.support.v4.app.Fragment {

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
        return inflater.inflate(R.layout.fragment_new_event_image, container, false);


    }

}

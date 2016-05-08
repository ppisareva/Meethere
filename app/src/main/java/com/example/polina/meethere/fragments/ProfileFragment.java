package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.Adapters.SimpleItem;
import com.example.polina.meethere.R;
import com.example.polina.meethere.SimpleListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends android.support.v4.app.Fragment {

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }




}

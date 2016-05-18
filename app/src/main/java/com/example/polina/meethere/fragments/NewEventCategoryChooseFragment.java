package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.CategoryChooseAdapter;
import com.example.polina.meethere.R;

import java.util.Arrays;
import java.util.List;


public class NewEventCategoryChooseFragment extends android.support.v4.app.Fragment {

    public static NewEventCategoryChooseFragment newInstance() {
        NewEventCategoryChooseFragment fragment = new NewEventCategoryChooseFragment();

        return fragment;
    }

    public NewEventCategoryChooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.fragment_category_choose, container, false);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.category_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String[] myResArray = getResources().getStringArray(R.array.category);
        List<String> myResArrayList = Arrays.asList(myResArray);
        CategoryChooseAdapter adapter = new CategoryChooseAdapter(myResArrayList);
        recyclerView.setAdapter(adapter);
        return v;
    }


}

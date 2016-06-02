package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.CategoryChooseAdapter;
import com.example.polina.meethere.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class NewEventCategoryChooseFragment extends android.support.v4.app.Fragment {

    CategoryChooseAdapter adapter;

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
        setHasOptionsMenu(true);
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.category_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String[] myResArray = getResources().getStringArray(R.array.category);
        List<String> myResArrayList = Arrays.asList(myResArray);
        adapter = new CategoryChooseAdapter(myResArrayList);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event_mid, menu);
    }

    public Collection<Integer> getTags (){
        return adapter.getTags();
    }

}

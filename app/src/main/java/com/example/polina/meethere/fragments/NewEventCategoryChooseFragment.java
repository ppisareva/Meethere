package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.CategoryChooseAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.model.Event;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NewEventCategoryChooseFragment extends android.support.v4.app.Fragment {

    CategoryChooseAdapter adapter;
    int[] category;

    public static NewEventCategoryChooseFragment newInstance(int[] category) {
        NewEventCategoryChooseFragment fragment = new NewEventCategoryChooseFragment();
        Bundle b = new Bundle();
        b.putIntArray(Event.TAGS, category);
        fragment.setArguments(b);

        return fragment;
    }

    public NewEventCategoryChooseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            category = getArguments().getIntArray(Event.TAGS);
        }

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
        Set<Integer> checked = new HashSet<>();
        if(category!=null){
        for(int i = 0; i<category.length; i++){
            checked.add(category[i]);
        }
        }
        adapter = new CategoryChooseAdapter(myResArrayList,checked);
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

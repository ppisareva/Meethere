package com.tolpa.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tolpa.R;
import com.tolpa.adapters.SimpleListAdapter;
import com.tolpa.Utils;


public class CategoryFragment extends android.support.v4.app.Fragment{

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();

        return fragment;
    }

    public CategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        SimpleListAdapter listAdapter = new SimpleListAdapter(Utils.getAllCategory(getActivity()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.all_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdapter);
        return view;
    }

}

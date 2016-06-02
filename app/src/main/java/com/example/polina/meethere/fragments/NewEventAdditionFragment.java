package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.polina.meethere.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link NewEventAdditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventAdditionFragment extends android.support.v4.app.Fragment {


    Spinner spinnerAgeFrom;
    Spinner spinnerAgeTo;
    Spinner spinnerBudgetFrom;
    Spinner spinnerBudgetTo;


    public static NewEventAdditionFragment newInstance() {
        NewEventAdditionFragment fragment = new NewEventAdditionFragment();

        return fragment;
    }

    public NewEventAdditionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event_adition, container, false);
        setHasOptionsMenu(true);
        spinnerAgeFrom = (Spinner) v.findViewById(R.id.spinner_age_from);
        spinnerAgeTo = (Spinner) v.findViewById(R.id.spinner_age_to);
        spinnerBudgetFrom = (Spinner) v.findViewById(R.id.spinner_budget_from);
        spinnerBudgetTo = (Spinner) v.findViewById(R.id.spinner_budget_to);

        List<Integer> listAge = new ArrayList<>();

        for (int i = 15; i <= 45; i++) {
            listAge.add(i);
        }


        List<Integer> listBudget = new ArrayList<>();
        for (int i = 10; i <= 2000; ) {
            listBudget.add((i));
            i=i+10;
        }


        ArrayAdapter<CharSequence> adapterAge = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listAge);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterBudget = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listBudget);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeFrom.setAdapter(adapterAge);
        spinnerAgeTo.setAdapter(adapterAge);
        spinnerBudgetFrom.setAdapter(adapterBudget);
        spinnerBudgetTo.setAdapter(adapterBudget);
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.new_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public int getMinAge() {
        return Integer.parseInt(spinnerAgeFrom.getSelectedItem().toString());
    }

    public int getMaxAge() {
        return Integer.parseInt(spinnerAgeTo.getSelectedItem().toString());
    }

    public int getMinBudget() {
        return Integer.parseInt(spinnerBudgetFrom.getSelectedItem().toString());
    }

    public int getMaxBudget() {
        return Integer.parseInt(spinnerBudgetTo.getSelectedItem().toString());
    }
}

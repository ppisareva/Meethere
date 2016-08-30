package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.polina.meethere.R;
import com.example.polina.meethere.model.Event;

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
    int min;
    int max;


    public static NewEventAdditionFragment newInstance(int min, int max) {
        NewEventAdditionFragment fragment = new NewEventAdditionFragment();
        Bundle b  = new Bundle();
        b.putInt(Event.BUDGET_MIN, min);
        b.putInt(Event.BUDGET_MAX, max);
        fragment.setArguments(b);
        return fragment;
    }

    public NewEventAdditionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            min = getArguments().getInt(Event.BUDGET_MIN);
            max = getArguments().getInt(Event.BUDGET_MAX);
        }
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

        List<String> listAge = new ArrayList<>();
        listAge.add("");

        for (int i = 15; i <= 45; i++) {
            listAge.add(i+"");
        }


        List<String> listBudget = new ArrayList<>();
        listBudget.add("");
        for (int i = 10; i <= 2000; ) {
            listBudget.add((i+""));
            i=i+10;
        }


        ArrayAdapter<CharSequence> adapterAge = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listAge);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterBudget = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listBudget);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeFrom.setAdapter(adapterAge);
        spinnerAgeTo.setAdapter(adapterAge);
        spinnerBudgetFrom.setAdapter(adapterBudget);
        spinnerBudgetFrom.setSelection(0);
        spinnerBudgetTo.setAdapter(adapterBudget);
        spinnerBudgetTo.setSelection(0);

        if(min!=0){
            int p = listBudget.indexOf(min+"");
            spinnerBudgetFrom.setSelection(p);
        }
        if(max!=0){
            spinnerBudgetTo.setSelection(listBudget.indexOf(max+""));
        }

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.new_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public int getMinAge() {
        String s = spinnerAgeFrom.getSelectedItem().toString();
        if(s.isEmpty())return -1;
        return Integer.parseInt(s);
    }

    public int getMaxAge() {
        String s = spinnerAgeTo.getSelectedItem().toString();
        if(s.isEmpty())return -1;
        return Integer.parseInt(s);
    }

    public int getMinBudget() {
        if(spinnerBudgetFrom.getSelectedItem()=="") return 0;
        if(spinnerBudgetTo.getSelectedItem()!=""&spinnerBudgetFrom.getSelectedItem()=="") return Integer.parseInt(spinnerBudgetTo.getSelectedItem().toString());
        return Integer.parseInt(spinnerBudgetFrom.getSelectedItem().toString());
    }

    public int getMaxBudget() {
        if(spinnerBudgetTo.getSelectedItem()=="") return 0;
        if(spinnerBudgetFrom.getSelectedItem()!=""&spinnerBudgetTo.getSelectedItem()=="") return Integer.parseInt(spinnerBudgetFrom.getSelectedItem().toString());

        return Integer.parseInt(spinnerBudgetTo.getSelectedItem().toString());
    }
}

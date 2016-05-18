package com.example.polina.meethere.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
 * {@link NewEventAditionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEventAditionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventAditionFragment extends android.support.v4.app.Fragment {

    public static NewEventAditionFragment newInstance() {
        NewEventAditionFragment fragment = new NewEventAditionFragment();

        return fragment;
    }

    public NewEventAditionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event_adition, container, false);
        setHasOptionsMenu(true);
        Spinner spinnerAgeFrom = (Spinner) v.findViewById(R.id.spinner_age_from);
        Spinner spinnerAgeTo = (Spinner) v.findViewById(R.id.spinner_age_to);
        Spinner spinnerBudgetFrom = (Spinner) v.findViewById(R.id.spinner_budget_from);
        Spinner spinnerBudgetTo = (Spinner) v.findViewById(R.id.spinner_budget_to);

        List<String> listAge = new ArrayList<>();
        listAge.add(">15");
        for (int i=15; i<=45; i++){
            listAge.add(i+"");
        }
        listAge.add("<45");

        List<String> listBudget = new ArrayList<>();
        for (int i=10; i<=2000; i++){
            listBudget.add((i+10)+"");
        }
        listBudget.add("<2000");

        ArrayAdapter<CharSequence> adapterAge = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listAge );
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapterBudget = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,listBudget );
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeFrom.setAdapter(adapterAge);
        spinnerAgeTo.setAdapter(adapterAge);
        spinnerBudgetFrom.setAdapter(adapterBudget);
        spinnerBudgetTo.setAdapter(adapterBudget);
        return  v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.new_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}

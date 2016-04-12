package com.example.polina.meethere.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.example.polina.meethere.CurrencyAdapter;
import com.example.polina.meethere.MainActivity;
import com.example.polina.meethere.R;

public class FindEventFragment extends android.support.v4.app.Fragment {

    Button start;
    Button end;
    RangeBar rangeBarAge;
    RangeBar rangeBarBudget;
    TextView rightIndexAge;
    TextView leftIndexAge;
    TextView rightIndexBudget;
    TextView leftIndexBudget;
    public static final int MIN_AGE = 15;
    public static final int MIN_BUDGET = 10;

    public static FindEventFragment newInstance() {
        FindEventFragment fragment = new FindEventFragment();
        return fragment;
    }

    public FindEventFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v = inflater.inflate(R.layout.fragment_find_event, container, false);
        setHasOptionsMenu(true);


        Spinner spinnerCategory = (Spinner) v.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        Spinner spinnerSort = (Spinner) v.findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSort);

        start = (Button) v.findViewById(R.id.button_from);
        end = (Button) v.findViewById(R.id.button_till);
        rangeBarAge =(RangeBar) v.findViewById(R.id.range_bar_ege);
        rangeBarBudget = (RangeBar) v.findViewById(R.id.range_bar_budget);
        leftIndexAge = (TextView)v. findViewById(R.id.leftIndexValue);
        leftIndexBudget = (TextView)v. findViewById(R.id.leftIndexValue_budget);
        rightIndexAge = (TextView)v. findViewById(R.id.rightIndexValue);
        rightIndexBudget = (TextView)v. findViewById(R.id.rightIndexValue_budget);



        rangeBarAge.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int left, int right) {

                leftIndexAge.setText("" + (MIN_AGE + left));
                rightIndexAge.setText("" + (MIN_AGE + 1 + right));
                System.out.println(rangeBarAge.getRightIndex());

            }
        });

        rangeBarBudget.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int left, int right) {
                leftIndexBudget.setText("" + (MIN_BUDGET + left));
                rightIndexBudget.setText("" + (MIN_BUDGET + 1 + right));
            }
        });


        rangeBarAge.setTickHeight(0);
        rangeBarBudget.setTickHeight(0);
        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_new_event){
            startActivity(new Intent(getActivity(), MainActivity.class));
            System.out.println(" olololololol--------------------------olololololol");
        }
        return super.onOptionsItemSelected(item);
    }

}

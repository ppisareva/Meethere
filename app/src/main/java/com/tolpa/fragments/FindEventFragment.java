package com.tolpa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.tolpa.activities.MainActivity;
import com.tolpa.R;
import com.tolpa.Utils;

public class FindEventFragment extends android.support.v4.app.Fragment {

    private static final int MIN_DIST = 500;

    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;
    RangeBar rangeBarAge;
    RangeBar rangeBarBudget;
    RangeBar rangeBarDistance;
    TextView rightIndexDistance;
    TextView rightIndexAge;
    TextView leftIndexDictance;
    TextView leftIndexAge;
    TextView rightIndexBudget;
    TextView leftIndexBudget;
    CheckBox checkBoxDistance;
    public static final int MIN_AGE = 15;
    public static final int MIN_BUDGET = 50;

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


        startDate = (Button) v.findViewById(R.id.button_from);
        startDate.setText(Utils.getCurrentDate());
        startTime = (Button) v.findViewById(R.id.button_from_time);
        startTime.setText(Utils.getCurrentTime());
        endDate = (Button) v.findViewById(R.id.button_till);
        endDate.setText(Utils.getCurrentTimePlusHour()[0]);
        endTime = (Button) v.findViewById(R.id.button_till_time);
        endTime.setText(Utils.getCurrentTimePlusHour()[1]);
        rangeBarAge =(RangeBar) v.findViewById(R.id.range_bar_ege);
        rangeBarBudget = (RangeBar) v.findViewById(R.id.range_bar_budget);
        rangeBarDistance = (RangeBar) v.findViewById(R.id.range_bar_distance);
        checkBoxDistance = (CheckBox)v.findViewById(R.id.checkBox_distance);
        leftIndexAge = (TextView)v. findViewById(R.id.leftIndexValue);
        leftIndexDictance = (TextView)v. findViewById(R.id.leftIndexValue_distance);
        leftIndexBudget = (TextView)v. findViewById(R.id.leftIndexValue_budget);
        rightIndexAge = (TextView)v. findViewById(R.id.rightIndexValue);
        rightIndexDistance = (TextView)v. findViewById(R.id.rightIndexValue_distance);
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

                leftIndexBudget.setText("" + (MIN_BUDGET*left));
                rightIndexBudget.setText(""  + MIN_BUDGET*right);
            }
        });

        rangeBarDistance.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                if (i == 0) {
                    leftIndexDictance.setText("" + MIN_DIST);
                    return;
                }
                ;

                leftIndexDictance.setText("" + i * MIN_DIST);
                rightIndexDistance.setText("" + (i1) * MIN_DIST);
            }
        });


        rangeBarAge.setTickHeight(0);
        rangeBarBudget.setTickHeight(0);
        rangeBarDistance.setTickHeight(0);


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
        if(id==R.id.action_new_create){
            startActivity(new Intent(getActivity(), MainActivity.class));
            System.out.println(" olololololol--------------------------olololololol");
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeStartDate(String st){
        startDate.setText(st);
    }
    public void changeTimeStart (String st) {
        startTime.setText(st);
    }
    public void changeEndDate(String st){
        endDate.setText(st);
    }
    public void changeEndTime(String st){
        endTime.setText(st);
    }

}

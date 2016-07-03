package com.example.polina.meethere.fragments;

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
import android.widget.Spinner;
import android.widget.TextView;
import com.edmodo.rangebar.RangeBar;
import com.example.polina.meethere.activities.MainActivity;
import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

public class NewEventFragment extends android.support.v4.app.Fragment{

    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;
    RangeBar rangeBarAge;
    RangeBar rangeBarBudget;
    TextView rightIndexAge;
    TextView leftIndexAge;
    TextView rightIndexBudget;
    TextView leftIndexBudget;
    public static final int MIN_AGE = 15;
    public static final int MIN_BUDGET = 10;


    public static NewEventFragment newInstance() {
        NewEventFragment fragment = new NewEventFragment();
        return fragment;
    }

    public NewEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_event, container, false);
        setHasOptionsMenu(true);

        Spinner spinnerCategory = (Spinner) v.findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);


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

    public void getData(){
        System.out.println("trololol");
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

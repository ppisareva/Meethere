package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

import java.util.ArrayList;
import java.util.List;


public class NewEventTimePickerFragment extends android.support.v4.app.Fragment {

    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;
    CheckBox repeated;
    LinearLayout layout;
    LinearLayout layoutEnd;
    LinearLayout layoutRepeat;
    Spinner spinner;


    public static NewEventTimePickerFragment newInstance() {
        NewEventTimePickerFragment fragment = new NewEventTimePickerFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_new_event_tine_picker, container, false);
        setHasOptionsMenu(true);
        startDate = (Button) v.findViewById(R.id.button_from);
        startDate.setText(Utils.getCurrentDate());
        startTime = (Button) v.findViewById(R.id.button_from_time);
        startTime.setText(Utils.getCurrentTime());
        endDate = (Button) v.findViewById(R.id.button_till);
        endDate.setText(Utils.getCurrentTimePlusHour()[0]);
        endTime = (Button) v.findViewById(R.id.button_till_time);
        endTime.setText(Utils.getCurrentTimePlusHour()[1]);
        layout =(LinearLayout) v.findViewById(R.id.day_of_week);
        layoutEnd = (LinearLayout)v.findViewById(R.id.linearLayout_end);
        layoutRepeat = (LinearLayout)v.findViewById(R.id.linearLayout_repeat);
        repeated = (CheckBox) v.findViewById(R.id.repeated_event);
        spinner = (Spinner)v.findViewById(R.id.spinner);


        List<String> list = new ArrayList<>();

        for (int i=1; i<=36; i++){
            list.add(i+"");
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        repeated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layout.setVisibility(View.VISIBLE);
                    layoutRepeat.setVisibility(View.VISIBLE);
                    endDate.setVisibility(View.INVISIBLE);



                } else {
                    layout.setVisibility(View.GONE);
                    layoutRepeat.setVisibility(View.GONE);
                    endDate.setVisibility(View.VISIBLE);
                }
            }
        });

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event_mid, menu);
    }


    public String getEndDateString() {
        return Utils.convertDateToISO(endDate.getText() + " " + endTime.getText());
    }

    public String getStartDateString() {
        return Utils.convertDateToISO(startDate.getText() + " " + startTime.getText());
    }
}

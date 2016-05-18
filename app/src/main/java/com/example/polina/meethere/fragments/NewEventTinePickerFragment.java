package com.example.polina.meethere.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewEventTinePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEventTinePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventTinePickerFragment extends android.support.v4.app.Fragment {

    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;
    CheckBox repeated;
    LinearLayout layout;
    LinearLayout layoutEnd;

    public static NewEventTinePickerFragment newInstance() {
        NewEventTinePickerFragment fragment = new NewEventTinePickerFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_new_event_tine_picker, container, false);
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
        repeated = (CheckBox) v.findViewById(R.id.repeated_event);

        repeated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layout.setVisibility(View.VISIBLE);
                    endDate.setVisibility(View.INVISIBLE);



                } else {
                    layout.setVisibility(View.GONE);
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



}

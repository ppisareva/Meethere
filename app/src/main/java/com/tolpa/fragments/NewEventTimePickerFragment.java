package com.tolpa.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    TextView weeks;
    String start;
    String end;


    public static NewEventTimePickerFragment newInstance(
            String start, String end
    ) {
        NewEventTimePickerFragment fragment = new NewEventTimePickerFragment();
        Bundle b = new Bundle();
        b.putString(Event.START, start);
        b.putString(Event.END, end);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            start = getArguments().getString(Event.START);
            end = getArguments().getString(Event.END);
        }
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
        weeks = (TextView)v.findViewById(R.id.weeks_amount);
        SimpleDateFormat dateFromServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateToView =  new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeToView =  new SimpleDateFormat("HH:mm");
        Date date = null;
        if(start!=null){
            try{
                date = dateFromServer.parse(start);
                startDate.setText(dateToView.format(date));
                startTime.setText(timeToView.format(date));
                date = dateFromServer.parse(end);
                endDate.setText(dateToView.format(date));
                endTime.setText(timeToView.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        List<String> list = new ArrayList<>();

        for (int i=1; i<=32; i++){
            list.add(i+"");
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                weeks.setText(getResources().getString(R.string.five_weeks));
                if(position==0||position==20||position==30) {weeks.setText(getResources().getString(R.string.one_week));}
                if(position==1||position==2||position==3||position==21||position==22||position==23||position==31)weeks.setText(getResources().getString(R.string.two_weeks));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
        endDate.setText(st);

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

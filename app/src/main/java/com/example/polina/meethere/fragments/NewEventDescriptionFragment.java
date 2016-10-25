package com.example.polina.meethere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewEventDescriptionFragment extends android.support.v4.app.Fragment{
    EditText name;
    EditText description;
    Bundle b;
    int min;
    int max;
    String start;
    String end;
    Spinner spinnerBudgetFrom;
    Spinner spinnerBudgetTo;
    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;


    public static NewEventDescriptionFragment newInstance(String name, String description, int min, int max, String start, String end) {
        NewEventDescriptionFragment fragment = new NewEventDescriptionFragment();
        Bundle bundle =  new Bundle();
        bundle.putString(Event.NAME, name);
        bundle.putString(Event.DESCRIPTION, description);
        bundle.putInt(Event.BUDGET_MIN, min);
        bundle.putInt(Event.BUDGET_MAX, max);
        bundle.putString(Event.START, start);
        bundle.putString(Event.END, end);
        fragment.setArguments(bundle);
        return fragment;
    }

    public NewEventDescriptionFragment() {
        // Required empty public constructor
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                TextView view = (TextView) v;
                String text = view.getText().toString();
                if (text.matches("")) {
                    view.setHint("Должно быть заполненно");
                    view.setHintTextColor(getResources().getColor(R.color.red));
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_asterisk, 0);
                    return;
                } else {
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }


            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getArguments();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event_description, container, false);
        setHasOptionsMenu(true);
        name = (EditText)v.findViewById(R.id.edit_name);
        name.setOnFocusChangeListener(onFocusChangeListener);
        description =(EditText) v.findViewById(R.id.edit_description);
        spinnerBudgetFrom = (Spinner) v.findViewById(R.id.spinner_budget_from);
        spinnerBudgetTo = (Spinner) v.findViewById(R.id.spinner_budget_to);
        startDate = (Button) v.findViewById(R.id.button_from);
        startDate.setText(Utils.getCurrentDate());
        startTime = (Button) v.findViewById(R.id.button_from_time);
        startTime.setText(Utils.getCurrentTime());
        endDate = (Button) v.findViewById(R.id.button_till);
        endDate.setText(Utils.getCurrentTimePlusHour()[0]);
        endTime = (Button) v.findViewById(R.id.button_till_time);
        endTime.setText(Utils.getCurrentTimePlusHour()[1]);


        List<String> listBudget = new ArrayList<>();
        listBudget.add("");
        for (int i = 0; i <= 2000; ) {
            listBudget.add((i+""));
            i=i+10;
        }
        ArrayAdapter<CharSequence> adapterBudget = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, listBudget);
        spinnerBudgetFrom.setAdapter(adapterBudget);
        spinnerBudgetFrom.setSelection(0);
        spinnerBudgetTo.setAdapter(adapterBudget);
        spinnerBudgetTo.setSelection(0);
        if(b!=null) {
            name.setText(b.getString(Event.NAME));
            description.setText(b.getString(Event.DESCRIPTION));
            min = getArguments().getInt(Event.BUDGET_MIN);
            max = getArguments().getInt(Event.BUDGET_MAX);
            start = getArguments().getString(Event.START);
            end = getArguments().getString(Event.END);
            if(min!=0){
                int p = listBudget.indexOf(min+"");
                spinnerBudgetFrom.setSelection(p);
            }
            if(max!=0){
                spinnerBudgetTo.setSelection(listBudget.indexOf(max+""));
            }
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


        }

        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        description.setImeOptions(EditorInfo.IME_ACTION_DONE);
        description.setOnFocusChangeListener(onFocusChangeListener);
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

    public String getName (){
        return name.getText().toString();
    }

    public String getDescription (){
        return description.getText().toString();
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

    public String getEndDateString() {
        return Utils.convertDateToISO(endDate.getText() + " " + endTime.getText());
    }

    public String getStartDateString() {
        return Utils.convertDateToISO(startDate.getText() + " " + startTime.getText());
    }

}

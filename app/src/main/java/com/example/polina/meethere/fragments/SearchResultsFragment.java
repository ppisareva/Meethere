package com.example.polina.meethere.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.edmodo.rangebar.RangeBar;
import com.example.polina.meethere.Adapters.SimpleItem;
import com.example.polina.meethere.R;
import com.example.polina.meethere.SimpleListAdapter;
import com.example.polina.meethere.Utils;

import java.util.ArrayList;
import java.util.List;


public class SearchResultsFragment extends android.support.v4.app.Fragment {

    RecyclerView listResults;
   static List<SimpleItem> itemList = new ArrayList<>();
    SimpleListAdapter adapter;
    LinearLayout layout;
    Button startDate;
    Button startTime;
    Button endDate;
    Button endTime;
    RangeBar rangeBarAge;
    RangeBar rangeBarBudget;
    TextView rightIndexDistance;
    TextView rightIndexAge;
    TextView leftIndexDictance;
    TextView leftIndexAge;
    TextView rightIndexBudget;
    TextView leftIndexBudget;
    public static final int MIN_AGE = 15;
    public static final int MIN_BUDGET = 50;

    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();


        return fragment;
    }

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search_results, container, false);

        listResults = (RecyclerView) v.findViewById(R.id.search_results);
        layout = (LinearLayout) v.findViewById(R.id.advanced_search);
        listResults.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        itemList.addAll(Utils.getAllCategory(getActivity()));
        adapter = new SimpleListAdapter(itemList);

        listResults.setAdapter(adapter);



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



        rangeBarAge.setTickHeight(0);
        rangeBarBudget.setTickHeight(0);

        return v;


    }

    public void highAdditionalParameters(){
        layout.setVisibility(View.GONE);
    }

    public void changeList(SimpleItem item){

        itemList.add(new SimpleItem(R.drawable.ic_android, item.getName()));
            adapter.notifyDataSetChanged();
    }
    public void changeList (List<SimpleItem> list){
        itemList.clear();
        itemList.addAll(list);
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public void clear(){
        itemList.clear();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }


    public void showAdvancedSearch() {
        layout.setVisibility(View.VISIBLE);

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
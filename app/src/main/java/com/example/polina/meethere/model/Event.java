package com.example.polina.meethere.model;

import android.widget.ImageView;

import java.util.List;

/**
 * Created by polina on 24.05.16.
 */
public class Event  {


    public static final String NAME = "name";
    public static final String DESCRIPTION =  "description";
    public static final String  START="start";
    public static final String END =  "end";
    public static final String   PLACE =     "place";
    public static final String  ADDRESS   =  "address";
    public static final String   AGE_MIN=     "age_min";
    public static final String    AGE_MAX =   "age_max";
    public static final String   BUDGET_MIN=     "budget_min";
    public static final String    BUDGET_MAX =  "budget_max";
    public static final String   TAGS =     "tags";

    String name;
    String description;
    String start;
    String end;
    List<Double> place;
    String address;
    int ageMin;
    int ageMax;
    int budgetMin;
    int budgetMax;
    List<Integer> tag;


}

package com.example.polina.meethere;

import android.content.Context;
import android.content.res.TypedArray;

import com.example.polina.meethere.Adapters.SimpleItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by polina on 10.03.16.
 */
public class Utils {
    public static final int FRAGMENT_PAST_EVENTS = 0;
    public static final int FRAGMENT_FUTURE_EVENTS = 1;
    public static final int FRAGMENT_CREATED_BY_ME_EVENTS = 2;

    public static String getCurrentTime( ){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());

    }

    public static String[]  getCurrentTimePlusHour(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);

        return new String []{new SimpleDateFormat("dd/MM/yy").format(c.getTime()), new SimpleDateFormat("HH:mm").format(c.getTime())};

    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());
    }


    public static List<SimpleItem> getAllCategory(Context context) {
        List<SimpleItem> allCategory= new ArrayList<>();
        String arr [] = context.getResources().getStringArray(R.array.category);
       TypedArray image = context.getResources().obtainTypedArray(R.array.category_images);
        for(int i =0; i<arr.length; i++){
            allCategory.add(new SimpleItem(image.getResourceId(i+1, R.drawable.ic_android), arr[i]));
        }
        return allCategory;
    }

}

package com.example.polina.meethere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.polina.meethere.fragments.NewEventAditionFragment;
import com.example.polina.meethere.fragments.NewEventCategoryChooseFragment;
import com.example.polina.meethere.fragments.NewEventDescriptionFragment;
import com.example.polina.meethere.fragments.NewEventImageFragment;
import com.example.polina.meethere.fragments.NewEventLocationFragment;
import com.example.polina.meethere.fragments.NewEventTinePickerFragment;
import com.example.polina.meethere.fragments.ProfileFragment;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {
    NewEventDescriptionFragment newEventDescriprionFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    NewEventTinePickerFragment newEventTinePickerFragment;
    NewEventImageFragment newEventImageFragment;
    NewEventLocationFragment locationFragment;

    public final int FRAGMENT_TIME = 2;
    public final int FRAGMENT_CATEGORY = 1;
    public final int FRAGMENT_DESCTIPTION = 0;
    public final int FRAGMENT_PHOTO = 3;
    public final int FRAGMENT_LOCATION = 4;
    public final int FRAGMENT_ADITION = 5;

    private static final int START_DATE = 202;

    private static final int START_TIME = 122;

    private static final int END_DATE = 200;
    private static final int END_TIME = 232;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(mViewPager);

     //   mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            System.out.println(position);
            switch (position){
                case FRAGMENT_TIME:
                    newEventTinePickerFragment = NewEventTinePickerFragment.newInstance();

                    return newEventTinePickerFragment;
                case  FRAGMENT_CATEGORY:

                    return NewEventCategoryChooseFragment.newInstance();
                case FRAGMENT_DESCTIPTION:
                    newEventDescriprionFragment = NewEventDescriptionFragment.newInstance();
                    return newEventDescriprionFragment;
                case FRAGMENT_PHOTO:
                    newEventImageFragment = NewEventImageFragment.newInstance();

                    return newEventImageFragment;
                case FRAGMENT_LOCATION:
                    locationFragment = NewEventLocationFragment.newInstance();
                    return locationFragment;
                case FRAGMENT_ADITION:

                    return NewEventAditionFragment.newInstance();
            }
            return null;


        }

        @Override
        public int getCount() {
            return 6;
        }


    }

    public void onStartDate(View v) {
        chooseTimeDialog(START_DATE);
    }

    public void onStartTime(View v) {
        chooseTimeDialog(START_TIME);
    }

    public void onAndDate(View v) {
        chooseTimeDialog(END_DATE);
    }

    public void onAndTime(View v) {
        chooseTimeDialog(END_TIME);
    }

    public void onLoadPhoto (View v){

    }



    public void chooseTimeDialog(Integer i) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog;
        TimePickerDialog timePickerDialog;
        switch (i) {
            case START_DATE:
                datePickerDialog = new DatePickerDialog(this, onDateStartListener, year, month, day);
                datePickerDialog.show();
                break;
            case END_DATE:
                datePickerDialog = new DatePickerDialog(this, onDateEndListener, year, month, day);
                datePickerDialog.show();
                break;
            case START_TIME:
                timePickerDialog = new TimePickerDialog(this, onTimeSetListenerStart, hour, minute, true);
                timePickerDialog.show();
                break;
            case END_TIME:
                timePickerDialog = new TimePickerDialog(this, onTimeSetListenerEnd, hour, minute, true);
                timePickerDialog.show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener onDateStartListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            newEventTinePickerFragment.changeStartDate(st);
        }
    };

    DatePickerDialog.OnDateSetListener onDateEndListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            newEventTinePickerFragment.changeEndDate(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventTinePickerFragment.changeTimeStart(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventTinePickerFragment.changeEndTime(st);
        }
    };




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_new_event){
            startActivity(new Intent(this, MainActivity.class));
            System.out.println(" olololololol--------------------------olololololol");
        }
        return super.onOptionsItemSelected(item);
    }



}

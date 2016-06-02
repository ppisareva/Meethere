package com.example.polina.meethere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.polina.meethere.fragments.NewEventAdditionFragment;
import com.example.polina.meethere.fragments.NewEventCategoryChooseFragment;
import com.example.polina.meethere.fragments.NewEventDescriptionFragment;
import com.example.polina.meethere.fragments.NewEventImageFragment;
import com.example.polina.meethere.fragments.NewEventLocationFragment;
import com.example.polina.meethere.fragments.NewEventTimePickerFragment;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.network.NetworkService;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {
    NewEventDescriptionFragment newEventDescriprionFragment;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    NewEventTimePickerFragment newEventTimePickerFragment;
    NewEventImageFragment newEventImageFragment;
    NewEventLocationFragment locationFragment;
    NewEventCategoryChooseFragment newEventCategoryChooseFragment;
    NewEventAdditionFragment additionFragment;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    newEventTimePickerFragment = NewEventTimePickerFragment.newInstance();

                    return newEventTimePickerFragment;
                case  FRAGMENT_CATEGORY:
                    newEventCategoryChooseFragment = NewEventCategoryChooseFragment.newInstance();
                    return newEventCategoryChooseFragment;
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
                    additionFragment = NewEventAdditionFragment.newInstance();
                    return additionFragment;
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

    public void onLoadPhoto(View v) {
       newEventImageFragment.loadImage();
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
            String st = String.format("%d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
            newEventTimePickerFragment.changeStartDate(st);
        }
    };

    DatePickerDialog.OnDateSetListener onDateEndListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = String.format("%d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
            newEventTimePickerFragment.changeEndDate(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventTimePickerFragment.changeTimeStart(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventTimePickerFragment.changeEndTime(st);
        }
    };


    public void moveNext() {
        //it doesn't matter if you're already in the last item
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    public void movePrevious() {
      if(mViewPager.getCurrentItem()==0) finish();
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_new_event){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Event.NAME, newEventDescriprionFragment.getName());
                jsonObject.put(Event.DESCRIPTION, newEventDescriprionFragment.getDescription());
                jsonObject.put(Event.TAGS, new JSONArray(newEventCategoryChooseFragment.getTags()));
                jsonObject.put(Event.START, newEventTimePickerFragment.getStartDateString());
                jsonObject.put(Event.END, newEventTimePickerFragment.getEndDateString());
                if (locationFragment.getLocation()!=null){
                    jsonObject.put(Event.PLACE, new JSONArray(locationFragment.getLocation()));
                }
                jsonObject.put(Event.ADDRESS,locationFragment.getAddress() );
                jsonObject.put(Event.AGE_MAX,additionFragment.getMaxAge() );
                jsonObject.put(Event.AGE_MIN, additionFragment.getMinAge());
                jsonObject.put(Event.BUDGET_MAX, additionFragment.getMaxBudget());
                jsonObject.put(Event.BUDGET_MIN,additionFragment.getMinBudget() );



            } catch (JSONException e) {
                e.printStackTrace();
            }
            LocalBroadcastManager.getInstance(this).registerReceiver(createEventReceiver, new IntentFilter(NetworkService.ACTION_CREATE_EVENT));
            NetworkService.startActionCreateNewEvent(this, jsonObject.toString(),newEventImageFragment.getBitMap());
        }

        if (id == android.R.id.home) {
           movePrevious();
        }

        if (id==R.id.action_forward){
            if(!(mViewPager.getCurrentItem()==5))
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createEventReceiver);
    }

    private BroadcastReceiver createEventReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(NetworkService.STATUS, false);
            if (status) {
                Toast.makeText(context, "New event has been created!", Toast.LENGTH_LONG).show();
                NewEventActivity.this.finish();
            } else {
                Toast.makeText(context, "Can not create event. Try again", Toast.LENGTH_LONG).show();
            }
        }
    };



}

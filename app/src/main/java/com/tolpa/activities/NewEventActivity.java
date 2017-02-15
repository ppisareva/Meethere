package com.tolpa.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.fragments.NewEventCategoryChooseFragment;
import com.tolpa.fragments.NewEventDescriptionFragment;
import com.tolpa.fragments.NewEventImageFragment;
import com.tolpa.fragments.NewEventLocationFragment;
import com.tolpa.model.Event;
import com.tolpa.network.NetworkService;
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
   // NewEventTimePickerFragment newEventTimePickerFragment;
    NewEventImageFragment newEventImageFragment;
    NewEventLocationFragment locationFragment;
    NewEventCategoryChooseFragment newEventCategoryChooseFragment;
 //   NewEventAdditionFragment additionFragment;

  //  public final int FRAGMENT_TIME = 2;
    public final int FRAGMENT_CATEGORY = 1;
    public final int FRAGMENT_DESCTIPTION = 0;
    public final int FRAGMENT_PHOTO = 2;
    public final int FRAGMENT_LOCATION = 3;
  //  public final int FRAGMENT_ADITION = 5;

    private static final int START_DATE = 202;

    private static final int START_TIME = 122;

    private static final int END_DATE = 200;
    private static final int END_TIME = 232;

    String description;
    String name;
    String id;
    String timeStart;
    String timeEnd;
    int budgetMin;
    int budgetMax;
    String address;
    Double lat;
    Double lng;
    int category[];
    String imageUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Intent intent = getIntent();
        if(intent!=null){
            name = intent.getStringExtra(Event.NAME);
            description = intent.getStringExtra(Event.DESCRIPTION);
            category = intent.getIntArrayExtra(Event.TAGS);
            timeStart = intent.getStringExtra(Event.START);
            timeEnd = intent.getStringExtra(Event.END);
            id= intent.getStringExtra(Event.ID);
            address = intent.getStringExtra(Event.PLACE);
            lat = intent.getDoubleExtra(Event.LAT, 0);
            lng = intent.getDoubleExtra(Event.LNG, 0);
            budgetMin = intent.getIntExtra(Event.BUDGET_MIN, 0);
            budgetMax = intent.getIntExtra(Event.BUDGET_MAX, 0);
            imageUrl = intent.getStringExtra(Utils.IMAGE_URL);

        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(mViewPager);

     //   mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission_group.LOCATION},
                        0);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            System.out.println(position);
            switch (position){
//                case FRAGMENT_TIME:
//                    newEventTimePickerFragment = NewEventTimePickerFragment.newInstance(timeStart, timeEnd);
//                    return newEventTimePickerFragment;
                case  FRAGMENT_CATEGORY:
                    newEventCategoryChooseFragment = NewEventCategoryChooseFragment.newInstance(category);
                    return newEventCategoryChooseFragment;
                case FRAGMENT_DESCTIPTION:
                    newEventDescriprionFragment = NewEventDescriptionFragment.newInstance(name,description, budgetMin, budgetMax, timeStart, timeEnd);
                    return newEventDescriprionFragment;
                case FRAGMENT_PHOTO:
                    newEventImageFragment = NewEventImageFragment.newInstance(id, imageUrl);
                    return newEventImageFragment;
                case FRAGMENT_LOCATION:
                    locationFragment = NewEventLocationFragment.newInstance(address, lat, lng);
                    return locationFragment;
//                case FRAGMENT_ADITION:
//                    additionFragment = NewEventAdditionFragment.newInstance(budgetMin, budgetMax);
//                    return additionFragment;
            }
            return null;


        }

        @Override
        public int getCount() {
            return 4;
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
            newEventDescriprionFragment.changeStartDate(st);
        }
    };

    DatePickerDialog.OnDateSetListener onDateEndListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = String.format("%d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
            newEventDescriprionFragment.changeEndDate(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventDescriprionFragment.changeTimeStart(st);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;
            if(minute<10)  st = hourOfDay + ":" + "0"+minute;
            newEventDescriprionFragment.changeEndTime(st);
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
        if(id==R.id.action_new_create){
            locationFragment.progressBarOn();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Event.NAME, newEventDescriprionFragment.getName());
                jsonObject.put(Event.DESCRIPTION, newEventDescriprionFragment.getDescription());
                jsonObject.put(Event.TAGS, new JSONArray(newEventCategoryChooseFragment.getTags()));
                jsonObject.put(Event.START, newEventDescriprionFragment.getStartDateString());
                jsonObject.put(Event.END, newEventDescriprionFragment.getEndDateString());
                if (locationFragment.getLocation()!=null){
                    jsonObject.put(Event.PLACE, new JSONArray(locationFragment.getLocation()));
                }
                jsonObject.put(Event.ADDRESS,locationFragment.getAddress() );
//                if(newEventDescriprionFragment.getMaxAge()!=-1){
//                    jsonObject.put(Event.AGE_MAX,newEventDescriprionFragment.getMaxAge() );
//                }
//
//                if( newEventDescriprionFragment.getMinAge()!=-1){
//                    jsonObject.put(Event.AGE_MIN, newEventDescriprionFragment.getMinAge());
//                }

                jsonObject.put(Event.BUDGET_MAX, newEventDescriprionFragment.getMaxBudget());
                jsonObject.put(Event.BUDGET_MIN,newEventDescriprionFragment.getMinBudget() );



            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(this.id!=null){
                LocalBroadcastManager.getInstance(this).registerReceiver(createUpdateReceiver, new IntentFilter(NetworkService.ACTION_UPDATE_EVENT));

                NetworkService.startActionUpdateEvent(this, jsonObject.toString(),newEventImageFragment.getBitMap(), this.id);

            } else {
                LocalBroadcastManager.getInstance(this).registerReceiver(createEventReceiver, new IntentFilter(NetworkService.ACTION_CREATE_EVENT));
                NetworkService.startActionCreateNewEvent(this, jsonObject.toString(),newEventImageFragment.getBitMap());
            }


        }

        if (id == android.R.id.home) {
           movePrevious();
        }

        if (id==R.id.action_forward){
            if(!(mViewPager.getCurrentItem()==3))
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createEventReceiver);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(createUpdateReceiver);
    }

    private BroadcastReceiver createUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(NetworkService.STATUS, false);
            if (status) {
                Toast.makeText(context, "Event has been updared!", Toast.LENGTH_LONG).show();
                NewEventActivity.this.finish();
            } else {
            locationFragment.progressOff();
                Toast.makeText(context, "Can not create event. Description and location must be fill up", Toast.LENGTH_LONG).show();
            }
        }
    };
    private BroadcastReceiver createEventReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(NetworkService.STATUS, false);
            if (status) {
                Toast.makeText(context, "New event has been created!", Toast.LENGTH_LONG).show();
                NewEventActivity.this.finish();
            } else {
              locationFragment.progressOff();
                Toast.makeText(context, "Can not create event. Try again", Toast.LENGTH_LONG).show();
            }
        }
    };



}

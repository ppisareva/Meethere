package com.example.polina.meethere;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.polina.meethere.Adapters.SimpleItem;
import com.example.polina.meethere.fragments.CategoryFragment;
import com.example.polina.meethere.fragments.FeedFragment;
import com.example.polina.meethere.fragments.FindEventFragment;
import com.example.polina.meethere.fragments.MyEventsListsFragment;
import com.example.polina.meethere.fragments.NewEventFragment;
import com.example.polina.meethere.fragments.ProfileFragment;
import com.example.polina.meethere.fragments.SearchResultsFragment;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;

import java.lang.reflect.Field;
import java.util.Calendar;

public class MainActivity extends AbstractMeethereActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final int MAX_WIDTH = 3000;
    private static final int START_DATE = 20202;
    public static final String NEW_EVENT = "new event";
    public static final String SEARCH = "search event";

    private static final int START_TIME = 202122;

    private static final int END_DATE = 20002;
    private static final int END_TIME = 2000232;
    private CategoryFragment categoryFragment;
    private MyEventsListsFragment myEventsListsFragment;
    private NewEventFragment newEventFragment;
    private ProfileFragment profileFragment;
    private FeedFragment feedFragment;
    SearchResultsFragment searchResultsFragment;

    private FindEventFragment findEventFragment;
    private String FEED = "feed";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryFragment = CategoryFragment.newInstance();
        feedFragment = FeedFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, feedFragment, FEED).addToBackStack(null).commit();
        myEventsListsFragment = MyEventsListsFragment.newInstance();
        newEventFragment = NewEventFragment.newInstance();
        findEventFragment = FindEventFragment.newInstance();
        searchResultsFragment = SearchResultsFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initUserInfo();
    }

    private void initUserInfo() {
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View root = nv.getHeaderView(0);
        ((TextView)root.findViewById(R.id.user_name)).setText(app().getUserProfile().getName());
        ((TextView)root.findViewById(R.id.location)).setText(app().getUserProfile().getLocation());
        SimpleDraweeView profileImage = (SimpleDraweeView)root.findViewById(R.id.profile_image);
        profileImage.setImageURI(Uri.parse(app().getUserProfile().getProfileUrl()));
        RoundingParams roundingParams = RoundingParams.asCircle();
        profileImage.getHierarchy().setRoundingParams(roundingParams);

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    public void onImageAdd(View v) {

    }
    public void onUse (View v){
        searchResultsFragment.highAdditionalParameters();
    }

    public void onEditProfile (View v){
        startActivity(new Intent(this, MyInformationActivity.class));
    }

    public void onMyEvent(View v){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, myEventsListsFragment)
                .addToBackStack(null).commit();
    }

    public void onFavorite(View v){

    }

    public void onPreferences (View v){
        Intent intent = new Intent(this, MyPreferencesActivity.class);
        startActivity(intent);

    }

    public void onSettings(View v){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onLogOut(View v){
        LoginManager.getInstance().logOut();
        app().logout();
        System.err.println("====================!!!!! >>>>>>>>>>>>>>>>>>");
        finish();
        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }

    public void onDistance(View v){

            searchResultsFragment.onDistancePress();


    }

    public void onPrice (View v){
        searchResultsFragment.onPricePress();

    }

    public void onFilter (View v){

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

    private boolean newEventFragment(){
        NewEventFragment newFragment = (NewEventFragment)getSupportFragmentManager().findFragmentByTag(NEW_EVENT);
        if (newFragment != null && newFragment.isVisible()) {
           return true;
        }
        return false;
    }
    private boolean searchFragment(){
        SearchResultsFragment newFragment = (SearchResultsFragment)getSupportFragmentManager().findFragmentByTag(SEARCH);
        if (newFragment != null && newFragment.isVisible()) {
           return true;
        }
        return false;
    }


    DatePickerDialog.OnDateSetListener onDateStartListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

            if (newEventFragment()) newEventFragment.changeStartDate(st);

                if(searchFragment()) findEventFragment.changeStartDate(st);

        }
    };

    DatePickerDialog.OnDateSetListener onDateEndListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String st = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

               if(newEventFragment()) newEventFragment.changeEndDate(st);

                   if(searchFragment()) findEventFragment.changeEndDate(st);

        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerStart = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;

             if(newEventFragment())   newEventFragment.changeTimeStart(st);

                if(searchFragment())    findEventFragment.changeTimeStart(st);

        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String st = hourOfDay + ":" + minute;


              if(newEventFragment())  newEventFragment.changeEndTime(st);

               if(searchFragment())     findEventFragment.changeEndTime(st);

        }
    };

    private void changeCloseButton(SearchView searchView){
        try {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView mSearchCloseButton = (ImageView) searchField.get(searchView);
            if (mSearchCloseButton != null) {
                mSearchCloseButton.setImageResource(R.drawable.ic_tune_white_24dp);

                mSearchCloseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      searchResultsFragment.showAdvancedSearch();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("", "Error finding close button", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            MenuItemCompat.setOnActionExpandListener(searchItem,new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, searchResultsFragment)
                            .addToBackStack(null).commit();
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, feedFragment)
                            .addToBackStack(null).commit();
                    return true;
                }
            });
        }
        if (searchView != null) {
           changeCloseButton(searchView);
            searchView.setMaxWidth(MAX_WIDTH);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    System.out.println(query + "------------------------------");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    System.out.println(newText + "++++++++++++++++++++++++++++++++++++");
                   if(!newText.isEmpty()){
                       if(newText.length()==1){
                           searchResultsFragment.clear();
                       }
                       searchResultsFragment.changeList(new SimpleItem(R.drawable.ic_android, newText));
                   }


                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_map) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            profileFragment = ProfileFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, profileFragment)
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_my_places) {
            myEventsListsFragment = MyEventsListsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, myEventsListsFragment)
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_new_event) {
            startActivity(new Intent(this, NewEventActivity.class));
        } else if (id == R.id.nav_category) {
            categoryFragment = CategoryFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, categoryFragment)
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_news) {
            feedFragment = FeedFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, feedFragment)
                    .addToBackStack(null).commit();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}



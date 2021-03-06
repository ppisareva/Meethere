package com.tolpa.activities;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.facebook.FacebookSdk;
import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.fragments.CategoryFragment;
import com.tolpa.fragments.FeedFragment;
import com.tolpa.fragments.FindEventFragment;
import com.tolpa.fragments.MyEventsListsFragment;
import com.tolpa.fragments.NewEventFragment;
import com.tolpa.fragments.ProfileFragment;
import com.tolpa.fragments.SearchResultsFragment;
import com.tolpa.model.UserProfile;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AbstractMeethereActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESULT_LOAD_IMAGE = 1;
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
    SimpleDraweeView profileImage;
    public NavigationView navigationView;

    private FindEventFragment findEventFragment;
    private String FEED = "feed";
    Set<String> category;


    public void setmContent(Fragment mContent) {
        this.mContent = mContent;
    }

    private Fragment mContent;




    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mContent==null) return;
        if (mContent.isAdded()){
            getSupportFragmentManager().putFragment(outState, "mFragment", mContent);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (app().getUserProfile() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        category =  sharedPreferences.getStringSet(UserProfile.CATEGORY, new HashSet<String>());

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");

        } else {
            feedFragment = FeedFragment.newInstance(category);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, feedFragment, FEED).commit();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initUserInfo();
        FirebaseInstanceId.getInstance().getToken();
    }

    public void initUserInfo() {

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        View root = nv.getHeaderView(0);
        FirebaseCrash.log("Main Activity: user_id=" + app().getUserProfile().getId());
        ((TextView)root.findViewById(R.id.user_name)).setText(app().getUserProfile().getName());
        ((TextView)root.findViewById(R.id.location)).setText(!app().getUserProfile().getLocation().equals("null")? app().getUserProfile().getLocation():"");
        (root.findViewById(R.id.profile_open)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment = ProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, profileFragment)
                        .commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        profileImage = (SimpleDraweeView)root.findViewById(R.id.profile_image);
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
        if (searchResultsFragment == null)
            searchResultsFragment = SearchResultsFragment.newInstance();
        searchResultsFragment.highAdditionalParameters();
    }

    public void onFavorite(View v){

        startActivity(new Intent(this, FeedActivity.class));

    }




    public void onLogOut(View v){
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();


        app().logout();
        System.err.println("====================!!!!! >>>>>>>>>>>>>>>>>>");

        startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();

    }

    public void onDistance(View v){
            searchResultsFragment.onDistancePress();
    }

    public void onPrice (View v){
        searchResultsFragment.onPricePress();
    }

    public void onFilter (View v){
        startActivity( new Intent(this, SearchFiltersActivity.class));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {

            searchView = (SearchView) searchItem.getActionView();

        }
        if (searchView != null) {
//          changeCloseButton(searchView);
            searchView.setMaxWidth(MAX_WIDTH);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    System.out.println(query + "------------------------------");
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
                    intent.putExtra(Utils.SEARCH, query.replace(" ", "+"));
                    startActivity(intent);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
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
            profileFragment  = ProfileFragment.newInstance();
            mContent = profileFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, profileFragment)
                    .commit();
        } else if (id == R.id.nav_my_places) {
            myEventsListsFragment = MyEventsListsFragment.newInstance();
            mContent = myEventsListsFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, myEventsListsFragment)
                 .commit();
        } else if (id == R.id.nav_new_event) {
            startActivity(new Intent(this, NewEventActivity.class));
        } else if (id == R.id.nav_category) {
            categoryFragment = CategoryFragment.newInstance();
            mContent = categoryFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, categoryFragment)
                    .commit();
        } else if (id == R.id.nav_news) {
            feedFragment = FeedFragment.newInstance(category);
            mContent = feedFragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, feedFragment)
                    .commit();

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



}



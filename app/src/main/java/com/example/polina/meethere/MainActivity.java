package com.example.polina.meethere;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.example.polina.meethere.fragments.CategoryFragment;
import com.example.polina.meethere.fragments.MyEventsListsFragment;
import com.example.polina.meethere.fragments.NewEventFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static int MAX_WIDTH = 3000;
    private CategoryFragment categoryFragment;
    private MyEventsListsFragment myEventsListsFragment;
    private NewEventFragment newEventFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categoryFragment = CategoryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_main, categoryFragment).commit();
        myEventsListsFragment = MyEventsListsFragment.newInstance();
        newEventFragment = NewEventFragment.newInstance();
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
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    public void onStart (View v){
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,)
//    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
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

                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_map){
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
        } else if (id == R.id.nav_my_places) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, myEventsListsFragment).commit();
        } else if (id == R.id.nav_new_event) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, newEventFragment).commit();
        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_category) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, categoryFragment).commit();
        } else if (id == R.id.nav_settings) {
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}

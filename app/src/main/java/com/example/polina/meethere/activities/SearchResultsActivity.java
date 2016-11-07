package com.example.polina.meethere.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.ListOfEventSearchFragment;
import com.example.polina.meethere.fragments.ListOfFriendsSearchFragment;

public class SearchResultsActivity extends AppCompatActivity {

    private static final int SEARCH_EVENTS = 0;
    private static final int SEARCH_PEOPLE = 1;
    String search;
    private PagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    ListOfEventSearchFragment fragmentEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        search = getIntent().getStringExtra(Utils.SEARCH);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        fragmentEvents = ListOfEventSearchFragment.newInstance(search);
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void onFilter(View v) {
        startActivity(new Intent(this, SearchFiltersActivity.class));

    }

    public class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case SEARCH_EVENTS:
                    return fragmentEvents;
                case  SEARCH_PEOPLE:
                    return ListOfFriendsSearchFragment.newInstance(search);

            }
            return null;


        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.events);
                case 1:
                    return getString(R.string.people);
            }
            return null;
        }
    }



}

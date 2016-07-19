package com.example.polina.meethere.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

public class MyEventsListsFragment extends android.support.v4.app.Fragment  {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    public final int FRAGMENT_PAST_EVENTS = 2;
    public final int FRAGMENT_FUTURE_EVENTS = 1;
    public final int FRAGMENT_CREATED_BY_ME_EVENTS = 0;

    public final int PAST_EVENTS = 4343430;
    public final int FUTURE_EVENTS = 4343431;
    public final int CREATED_BY_ME_EVENTS = 4343432;

    public static MyEventsListsFragment newInstance() {
        MyEventsListsFragment fragment = new MyEventsListsFragment();
        return fragment;
    }

    public MyEventsListsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_my_events_lists, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return v;
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case FRAGMENT_CREATED_BY_ME_EVENTS:
                    return MyEventsFragment.newInstance(CREATED_BY_ME_EVENTS);
                case  FRAGMENT_FUTURE_EVENTS:
                    return MyEventsFragment.newInstance(FUTURE_EVENTS);
                case FRAGMENT_PAST_EVENTS:
                    return MyEventsFragment.newInstance(PAST_EVENTS);

            }
            return null;

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.createt_by_my);
                case 1:
                    return getString(R.string.future_events);
                case 2:
                    return getString(R.string.past_events);
            }
            return null;
        }
    }

}

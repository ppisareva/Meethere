package com.tolpa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tolpa.adapters.MyEventsAdapter;
import com.tolpa.R;

public class MyEventsListsFragment extends android.support.v4.app.Fragment  {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

    MyEventsFragment fragment;
    public final int FRAGMENT_PAST_EVENTS = 2;
    public final int FRAGMENT_FUTURE_EVENTS = 1;
    public final int FRAGMENT_CREATED_BY_ME_EVENTS = 0;

    public static final int PAST_EVENTS = 4343430;
    public static final int FUTURE_EVENTS = 4343431;
    public static final int CREATED_BY_ME_EVENTS = 4343432;
    Context context;

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
        context = getActivity();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return v;
    }

    public MyEventsAdapter getAdapter() {
       return fragment.getAdapter();
    }


    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case FRAGMENT_CREATED_BY_ME_EVENTS:
                    fragment = MyEventsFragment.newInstance(CREATED_BY_ME_EVENTS);
                    return fragment;
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

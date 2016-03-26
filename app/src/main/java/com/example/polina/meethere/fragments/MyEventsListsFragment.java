package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

public class MyEventsListsFragment extends android.support.v4.app.Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        // подписать таб
        tabLayout.getTabAt(Utils.FRAGMENT_PAST_EVENTS).setText(R.string.past_events);
        tabLayout.getTabAt(Utils.FRAGMENT_FUTURE_EVENTS).setText(R.string.future_events);
        tabLayout.getTabAt(Utils.FRAGMENT_CREATED_BY_ME_EVENTS).setText(R.string.createt_by_my);
        return v;
    }




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position){
                case Utils.FRAGMENT_CREATED_BY_ME_EVENTS:
                    return MyEventsFragment.newInstance();
                case  Utils.FRAGMENT_FUTURE_EVENTS:
                    return MyEventsFragment.newInstance();
                case Utils.FRAGMENT_PAST_EVENTS:
                    return MyEventsFragment.newInstance();

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
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

}

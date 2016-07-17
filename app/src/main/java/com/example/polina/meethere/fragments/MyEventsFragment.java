package com.example.polina.meethere.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;

public class MyEventsFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView list;
    MyEventsAdapter adapter;
    int tag;




    public static MyEventsFragment newInstance(int tag) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle arg = new Bundle();
        arg.putInt(Utils.TIME_TAG, tag);
        fragment.setArguments(arg);
        return fragment;
    }

    public static MyEventsFragment newInstance(int tag, int userId) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle arg = new Bundle();
        arg.putInt(Utils.TIME_TAG, tag);
        arg.putInt(Utils.USER_ID, userId);
        fragment.setArguments(arg);
        return fragment;
    }

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoader();
    }

    private void initLoader() {
        if (getArguments() != null) {
            Bundle bundle = null;
            int user_id= getArguments().getInt(Utils.USER_ID, 0);
            tag = getArguments().getInt(Utils.TIME_TAG);
            bundle.putInt(Utils.USER_ID, getArguments().getInt(Utils.USER_ID));

            getActivity().getSupportLoaderManager().initLoader(tag+user_id, bundle, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);
        list = (RecyclerView) v.findViewById(R.id.my_events_list);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new MyEventsAdapter(getActivity());
        list.setAdapter(adapter);
        return v;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] = new String[]{com.example.polina.meethere.model.Event.ID, com.example.polina.meethere.model.Event.NAME,
                com.example.polina.meethere.model.Event.DESCRIPTION, com.example.polina.meethere.model.Event.START,
                com.example.polina.meethere.model.Event.END, com.example.polina.meethere.model.Event.TAGS,
                com.example.polina.meethere.model.Event.PLACE, com.example.polina.meethere.model.Event.ADDRESS,
                com.example.polina.meethere.model.Event.AGE_MAX, com.example.polina.meethere.model.Event.AGE_MIN,
                com.example.polina.meethere.model.Event.BUDGET_MAX, com.example.polina.meethere.model.Event.BUDGET_MIN};
        int userId = args.getInt(Utils.USER_ID);

        Uri uri =  Uri.parse("content://com.example.polina.meethere.data.data/myevents/"+(id));
        if(userId!=0){
           uri =  Uri.parse("content://com.example.polina.meethere.data.data/userevents/"+(userId));
        }
        return new CursorLoader(getActivity(), uri, arr, null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.err.println("onLoaderReset");
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(tag, null, this);

    }


}

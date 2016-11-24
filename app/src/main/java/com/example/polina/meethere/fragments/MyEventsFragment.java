package com.example.polina.meethere.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Event;

public class MyEventsFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CHANGE_EVENT_REQUEST = 7;
    private RecyclerView list;
    MyEventsAdapter adapter;
    int tag;
    int offset = 0;
    private int STEP =10;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag;




    public static MyEventsFragment newInstance(int tag) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle arg = new Bundle();
        arg.putInt(Utils.TIME_TAG, tag);
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

    public MyEventsAdapter getAdapter(){
        return adapter;
    }


    private void initLoader() {
        if (getArguments() != null) {
            tag = getArguments().getInt(Utils.TIME_TAG);
            Bundle b = new Bundle();
            b.putInt(Utils.OFFSET, offset);
            getActivity().getSupportLoaderManager().initLoader(tag, b, this);
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
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mRecyclerViewHelper.getItemCount();
                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                System.err.println("first visible id" + firstVisibleItem + "visibleItemCount " + visibleItemCount + "totalItemCount" + totalItemCount);
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 1) {
                    if(isFlag()) {
                        setFlag(false);
                        Bundle arg = new Bundle();
                        offset+=STEP;
                        arg.putInt(Utils.OFFSET, offset);
                        getActivity().getSupportLoaderManager().restartLoader(tag, arg, MyEventsFragment.this);
                    }
                }
            }
        });
        return v;

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String o = ""+args.getInt(Utils.OFFSET, 0);
        Uri uri =  Uri.parse(String.format("content://com.example.polina.meethere.data.data/myevents/?offset=%s&id=%s", o, (id+"")));


        return new CursorLoader(getActivity(),
               uri,new String[]{Event.ID, Event.NAME,Event.DESCRIPTION, Event.START,
                 Event.TAGS,Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data==null){
            Toast.makeText(getActivity(), getString(R.string.on_internet_connection), Toast.LENGTH_LONG).show();
            if(offset==0){
               // getActivity().getSupportFragmentManager().
            }
            return;
        }
        if(adapter.getItemCount() < data.getCount()) {
            setFlag(true);
        }
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.err.println("onLoaderReset");
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle b = new Bundle();
        b.putInt(Utils.OFFSET, offset);
        getActivity().getSupportLoaderManager().restartLoader(tag, b, this);

    }



}

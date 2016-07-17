package com.example.polina.meethere.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.MyFriendsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.facebook.drawee.view.SimpleDraweeView;


public class ListOfFriendsSearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int SEARCH_FRIEND_LOUDER = 566765 ;
    MyFriendsAdapter adapter;
    String search;
    LinearLayout layout;

    public ListOfFriendsSearchFragment() {
        // Required empty public constructor
    }


    public static ListOfFriendsSearchFragment newInstance(String search) {
        ListOfFriendsSearchFragment fragment = new ListOfFriendsSearchFragment();
        Bundle args = new Bundle();
        args.putString(Utils.SEARCH,search);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoader();
    }

    private void initLoader() {
        if (getArguments() != null) {
            search = getArguments().getString(Utils.SEARCH);
            Bundle arg = new Bundle();
            arg.putString(Utils.SEARCH, search);
            getActivity().getSupportLoaderManager().initLoader(SEARCH_FRIEND_LOUDER, arg, this);

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_of_friends, container, false);

        RecyclerView recyclerView = (RecyclerView)v. findViewById(R.id.friends_list);
        layout =(LinearLayout) v.findViewById(R.id.no_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new MyFriendsAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        return v;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{ User.ID, User.FIRST_NAME,User.LAST_NAME, User.IMAGE};
        String search = args.getString(Utils.SEARCH, "");
        Uri uri = Uri.parse("content://com.example.polina.meethere.data.data/friends_search/" + search);
        return new CursorLoader(getActivity(), uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

       if(data !=null&&data.getCount()!=0){
           layout.setVisibility(View.GONE);
           adapter.swapCursor(data);
       } else {
           layout.setVisibility(View.VISIBLE);

       }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

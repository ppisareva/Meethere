package com.example.polina.meethere.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Category;
import com.example.polina.meethere.adapters.HeaderAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.adapters.VerticalEventAdapter;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    VerticalEventAdapter verticalEventAdapter;
    List <Category> eventsCategoryList = new ArrayList<>();
    RecyclerView verticalList;
    RecyclerViewHeader header;
    RecyclerView  headerView;
    Set<String> category;
    ServerApi serverApi;
    public static final String ID = "id";
    String arr[];



    public static FeedFragment newInstance(Set<String> c) {
        FeedFragment fragment = new FeedFragment();
        Bundle b = new Bundle();
        ArrayList<String> list = new ArrayList<String>(c);
        b.putStringArrayList(Utils.CATEGORY, list);
        fragment.setArguments(b);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverApi = ((App)getActivity().getApplication()).getServerApi();
         arr = getResources().getStringArray(R.array.category);
        if (getArguments() != null) {
           category = new HashSet<>(getArguments().getStringArrayList(Utils.CATEGORY));

        }
    }

    public FeedFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_feed, container, false);
        eventsCategoryList = getListEvents();
        verticalEventAdapter = new VerticalEventAdapter(eventsCategoryList, getActivity());
        for (Category c : eventsCategoryList)
            getActivity().getSupportLoaderManager().initLoader(c.getId(), null, this);
        headerView = (RecyclerView) v.findViewById(R.id.category_list_header);
        verticalList = (RecyclerView) v.findViewById(R.id.vertical_list);
        header = (RecyclerViewHeader) v.findViewById(R.id.header);

        verticalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        headerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... params) {
                return serverApi.loadPopularCategory();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                List<Category> categories = new ArrayList<Category>();
                List<Integer> response =  getCategory(jsonObject);
                for (Integer c :response) {
                    categories.add(new Category(c, arr[c]));
                }
                HeaderAdapter headerAdapter = new HeaderAdapter(categories);
                headerView.setAdapter(headerAdapter);
            }
        }.execute();

        header.attachTo(verticalList);
        verticalList.setAdapter(verticalEventAdapter);
//        verticalList.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
//                int visibleItemCount = recyclerView.getChildCount();
//                int totalItemCount = mRecyclerViewHelper.getItemCount();
//                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 1) {
//                    // load more category
//                }
//                }
//
//        });

        return v;
    }




    public List<Category> getListEvents(){
        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category(445445, "Популярное"));
        for(String c: category){

                int idCategory = Integer.parseInt(c);
            try {
                categoryList.add(new Category(idCategory, arr[idCategory]));
            } catch (Exception e){
                e.printStackTrace();

            }

        }
        return categoryList;
    }
 //
    public List<Integer> getCategory(JSONObject jsonObject){
        List<Integer> c = new ArrayList<>();
            JSONArray arr = new JSONArray();
        if(jsonObject==null){
            Set<String> set = ((App)getActivity().getApplication()).getUserProfile().getCategory();
            for(String id :set){
                c.add(Integer.parseInt(id));
            }
            return c;
        }
            try {
                arr = jsonObject.getJSONArray(Utils.RESULTS);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject category = arr.getJSONObject(i);
                    c.add(category.getInt(ID));
                }
                return c;
            } catch (JSONException e) {
                e.printStackTrace();
               return null;
            }
        }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
               Uri.parse(String.format("content://com.example.polina.meethere.data.data/category_feed/?category=%s", id))
                ,  new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START,
                Event.TAGS,
                Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        verticalEventAdapter.updateCursor(loader.getId(), data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        verticalEventAdapter.updateCursor(loader.getId(), null);
    }
}

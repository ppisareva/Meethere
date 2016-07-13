package com.example.polina.meethere.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.adapters.Category;
import com.example.polina.meethere.HeaderAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.VerticalEventAdapter;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    VerticalEventAdapter verticalEventAdapter;
    List <Category> events = getListEvents();
    RecyclerView verticalList;
    RecyclerViewHeader header;
    RecyclerView  headerView;



    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();

        return fragment;
    }

    public FeedFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_feed, container, false);
        verticalEventAdapter = new VerticalEventAdapter(events, getActivity());
        for (Category c : events)
            getActivity().getSupportLoaderManager().initLoader(c.getId(), null, this);
        headerView = (RecyclerView) v.findViewById(R.id.category_list_header);
        verticalList = (RecyclerView) v.findViewById(R.id.vertical_list);
        header = (RecyclerViewHeader) v.findViewById(R.id.header);

        verticalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        headerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        HeaderAdapter headerAdapter = new HeaderAdapter(getCategory());
        headerView.setAdapter(headerAdapter);
        header.attachTo(verticalList);
        verticalList.setAdapter(verticalEventAdapter);

        return v;
    }




    public List<Category> getListEvents(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(445445, "Популярное"));
        categoryList.add(new Category(1, "Фитнес"));
        categoryList.add(new Category(2, "Еда и напитки"));
        categoryList.add(new Category(3, "Духовность"));
        categoryList.add(new Category(4, "С друзьями"));
        categoryList.add(new Category(5, "На природе"));
        categoryList.add(new Category(6, "Книги"));
        categoryList.add(new Category(7, "Политика"));

        return categoryList;
    }

    public List<String> getCategory(){
        List<String > c = new ArrayList<>();
        c.add("Фитнес");
        c.add("Еда и Напитки");
        c.add("Исскуство и Культура");
        c.add("Танци");
        c.add("Мода");
        c.add("Больше");
        return c;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                Uri.parse("content://com.example.polina.meethere.data.data/category/"+ id)
                , new String[]{com.example.polina.meethere.model.Event.ID, com.example.polina.meethere.model.Event.NAME,
                com.example.polina.meethere.model.Event.DESCRIPTION, com.example.polina.meethere.model.Event.START,
                com.example.polina.meethere.model.Event.END, com.example.polina.meethere.model.Event.TAGS,
                com.example.polina.meethere.model.Event.PLACE, com.example.polina.meethere.model.Event.ADDRESS,
                com.example.polina.meethere.model.Event.AGE_MAX, com.example.polina.meethere.model.Event.AGE_MIN,
                com.example.polina.meethere.model.Event.BUDGET_MAX, com.example.polina.meethere.model.Event.BUDGET_MIN}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        verticalEventAdapter.updateCursor(loader.getId(), data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

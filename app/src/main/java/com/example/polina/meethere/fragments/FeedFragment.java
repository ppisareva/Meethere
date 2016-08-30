package com.example.polina.meethere.fragments;

import android.content.SharedPreferences;
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

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.adapters.Category;
import com.example.polina.meethere.HeaderAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.VerticalEventAdapter;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.UserProfile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    VerticalEventAdapter verticalEventAdapter;
    List <Category> events = new ArrayList<>();
    RecyclerView verticalList;
    RecyclerViewHeader header;
    RecyclerView  headerView;
    Set<String> category;



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
        events =getListEvents();
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

        String arr[] = getResources().getStringArray(R.array.category);

        categoryList.add(new Category(445445, "Популярное"));
        for(String c: category){
            int idCategory = Integer.parseInt(c);
            categoryList.add(new Category(idCategory, arr[idCategory]));
        }
//        categoryList.add(new Category(1, "Фитнес"));
//        categoryList.add(new Category(2, "Еда и напитки"));
//        categoryList.add(new Category(3, "Духовность"));
//        categoryList.add(new Category(4, "С друзьями"));
//        categoryList.add(new Category(5, "На природе"));
//        categoryList.add(new Category(6, "Книги"));
//        categoryList.add(new Category(7, "Политика"));

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
               Uri.parse(String.format("content://com.example.polina.meethere.data.data/category/?category=%s&offset=%s", id, 0))
                ,  new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START,
                Event.END, Event.TAGS,
                Event.PLACE, Event.ADDRESS,
                Event.AGE_MAX, Event.AGE_MIN,
                Event.BUDGET_MAX, Event.BUDGET_MIN, Event.LAT, Event.LNG}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        verticalEventAdapter.updateCursor(loader.getId(), data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

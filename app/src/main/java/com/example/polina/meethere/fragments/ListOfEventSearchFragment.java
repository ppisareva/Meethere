package com.example.polina.meethere.fragments;

import android.database.Cursor;
import android.location.Location;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;

/**
 * Created by polina on 28.06.16.
 */
public class ListOfEventSearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    MyEventsAdapter myEventsAdapter;
    boolean highPrice = false;
    boolean lowPrice = false;
    public static final int SEARCH_LOUDER = 20330;
    public static final int SEARCH_BY_LOW_PRICE = 203990;
    public static final int SEARCH_BY_HIGH_PRICE = 2033430;
    public static final int SEARCH_BY_DISTANCE = 20999430;
    private final int STEP = 10;
    private int offset = STEP;



    TextView distance;
    TextView price;
    ImageView imageView;
    String search;
    App app;
    LinearLayout l;

    public static ListOfEventSearchFragment newInstance(String search) {
        Bundle args = new Bundle();
        args.putString(Utils.SEARCH, search);
        ListOfEventSearchFragment fragment = new ListOfEventSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListOfEventSearchFragment(){

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_list_of_events, container, false);
        distance = (TextView)v. findViewById(R.id.distance_filter);
        price = (TextView) v.findViewById(R.id.price_filter);
        imageView = (ImageView)v. findViewById(R.id.image_price);
        l=(LinearLayout) v.findViewById(R.id.no_result);
        RecyclerView recyclerView = (RecyclerView)v. findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        myEventsAdapter = new MyEventsAdapter(getActivity());
        recyclerView.setAdapter(myEventsAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mRecyclerViewHelper.getItemCount();
                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                System.err.println("first visible id" + firstVisibleItem + "visibleItemCount " + visibleItemCount + "totalItemCount" + totalItemCount);
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 1) {

                    Bundle arg = new Bundle();
                    arg.putString(Utils.SEARCH, search);
                    arg.putInt(Utils.OFFSET, offset);
                    getActivity().getSupportLoaderManager().initLoader(SEARCH_LOUDER, arg, ListOfEventSearchFragment.this);
                    }

            }
        });

        return v;
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
            arg.putInt(Utils.OFFSET, offset);
           getActivity().getSupportLoaderManager().initLoader(SEARCH_LOUDER, arg, this);

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app =(App) getActivity().getApplication();
        setRetainInstance(true);




    }


    public  void onDistance() {
        distance.setTextColor(getResources().getColor(R.color.filter));
        price.setTextColor(getResources().getColor(R.color.white));
        imageView.setImageResource(R.drawable.ic_price);
        highPrice = false;
        lowPrice = false;
        Bundle arg = new Bundle();
        Location location = app.getCurrentLocation();
        arg.putDouble(Utils.LON,location.getLongitude());
        arg.putDouble(Utils.LAT,location.getLatitude());
        arg.putString(Utils.SEARCH, search);
        offset = STEP;
        arg.putInt(Utils.OFFSET, offset);
        getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_DISTANCE,arg , this);

    }

    public void onPrice() {
        distance.setTextColor(getResources().getColor(R.color.white));
        price.setTextColor(getResources().getColor(R.color.filter));
        Bundle arg = new Bundle();
        arg.putString(Utils.SEARCH, search);
        offset = STEP;
        arg.putInt(Utils.OFFSET, offset);
        if (highPrice || lowPrice) {
            if (highPrice) {
                imageView.setImageResource(R.drawable.ic_price_down_24dp);
                lowPrice = true;
                highPrice = false;

               getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, this);
            } else {

                if (lowPrice) {
                    imageView.setImageResource(R.drawable.ic_price_up_24dp);
                    lowPrice = false;
                    highPrice = true;
                    getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_LOW_PRICE, arg, this);
                }
            }
        } else {

            lowPrice = true;
            imageView.setImageResource(R.drawable.ic_price_down_24dp);
            getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, this);

        }
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{com.example.polina.meethere.model.Event.ID, com.example.polina.meethere.model.Event.NAME,
                com.example.polina.meethere.model.Event.DESCRIPTION, com.example.polina.meethere.model.Event.START,
                com.example.polina.meethere.model.Event.END, com.example.polina.meethere.model.Event.TAGS,
                com.example.polina.meethere.model.Event.PLACE, com.example.polina.meethere.model.Event.ADDRESS,
                com.example.polina.meethere.model.Event.AGE_MAX, com.example.polina.meethere.model.Event.AGE_MIN,
                com.example.polina.meethere.model.Event.BUDGET_MAX, com.example.polina.meethere.model.Event.BUDGET_MIN};
        String search = args.getString(Utils.SEARCH, "");
        int offset = args.getInt(Utils.OFFSET, STEP);
         Uri uri = null;


        switch (id){
            case SEARCH_LOUDER:
                     uri =   Uri.parse("content://com.example.polina.meethere.data.data/words_search/" + search);
                break;
            case SEARCH_BY_LOW_PRICE:
                  uri =  Uri.parse("content://com.example.polina.meethere.data.data/low_price_search/" + search);
                break;
            case SEARCH_BY_HIGH_PRICE:
                    uri=    Uri.parse("content://com.example.polina.meethere.data.data/high_price_search/" + search);
                break;
            case SEARCH_BY_DISTANCE:
                String lon = String.valueOf(args.getDouble(Utils.LON));
                String lat = String.valueOf(args.getDouble(Utils.LAT));
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/distance_search/?lon=%s&lat=%s&search=%s", lon, lat, search));

        }
        return new CursorLoader(getActivity(), uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data !=null&&data.getCount()!=0) {
            l.setVisibility(View.GONE);

            myEventsAdapter.swapCursor(data);


        } else {
            l.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



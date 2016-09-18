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
import com.example.polina.meethere.model.Event;

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
    int loaderWorking= 0;
    int offset = 0;
    private int STEP =10;
    Boolean loaderDistance =false;
    Boolean loaderHighPrice = false;
    Boolean loaderLowPrice = false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag = true;



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
        distance.setOnClickListener(onDistance);
        price = (TextView) v.findViewById(R.id.price_filter);
        price.setOnClickListener(onPrice);
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
                    if(isFlag()) {
                        setFlag(false);
                        Bundle arg = new Bundle();
                        arg.putString(Utils.SEARCH, search);
                        offset+=STEP;
                        arg.putInt(Utils.OFFSET, offset);

                        if(loaderWorking==0)return;
                        getActivity().getSupportLoaderManager().restartLoader(loaderWorking, arg, ListOfEventSearchFragment.this);



                    }
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

View.OnClickListener onDistance = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
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
        offset = 0;
        arg.putInt(Utils.OFFSET, offset);
        if(loaderDistance){
            getActivity().getSupportLoaderManager().restartLoader(SEARCH_BY_DISTANCE,arg , ListOfEventSearchFragment.this);
        } else {
            getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_DISTANCE, arg, ListOfEventSearchFragment.this);
            loaderDistance = true;
        }
    }
};

    View.OnClickListener onPrice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            distance.setTextColor(getResources().getColor(R.color.white));
            price.setTextColor(getResources().getColor(R.color.filter));
            Bundle arg = new Bundle();
            arg.putString(Utils.SEARCH, search);
            offset = 0;
            arg.putInt(Utils.OFFSET, offset);
            if (highPrice || lowPrice) {
                if (highPrice) {
                    imageView.setImageResource(R.drawable.ic_price_down_24dp);
                    lowPrice = true;
                    highPrice = false;
                    if(loaderHighPrice) {
                        getActivity().getSupportLoaderManager().restartLoader(SEARCH_BY_HIGH_PRICE, arg, ListOfEventSearchFragment.this);
                    } else {
                        getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, ListOfEventSearchFragment.this);
                        loaderHighPrice = true;
                    }
                } else {

                    if (lowPrice) {
                        imageView.setImageResource(R.drawable.ic_price_up_24dp);
                        lowPrice = false;
                        highPrice = true;
                        if(loaderLowPrice) {
                            getActivity().getSupportLoaderManager().restartLoader(SEARCH_BY_LOW_PRICE, arg, ListOfEventSearchFragment.this);
                        } else {
                            getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_LOW_PRICE, arg, ListOfEventSearchFragment.this);
                            loaderLowPrice = true;
                        }
                    }
                }
            } else {

                lowPrice = true;
                imageView.setImageResource(R.drawable.ic_price_down_24dp);
                if(loaderHighPrice) {
                    getActivity().getSupportLoaderManager().restartLoader(SEARCH_BY_HIGH_PRICE, arg, ListOfEventSearchFragment.this);
                } else {
                    getActivity().getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, ListOfEventSearchFragment.this);
                    loaderHighPrice = true;
                }

            }

        }
    };




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START,
                 Event.TAGS,
                Event.JOINED, Event.ADDRESS,
               Event.BUDGET_MIN, Event.LAT, Event.LNG};
        String search = args.getString(Utils.SEARCH, "");
       String offset = ""+args.getInt(Utils.OFFSET, 0);
         Uri uri = null;


        switch (id){
            case SEARCH_LOUDER:
                     uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/words_search/?offset=%s&search=%s" , offset, search));
                break;
            case SEARCH_BY_LOW_PRICE:
                  uri =  Uri.parse(String.format("content://com.example.polina.meethere.data.data/low_price_search/?search=%s&offset=%s" ,search,offset));
                break;
            case SEARCH_BY_HIGH_PRICE:
                    uri=    Uri.parse(String.format("content://com.example.polina.meethere.data.data/high_price_search/?search=%s&offset=%s" , search, offset));
                break;
            case SEARCH_BY_DISTANCE:
                String lon = String.valueOf(args.getDouble(Utils.LON));
                String lat = String.valueOf(args.getDouble(Utils.LAT));
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/distance_search/?lon=%s&lat=%s&search=%s&offset=%s", lon, lat, search, offset));

        }
        return new CursorLoader(getActivity(), uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(myEventsAdapter.getItemCount() < data.getCount()) {
            l.setVisibility(View.GONE);
            setFlag(true);
        } else {
            if(myEventsAdapter.getItemCount()==0)
            l.setVisibility(View.VISIBLE);
        }
        myEventsAdapter.swapCursor(data);
        loaderWorking = loader.getId();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



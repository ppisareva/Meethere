package com.example.polina.meethere.activities;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.ListOfEventSearchFragment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;

public class ListOfEventsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {



    MyEventsAdapter myEventsAdapter;
    boolean highPrice = false;
    boolean lowPrice = false;

    TextView distance;
    TextView price;
    TextView time;
    ImageView imageView;
    int loaderWorking= 0;
    private int STEP =10;
    Boolean loaderDistance =false;
    Boolean loaderHighPrice = false;
    Boolean loaderLowPrice = false;
    Boolean loaderDate = false;
    App app;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public static final int CATEGORY = 20330;
    public static final int BY_LOW_PRICE = 203000990;
    private static final int CATEGORY_TIME_FUTURE = 556;
    public static final int BY_HIGH_PRICE = 2090933430;
    public static final int BY_DISTANCE = 209994385;
     int category;




    Boolean flag = true;
    int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        app = (App) getApplication();
        category = getIntent().getIntExtra(Utils.CATEGORY, -1);

        distance =(TextView)findViewById(R.id.distance_filter);
        time = (TextView) findViewById(R.id.time_sort);
        distance.setOnClickListener(onDistance);
        price = (TextView)findViewById(R.id.price_filter);
        price.setOnClickListener(onPrice);
        imageView = (ImageView)findViewById(R.id.image_price);
        System.out.println(" category # " + category);
        Bundle arg = new Bundle();
        arg.putString(Utils.CATEGORY, category+"");
        arg.putString(Utils.OFFSET, offset+"");

        getSupportLoaderManager().initLoader(CATEGORY, arg, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Utils.categoryTitle(this, category));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myEventsAdapter = new MyEventsAdapter(this);
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
                        offset+=STEP;
                        arg.putString(Utils.CATEGORY, category+"");
                        arg.putString(Utils.OFFSET, offset+"");
                        if(loaderWorking==0)return;
                        getSupportLoaderManager().restartLoader(loaderWorking, arg, ListOfEventsActivity.this);


                    }
                }
            }
        });
    }

    public void onDatePress(View v){
        time.setTextColor(getResources().getColor(R.color.filter));
        distance.setTextColor(getResources().getColor(R.color.white));
        price.setTextColor(getResources().getColor(R.color.white));
        imageView.setImageResource(R.drawable.ic_price);
        offset = 0;
        Bundle arg = new Bundle();
        arg.putString(Utils.CATEGORY, category+"");
        arg.putString(Utils.OFFSET, offset+"");
        if(loaderDate){
            getSupportLoaderManager().restartLoader(CATEGORY_TIME_FUTURE,arg ,ListOfEventsActivity.this);
        } else {
            getSupportLoaderManager().restartLoader(CATEGORY_TIME_FUTURE,arg ,ListOfEventsActivity.this);
            loaderDate = true;
        }



    }


    View.OnClickListener onDistance= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            distance.setTextColor(getResources().getColor(R.color.filter));
            time.setTextColor(getResources().getColor(R.color.white));
            price.setTextColor(getResources().getColor(R.color.white));
            imageView.setImageResource(R.drawable.ic_price);
            highPrice = false;
            lowPrice = false;
            offset = 0;
            Bundle arg = new Bundle();
            Location location = app.getCurrentLocation();
            arg.putDouble(Utils.LON,location.getLongitude());
            arg.putDouble(Utils.LAT,location.getLatitude());
            arg.putString(Utils.CATEGORY, category+"");

            arg.putString(Utils.OFFSET, offset+"");
            if(loaderDistance){
               getSupportLoaderManager().restartLoader(BY_DISTANCE,arg ,ListOfEventsActivity.this);
            } else {
                getSupportLoaderManager().initLoader(BY_DISTANCE, arg, ListOfEventsActivity.this);
                loaderDistance = true;
            }
        }
    };

    View.OnClickListener onPrice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            distance.setTextColor(getResources().getColor(R.color.white));
            time.setTextColor(getResources().getColor(R.color.white));
            price.setTextColor(getResources().getColor(R.color.filter));
            Bundle arg = new Bundle();
            arg.putString(Utils.CATEGORY,category+"");
            offset = 0;
            arg.putString(Utils.OFFSET, offset+"");
            if (highPrice || lowPrice) {
                if (highPrice) {
                    imageView.setImageResource(R.drawable.ic_price_down_24dp);
                    lowPrice = true;
                    highPrice = false;
                    if(loaderHighPrice) {
                        getSupportLoaderManager().restartLoader(BY_HIGH_PRICE, arg, ListOfEventsActivity.this);
                    } else {
                        getSupportLoaderManager().initLoader(BY_HIGH_PRICE, arg, ListOfEventsActivity.this);
                        loaderHighPrice = true;
                    }
                } else {

                    if (lowPrice) {
                        imageView.setImageResource(R.drawable.ic_price_up_24dp);
                        lowPrice = false;
                        highPrice = true;
                        if(loaderLowPrice) {
                            getSupportLoaderManager().restartLoader(BY_LOW_PRICE, arg, ListOfEventsActivity.this);
                        } else {
                            getSupportLoaderManager().initLoader(BY_LOW_PRICE, arg, ListOfEventsActivity.this);
                            loaderLowPrice = true;
                        }
                    }
                }
            } else {

                lowPrice = true;
                imageView.setImageResource(R.drawable.ic_price_down_24dp);
                if(loaderHighPrice) {
                    getSupportLoaderManager().restartLoader(BY_HIGH_PRICE, arg, ListOfEventsActivity.this);
                } else {
                    getSupportLoaderManager().initLoader(BY_HIGH_PRICE, arg, ListOfEventsActivity.this);
                    loaderHighPrice = true;
                }

            }

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{Event.ID, Event.NAME,
               Event.DESCRIPTION, Event.START,
               Event.TAGS,
                Event.JOINED, Event.ADDRESS,
               Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES};
        String category = args.getString(Utils.CATEGORY);
        String offset = args.getString(Utils.OFFSET);
        Uri uri = null;


        switch (id){
            case CATEGORY:
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/category/?category=%s&offset=%s",  category, offset));
                break;
            case CATEGORY_TIME_FUTURE:
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/category_time/?category=%s&offset=%s",  category, offset));
                break;
            case BY_LOW_PRICE:
                uri =  Uri.parse(String.format("content://com.example.polina.meethere.data.data/low_price_category/?category=%s&offset=%s" ,  category, offset));
                break;
            case BY_HIGH_PRICE:
                uri=    Uri.parse(String.format("content://com.example.polina.meethere.data.data/high_price_category/?category=%s&offset=%s" , category, offset));
                break;
            case BY_DISTANCE:
                String lon = String.valueOf(args.getDouble(Utils.LON));
                String lat = String.valueOf(args.getDouble(Utils.LAT));
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/distance_category/?lon=%s&lat=%s&category=%s&offset=%s", lon, lat, category, offset));

        }
        return new CursorLoader(this, uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(myEventsAdapter.getItemCount() < data.getCount()) {
            setFlag(true);
        }
        myEventsAdapter.swapCursor(data);
        loaderWorking = loader.getId();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}

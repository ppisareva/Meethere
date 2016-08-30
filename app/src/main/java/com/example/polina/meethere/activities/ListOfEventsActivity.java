package com.example.polina.meethere.activities;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.polina.meethere.model.Event;

public class ListOfEventsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int STEP = 10;
    MyEventsAdapter myEventsAdapter;
    boolean highPrice = false;
    boolean lowPrice = false;

    TextView distance;
    TextView price;
    ImageView imageView;


    public static final int CATEGORY = 20330;
    public static final int BY_LOW_PRICE = 203000990;
    public static final int BY_HIGH_PRICE = 2090933430;

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    Boolean flag = true;
    int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        final int category = getIntent().getIntExtra(Utils.CATEGORY, -1);

//        distance =(TextView)findViewById(R.id.distance_filter);
//        price = (TextView)findViewById(R.id.price_filter);
//        imageView = (ImageView)findViewById(R.id.image_price);
        System.out.println(" category # " + category);
        Bundle arg = new Bundle();
        arg.putString(Utils.CATEGORY, category+"");
        arg.putString(Utils.OFFSET, offset+"");

        getSupportLoaderManager().initLoader(CATEGORY, arg, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        getSupportLoaderManager().restartLoader(CATEGORY, arg, ListOfEventsActivity.this);
                    }
                }
            }
        });
    }


//    public void onPrice(View v) {
//        distance.setTextColor(getResources().getColor(R.color.white));
//        price.setTextColor(getResources().getColor(R.color.filter));
//
//        if (highPrice || lowPrice) {
//            if (highPrice) {
//                imageView.setImageResource(R.drawable.ic_price_down_24dp);
//                lowPrice = true;
//                highPrice = false;
//
//                getSupportLoaderManager().initLoader(BY_HIGH_PRICE, null, this);
//            } else {
//
//                if (lowPrice) {
//                    imageView.setImageResource(R.drawable.ic_price_up_24dp);
//                    lowPrice = false;
//                    highPrice = true;
//                    getSupportLoaderManager().initLoader(BY_LOW_PRICE, null, this);
//                }
//            }
//        } else {
//
//            lowPrice = true;
//            imageView.setImageResource(R.drawable.ic_price_down_24dp);
//            getSupportLoaderManager().initLoader(BY_HIGH_PRICE, null, this);
//
//        }
//    }
//
//    public void onFilter (View v){
//        startActivity( new Intent(this, SearchFiltersActivity.class));
//
//    }
//

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
                Event.END, Event.TAGS,
                Event.PLACE, Event.ADDRESS,
                Event.AGE_MAX, Event.AGE_MIN,
                Event.BUDGET_MAX, Event.BUDGET_MIN, Event.LAT, Event.LNG};
        String category = args.getString(Utils.CATEGORY);
        String offset = args.getString(Utils.OFFSET);
        Uri uri = null;


        switch (id){
            case CATEGORY:
                uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/category/?category=%s&offset=%s",  category, offset));
                break;
//            case BY_LOW_PRICE:
//                uri =  Uri.parse("content://com.example.polina.meethere.data.data/low_price_search/" + search);
//                break;
//            case BY_HIGH_PRICE:
//                uri=    Uri.parse("content://com.example.polina.meethere.data.data/high_price_search/" + search);
//                break;

        }
        return new CursorLoader(this, uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(myEventsAdapter.getItemCount() < data.getCount()) {
            setFlag(true);
        }
        myEventsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




}

package com.example.polina.meethere.activities;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;

/**
 * Created by polina on 28.06.16.
 */
public class ListOfEventSearchActivity  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    MyEventsAdapter myEventsAdapter;
    boolean highPrice = false;
    boolean lowPrice = false;
    public static final int SEARCH_LOUDER = 20330;
    public static final int SEARCH_BY_LOW_PRICE = 203990;
    public static final int SEARCH_BY_HIGH_PRICE = 2033430;
    public static final int SEARCH_BY_DISTANCE = 20999430;



    TextView distance;
    TextView price;
    ImageView imageView;
    String search;
    App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        app =(App) getApplication();

        search = getIntent().getStringExtra(Utils.SEARCH);
        distance = (TextView) findViewById(R.id.distance_filter);
        price = (TextView) findViewById(R.id.price_filter);
        imageView = (ImageView) findViewById(R.id.image_price);

        Bundle arg = new Bundle();
        arg.putString(Utils.SEARCH, search);

        getSupportLoaderManager().initLoader(SEARCH_LOUDER, arg, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myEventsAdapter = new MyEventsAdapter(this);
        recyclerView.setAdapter(myEventsAdapter);
    }


    public void onDistance(View v) {
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
        getSupportLoaderManager().initLoader(SEARCH_BY_DISTANCE,arg , this);

    }

    public void onPrice(View v) {
        distance.setTextColor(getResources().getColor(R.color.white));
        price.setTextColor(getResources().getColor(R.color.filter));
        Bundle arg = new Bundle();
        arg.putString(Utils.SEARCH, search);
        if (highPrice || lowPrice) {
            if (highPrice) {
                imageView.setImageResource(R.drawable.ic_price_down_24dp);
                lowPrice = true;
                highPrice = false;

                getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, this);
            } else {

                if (lowPrice) {
                    imageView.setImageResource(R.drawable.ic_price_up_24dp);
                    lowPrice = false;
                    highPrice = true;
                    getSupportLoaderManager().initLoader(SEARCH_BY_LOW_PRICE, arg, this);
                }
            }
        } else {

            lowPrice = true;
            imageView.setImageResource(R.drawable.ic_price_down_24dp);
            getSupportLoaderManager().initLoader(SEARCH_BY_HIGH_PRICE, arg, this);

        }
    }

    public void onFilter(View v) {
        startActivity(new Intent(this, SearchFiltersActivity.class));

    }


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
        String arr[] =  new String[]{com.example.polina.meethere.model.Event.ID, com.example.polina.meethere.model.Event.NAME,
                com.example.polina.meethere.model.Event.DESCRIPTION, com.example.polina.meethere.model.Event.START,
                com.example.polina.meethere.model.Event.END, com.example.polina.meethere.model.Event.TAGS,
                com.example.polina.meethere.model.Event.PLACE, com.example.polina.meethere.model.Event.ADDRESS,
                com.example.polina.meethere.model.Event.AGE_MAX, com.example.polina.meethere.model.Event.AGE_MIN,
                com.example.polina.meethere.model.Event.BUDGET_MAX, com.example.polina.meethere.model.Event.BUDGET_MIN};
        String search = args.getString(Utils.SEARCH, "");
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
        return new CursorLoader(this, uri,arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        myEventsAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



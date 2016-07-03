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
import com.example.polina.meethere.Utils;

public class ListOfEventsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    MyEventsAdapter myEventsAdapter;
    boolean highPrice = false;
    boolean lowPrice = false;

    TextView distance;
    TextView price;
    ImageView imageView;


    public static final int CATEGORY = 20330;
    public static final int BY_LOW_PRICE = 203000990;
    public static final int BY_HIGH_PRICE = 2090933430;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        int category = getIntent().getIntExtra(Utils.CATEGORY, -1);

        distance =(TextView)findViewById(R.id.distance_filter);
        price = (TextView)findViewById(R.id.price_filter);
        imageView = (ImageView)findViewById(R.id.image_price);
        System.out.println(" category # " + category);
        Bundle arg = new Bundle();
        arg.putString(Utils.CATEGORY, category+"");

        getSupportLoaderManager().initLoader(CATEGORY, arg, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myEventsAdapter = new MyEventsAdapter(this);
        recyclerView.setAdapter(myEventsAdapter);
    }


    public void onPrice(View v) {
        distance.setTextColor(getResources().getColor(R.color.white));
        price.setTextColor(getResources().getColor(R.color.filter));

        if (highPrice || lowPrice) {
            if (highPrice) {
                imageView.setImageResource(R.drawable.ic_price_down_24dp);
                lowPrice = true;
                highPrice = false;

                getSupportLoaderManager().initLoader(BY_HIGH_PRICE, null, this);
            } else {

                if (lowPrice) {
                    imageView.setImageResource(R.drawable.ic_price_up_24dp);
                    lowPrice = false;
                    highPrice = true;
                    getSupportLoaderManager().initLoader(BY_LOW_PRICE, null, this);
                }
            }
        } else {

            lowPrice = true;
            imageView.setImageResource(R.drawable.ic_price_down_24dp);
            getSupportLoaderManager().initLoader(BY_HIGH_PRICE, null, this);

        }
    }

    public void onFilter (View v){
        startActivity( new Intent(this, SearchFiltersActivity.class));

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
        String category = args.getString(Utils.CATEGORY);
        Uri uri = null;


        switch (id){
            case CATEGORY:
                uri =   Uri.parse("content://com.example.polina.meethere.data.data/category/" + category);
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
        if(data==null){

        }
       myEventsAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}

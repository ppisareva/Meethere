package com.example.polina.meethere.activities;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AbstractMeethereActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    LatLng pin;
    App app;
    MapFragment mapFragment;
    ProgressBar progressBar;
    public static final int SEARCH_BY_DISTANCE = 20999430;
    List<Event> listOfEvents;
    LatLng eventLocation;
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int START = 3;
    private static final int LAT = 8;
    private static final int LNG = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        app = (App) getApplication();
        Location location = app.getCurrentLocation();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pin = new LatLng(location.getLatitude(), location.getLongitude());



        if(getIntent().hasExtra(Event.LAT)){
            eventLocation  = new LatLng( getIntent().getDoubleExtra(Event.LNG, 0), getIntent().getDoubleExtra(Event.LAT, 0));
        } else {
            Bundle bundle = new Bundle();
            bundle.putDouble(Utils.LON, location.getLongitude());
            bundle.putDouble(Utils.LAT, location.getLatitude());
            getSupportLoaderManager().initLoader(SEARCH_BY_DISTANCE, bundle, this);

        }
        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();




        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 15));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);
                if(eventLocation!=null) {
                    mMap.addMarker(new MarkerOptions().position(eventLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15));
                }

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (Event e : listOfEvents) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.getLat(), e.getLng()))
                    .title(e.getName()));
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] = new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START, Event.TAGS,
                Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES};
        String lon = String.valueOf(args.getDouble(Utils.LON));
        String lat = String.valueOf(args.getDouble(Utils.LAT));
        Uri uri = Uri.parse(String.format("content://com.example.polina.meethere.data.data/distance_search/?lon=%s&lat=%s", lon, lat));
        return new CursorLoader(this, uri, arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        System.out.println(data.getCount());
        listOfEvents = new ArrayList();
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            Event event = new Event();
            if (data.getDouble(LNG) != 0 && data.getDouble(LAT) != 0) {
                event.setId(data.getString(ID));
                event.setName(data.getString(NAME));
                event.setStart(data.getString(START));
                event.setLat(data.getDouble(LAT));
                event.setLng(data.getDouble(LNG));
                listOfEvents.add(event);
            }
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}

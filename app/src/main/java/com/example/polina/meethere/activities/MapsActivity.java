package com.example.polina.meethere.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AbstractMeethereActivity implements OnMapReadyCallback , LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    LatLng pin;
    App app;
    SupportMapFragment mapFragment;
    public static final int SEARCH_BY_DISTANCE = 20999430;
    List<Event> listOfEvents ;
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int START = 3;
    private static final int LAT= 8;
    private static final int LNG = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        app= (App) getApplication();
        Location location = app.getCurrentLocation();

            pin = new LatLng(location.getLatitude(), location.getLongitude());


       mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);



        Bundle bundle = new Bundle();
        bundle.putDouble(Utils.LON,location.getLongitude());
        bundle.putDouble(Utils.LAT,location.getLatitude());
        getSupportLoaderManager().initLoader(SEARCH_BY_DISTANCE, bundle, this);





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng cameraTo =   new LatLng(listOfEvents.get(0).getLat(), listOfEvents.get(0).getLng());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 15));


        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);
        for(Event e: listOfEvents){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.getLat(), e.getLng()))
                    .title(e.getName()));
        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String arr[] =  new String[]{Event.ID, Event.NAME,
                Event.DESCRIPTION, Event.START, Event.TAGS,
                Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES};

        String lon = String.valueOf(args.getDouble(Utils.LON));
        String lat = String.valueOf(args.getDouble(Utils.LAT));

      Uri uri =   Uri.parse(String.format("content://com.example.polina.meethere.data.data/distance_search/?lon=%s&lat=%s", lon, lat));
        return new CursorLoader(this, uri, arr, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        System.out.println(data.getCount());
        listOfEvents = new ArrayList();
        for(data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {

            Event event = new Event();
            if(data.getDouble(LNG)!=0 &&data.getDouble(LAT)!=0) {
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


}

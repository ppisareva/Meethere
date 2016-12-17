package com.example.polina.meethere.activities;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends AbstractMeethereActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private GoogleMap mMap;
    LatLng pin;
    App app;
    MapFragment mapFragment;
    public static final int SEARCH_BY_DISTANCE = 20999430;
    List<Event> listOfEvents;
    LatLng eventLocation;
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int START = 3;
    private static final int LAT = 8;
    private static final int LNG = 9;
    Map<Marker,String > mapOfMarkers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        app = (App) getApplication();
        Location location = app.getCurrentLocation();
        if (location == null) {
            Toast.makeText(this, "eable GEO location", Toast.LENGTH_LONG).show();
            askPermission();
        }

            loadMap(location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


    }



    private void loadMap(Location location) {
        if(location==null) return;
        pin = new LatLng(location.getLatitude(), location.getLongitude());
        if (getIntent().hasExtra(Event.LAT)) {
            eventLocation = new LatLng(getIntent().getDoubleExtra(Event.LNG, 0), getIntent().getDoubleExtra(Event.LAT, 0));
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
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin)).position(eventLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15));
                }


            }
        });
    }

    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermission();


        } else {

            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);
        }
    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(MapsActivity.this, getString(R.string.req_permission), Toast.LENGTH_SHORT)
                    .show();
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               loadMap(app.getCurrentLocation());
            } else {
                Toast.makeText(MapsActivity.this, getString(R.string.deny_permission), Toast.LENGTH_SHORT)
                        .show();
            }
        }
        return;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (Event e : listOfEvents) {

            Marker marker =  mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.getLat(), e.getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                    .title(e.getName()).snippet(Utils.parseData(e.getStart())));
            mapOfMarkers.put(marker, e.getId());
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String idEvent = mapOfMarkers.get(marker);
                Intent intent = new Intent(MapsActivity.this, EventActivity.class);
                intent.putExtra(Utils.EVENT_ID, idEvent);
                startActivity(intent);

            }
       
        });


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
        if(data==null){
            Toast.makeText(MapsActivity.this, getString(R.string.on_internet_connection), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

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

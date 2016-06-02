package com.example.polina.meethere.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.polina.meethere.R;
import com.example.polina.meethere.model.App;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;


public class NewEventLocationFragment extends android.support.v4.app.Fragment {

    MapView mapView;
    GoogleMap map;
    EditText adress;
    App application;
    Collection<Double> pin;

    public static NewEventLocationFragment newInstance() {
        NewEventLocationFragment fragment = new NewEventLocationFragment();
        return fragment;
    }

    public NewEventLocationFragment() {
        // Required empty public constructor
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_event_location, container, false);
        setHasOptionsMenu(true);
        application = (App) getActivity().getApplication();
        mapView = (MapView) v.findViewById(R.id.map_view);
        adress = (EditText) v.findViewById(R.id.add_adress);
        adress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        adress.setOnFocusChangeListener(onFocusChangeListener);

        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        Location location = application.getCurrentLocation();
        if (location != null && map!=null ) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setMyLocationEnabled(true);


            // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Updates the location and zoom of the MapView

            map.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()));
            map.addMarker(markerOptions);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    map.clear();
                    map.addMarker(new MarkerOptions()
                            .position(latLng));
                    adress.setText(latLng.latitude + ", " + latLng.longitude);
                    pin = new ArrayList<Double>();

                    pin.add(latLng.longitude);
                    pin.add(latLng.latitude);

                }
            });
        }

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event_mid, menu);
    }

    public String getAddress (){
        return adress.getText().toString();
    }

    public Collection<Double> getLocation(){
        return pin;
    }

}

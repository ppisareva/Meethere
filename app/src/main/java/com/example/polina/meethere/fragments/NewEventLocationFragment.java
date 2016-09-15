package com.example.polina.meethere.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.polina.meethere.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;


public class NewEventLocationFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    EditText adressView;
    App application;
    Collection<Double> pin;
    String address = "";
    Double lat;
    Double lng;


    public static NewEventLocationFragment newInstance(String a, Double latitude, Double longtitude) {
        NewEventLocationFragment fragment = new NewEventLocationFragment();
        Bundle b = new Bundle();
        b.putString(Event.ADDRESS, a);
        b.putDouble(Event.LAT, latitude);
        b.putDouble(Event.LNG, longtitude);
        fragment.setArguments(b);
        return fragment;
    }

    public NewEventLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            address = getArguments().getString(Event.ADDRESS);
            lat = getArguments().getDouble(Event.LAT);
            lng = getArguments().getDouble(Event.LNG);
        }
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
        adressView = (EditText) v.findViewById(R.id.add_adress);
        adressView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        adressView.setOnFocusChangeListener(onFocusChangeListener);
        if(address!=null){
            adressView.setText(address);
        }

        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);



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
        return adressView.getText().toString();
    }

    public Collection<Double> getLocation(){
        return pin;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location = application.getCurrentLocation();
        map = googleMap;
        if ( map!=null ) {
            MarkerOptions markerOptions = new MarkerOptions();

            if(location!=null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
                markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            }
           //  Needs to call MapsInitializer before doing any CameraUpdateFactory calls
            try {
                MapsInitializer.initialize(this.getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Updates the location and zoom of the MapView
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);

//            if(lat!=0&&lng!=0){
//                markerOptions.position(new LatLng(lat,lng));
//            }
//            map.addMarker(markerOptions);

            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    map.clear();
                    map.addMarker(new MarkerOptions()
                            .position(latLng));
                    adressView.setText(latLng.latitude + ", " + latLng.longitude);
                    pin = new ArrayList<Double>();

                    pin.add(latLng.longitude);
                    pin.add(latLng.latitude);

                }
            });
        }
    }
}

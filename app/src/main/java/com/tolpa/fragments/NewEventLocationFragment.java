package com.tolpa.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.tolpa.R;
import com.tolpa.model.App;
import com.tolpa.model.Event;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


public class NewEventLocationFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    EditText adressView;
    App application;
    Collection<Double> pin;
    String address = "";
    Double lat;
    Double lng;
    ProgressBar progressBar;


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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.new_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        mapView = (MapView) v.findViewById(R.id.map_view);
        adressView = (EditText) v.findViewById(R.id.add_adress);
//        adressView.setOnFocusChangeListener(onFocusChangeListener);
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



    public String getAddress (){
        String text = adressView.getText().toString();
        if (text.matches("")) {
            return "see pin on map";
        }
        return text;
    }

    public void progressBarOn(){
        if(progressBar==null) return;
        progressBar.setVisibility(View.VISIBLE);

    }

    public void progressOff(){
        if(progressBar==null) return;
        progressBar.setVisibility(View.GONE);
    }


    public Collection<Double> getLocation(){
        return pin;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location = null;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            location = application.getCurrentLocation();
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
            if (location != null) {
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.setMyLocationEnabled(true);
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);
            }



            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    map.clear();
                    map.addMarker(new MarkerOptions()
                            .position(latLng));
//                    address = latLng.latitude + ", " + latLng.longitude;
//                    adressView.setHint("ввыедены координаты");
                    pin = new ArrayList<Double>();
                    pin.add(latLng.longitude);
                    pin.add(latLng.latitude);
                    new GetAddress().execute(latLng);

                }
            });
        }
    }

    private class GetAddress extends AsyncTask<LatLng, Void, String> {
        @Override
        protected String doInBackground(LatLng... params) {
            Geocoder geocoder;
            List<Address> addresses = new ArrayList<>();
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            LatLng latLng = params[0];

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses.size() == 0) return "";
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                return (!TextUtils.equals(address, null) ? address : "") + ", " +(!TextUtils.equals(city, null) ? city : "")  ;// Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";

        }

        @Override
        protected void onPostExecute(String s) {
            adressView.setText(s);
        }
    }
}


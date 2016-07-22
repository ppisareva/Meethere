package com.example.polina.meethere.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;


/**
 * Created by ko3a4ok on 5/7/16.
 */
public class App extends Application {

    private ServerApi serverApi = new ServerApi();
    private UserProfile userProfile = null;



    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        userProfile = UserProfile.init(pref());
        if (userProfile != null)
            serverApi.setAuthToken(userProfile.getAccessToken());

    }

    public ServerApi getServerApi() {
        return serverApi;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void saveUserProfile(JSONObject o) throws JSONException {
        SharedPreferences.Editor editor = pref().edit();
        editor.putInt(UserProfile.USER_ID, o.optInt(UserProfile.USER_ID));
        editor.putString(UserProfile.FIRST_NAME, o.optString(UserProfile.FIRST_NAME));
        editor.putString(UserProfile.LAST_NAME, o.optString(UserProfile.LAST_NAME));
        editor.putString(UserProfile.PROFILE_URL, o.optString(UserProfile.PROFILE_URL));
        editor.putString(UserProfile.MINI_PROFILE_URL, o.optString(UserProfile.MINI_PROFILE_URL));
        editor.putString(UserProfile.LOCATION, o.optString(UserProfile.LOCATION));
        if (o.has(UserProfile.TOKEN))
            editor.putString(UserProfile.TOKEN, o.optString(UserProfile.TOKEN));
        editor.putString(UserProfile.PHONE, o.optString(UserProfile.PHONE));
        editor.putString(UserProfile.EMAIL, o.optString(UserProfile.EMAIL));
        editor.putString(UserProfile.BIRTHDAY, o.optString(UserProfile.BIRTHDAY));
        editor.putBoolean(UserProfile.GENDER, o.optBoolean(UserProfile.GENDER));
        editor.putInt(UserProfile.FOLLOWERS, o.optInt(UserProfile.FOLLOWERS));
        editor.putInt(UserProfile.FOLLOWINGS, o.optInt(UserProfile.FOLLOWINGS));
        editor.putBoolean(UserProfile.FOLLOW, o.optBoolean(UserProfile.FOLLOW));
        JSONArray array = o.optJSONArray(UserProfile.CATEGORY);
        Set<String > set = null;
       if(array!=null){
           for(int i = 0; i<array.length();i++ ){
               set.add(""+array.get(i));
           }
       }
        editor.putStringSet(UserProfile.CATEGORY,set);

        editor.apply();
        userProfile = UserProfile.init(pref());
        serverApi.setAuthToken(userProfile.getAccessToken());
    }

    public SharedPreferences pref() {
        return getSharedPreferences("pref", MODE_PRIVATE);
    }

    public void logout() {
        userProfile = null;
        pref().edit().clear().commit();
    }

    public Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, mLocationListener);

        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 1, mLocationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return location;
    }
}

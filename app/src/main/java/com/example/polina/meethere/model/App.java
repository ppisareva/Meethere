package com.example.polina.meethere.model;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.multidex.MultiDexApplication;

import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by ko3a4ok on 5/7/16.
 */
public class App extends MultiDexApplication {

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

    public void utdateUserProfileIfExist(JSONObject o){


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
        Set<String > set = new HashSet<>( );
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

    public Bitmap decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getApplicationContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFileDescriptor(imageSource, null, o2);


        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                   return null;
                }
        }
    }
}

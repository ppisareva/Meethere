package com.example.polina.meethere.model;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONObject;


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
    }

    public ServerApi getServerApi() {
        return serverApi;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void saveUserProfile(JSONObject o) {
        SharedPreferences.Editor editor = pref().edit();
        editor.putInt(UserProfile.USER_ID, o.optInt(UserProfile.USER_ID));
        editor.putString(UserProfile.FIRST_NAME, o.optString(UserProfile.FIRST_NAME));
        editor.putString(UserProfile.LAST_NAME, o.optString(UserProfile.LAST_NAME));
        editor.putString(UserProfile.PROFILE_URL, o.optString(UserProfile.PROFILE_URL));
        editor.putString(UserProfile.MINI_PROFILE_URL, o.optString(UserProfile.MINI_PROFILE_URL));
        editor.putString(UserProfile.LOCATION, o.optString(UserProfile.LOCATION));
        editor.apply();
        userProfile = UserProfile.init(pref());
    }

    public SharedPreferences pref() {
        return getSharedPreferences("pref", MODE_PRIVATE);
    }
}

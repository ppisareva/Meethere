package com.example.polina.meethere.model;

import android.content.SharedPreferences;

import com.crittercism.app.Crittercism;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class UserProfile {
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_URL = "profile_image_url";
    public static final String MINI_PROFILE_URL = "profile_mini_image_url";
    public static final String USER_ID = "user_id";
    public static final String LOCATION = "location";
    public static final String TOKEN = "token";


    private int id;
    private String firstName;
    private String lastName;
    private String profileUrl;
    private String miniProfileUrl;
    private String location;
    private String accessToken;

    public static UserProfile init(SharedPreferences pref) {
        try {
            if (!pref.contains(USER_ID)) return null;
            UserProfile up = new UserProfile();
            up.firstName = pref.getString(FIRST_NAME, null);
            up.lastName = pref.getString(LAST_NAME, null);
            up.profileUrl = pref.getString(PROFILE_URL, null);
            up.miniProfileUrl = pref.getString(MINI_PROFILE_URL, null);
            up.location = pref.getString(LOCATION, null);
            up.accessToken = pref.getString(TOKEN, null);
            up.id = pref.getInt(USER_ID, -1);
            return up;
        }catch (Exception e){
            Crittercism.logHandledException(e);
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getMiniProfileUrl() {
        return miniProfileUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

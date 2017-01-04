package com.tolpa.model;

import android.content.SharedPreferences;
import android.text.TextUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class UserProfile {
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PROFILE_URL = "profile_url";
    public static final String MINI_PROFILE_URL = "mini_profile_url";
    public static final String USER_ID = "user_id";
    public static final String LOCATION = "location";
    public static final String TOKEN = "token";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String FOLLOWERS= "followers_count";
    public static final String FOLLOWINGS= "followings_count";
    public static final String FOLLOW= "follow";
    public static final String CATEGORY= "categories";




    private int id;
    private String firstName;
    private String lastName;
    private String profileUrl;
    private String miniProfileUrl;
    private String location;
    private String accessToken;
    private String phone;
    private String email;
    private String birthday;
    private boolean gender;
    private int followers;
    private int followings;
    private boolean follow;
    private Set<String> category;





    public static UserProfile init(SharedPreferences pref) {
        try {
            if (!pref.contains(USER_ID)) return null;
            if (TextUtils.isEmpty(pref.getString(TOKEN, null)))return null;
            UserProfile up = new UserProfile();
            up.firstName = pref.getString(FIRST_NAME, null);
            up.lastName = pref.getString(LAST_NAME, null);
            up.profileUrl = pref.getString(PROFILE_URL, null);
            up.miniProfileUrl = pref.getString(MINI_PROFILE_URL, null);
            up.location = pref.getString(LOCATION, null);
            up.accessToken = pref.getString(TOKEN, null);
            up.id = pref.getInt(USER_ID, -1);
            up.phone = pref.getString(PHONE, null);
            up.email = pref.getString(EMAIL, null);
            up.birthday = pref.getString(BIRTHDAY, null);
            up.gender = pref.getBoolean(GENDER, true);
            up.followers = pref.getInt(FOLLOWERS, -1);
            up.followings = pref.getInt(FOLLOWINGS, -1);
            up.follow = pref.getBoolean(FOLLOW, false);
            up.category = pref.getStringSet(CATEGORY, null);


            return up;
        }catch (Exception e){
            return null;
        }
    }

    public static UserProfile parseUserProfile(JSONObject o)  {
       UserProfile userProfile = new UserProfile();
        userProfile.setId(o.optInt(UserProfile.USER_ID));
        userProfile.setFirstName(o.optString(UserProfile.FIRST_NAME));
        userProfile.setLastName(o.optString(UserProfile.LAST_NAME));
        userProfile.setProfileUrl(o.optString(UserProfile.PROFILE_URL));
        userProfile.setMiniProfileUrl(o.optString(UserProfile.MINI_PROFILE_URL));
        userProfile.setLocation(o.optString(UserProfile.LOCATION));
        userProfile.setPhone(o.optString(UserProfile.PHONE));
        userProfile.setEmail(o.optString(UserProfile.EMAIL));
        userProfile.setBirthday( o.optString(UserProfile.BIRTHDAY));
        userProfile.setGender( o.optBoolean(UserProfile.GENDER));
        userProfile.setFollowers(o.optInt(UserProfile.FOLLOWERS));
        userProfile.setFollowings( o.optInt(UserProfile.FOLLOWINGS));
        userProfile.setFollow(o.optBoolean(UserProfile.FOLLOW));
        JSONArray array = o.optJSONArray(UserProfile.CATEGORY);
        Set<String> set = new HashSet<>();
        if(array!=null){
            for(int i = 0; i<array.length();i++ ){
                try {
                    set.add(""+array.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        userProfile.setCategory(set);
        return userProfile;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setMiniProfileUrl(String miniProfileUrl) {
        this.miniProfileUrl = miniProfileUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public void setCategory(Set<String> category) {
        this.category = category;
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

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public Set<String> getCategory() {
        return category;
    }

    public boolean isFollow() {
        return follow;
    }

    public int getFollowers() {
        return followers;
    }

    public boolean isGender() {
        return gender;
    }

    public int getFollowings() {
        return followings;
    }
}

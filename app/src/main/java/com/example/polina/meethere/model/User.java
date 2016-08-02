package com.example.polina.meethere.model;

import org.json.JSONObject;

/**
 * Created by polina on 22.06.16.
 */
public class User {
    public static final String ID = "user_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String IMAGE = "mini_profile_url";



    String id;
    String firstName;
    String lastName;
    String image;

    public static User parseUser(JSONObject jsonObject){
        User user = new User();

        user.setId(jsonObject.optString(ID));
        user.setFirstName(jsonObject.optString(FIRST_NAME));
        user.setLastName(jsonObject.optString(LAST_NAME));
        user.setImage(jsonObject.optString(IMAGE));
        return user;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

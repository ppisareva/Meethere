package com.example.polina.meethere.model;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.adapters.*;
import com.example.polina.meethere.adapters.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by polina on 27.07.16.
 */
public class Feed {

    User user;
    String type;
    String time;
    com.example.polina.meethere.adapters.Event event;
    public static final String RESULTS = "results";

    public static final String USER = "user";
    public static final String TYPE = "type";
    public static final String TIME = "time";
    public static final String EVENT = "event";


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public static List<Feed> parseFeed(JSONObject hh) {

            List<Feed> list = new ArrayList<>();
            JSONArray arr = new JSONArray();
            try {
                arr = hh.getJSONArray(RESULTS);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject feedJSON = arr.getJSONObject(i);
                    if(!feedJSON.getString(TYPE).equals(Utils.FOLLOW)) {
                        list.add(parseJSON(feedJSON));
                    }

                }


                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

    private static Feed parseJSON(JSONObject jsonObject) {
        Feed feed = new Feed();
        feed.setEvent(new Event());
        try {
            feed.setTime(jsonObject.getString(TIME));
            feed.setType(jsonObject.getString(TYPE));
            feed.setUser(User.parseUser(jsonObject.getJSONObject(USER)));
            if(jsonObject.has(EVENT)) {
                feed.setEvent(Event.parseEvent(jsonObject.getJSONObject(EVENT)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  feed;
    }

}

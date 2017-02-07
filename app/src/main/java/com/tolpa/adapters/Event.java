package com.tolpa.adapters;

import com.tolpa.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by polina on 08.03.16.
 */
public class Event {

    private String name;
    private String description;
    private String start;
    private int budget_min;
    private String id;
    private String imageUrl;

    public static final String START = "start";
    public static final String BUDGET = "budget_min";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";

    public static Event parseEvent(JSONObject jsonObject) {
        Event event = new Event();
        try {
            event.setId(jsonObject.getString(ID));
            event.setBudget_min(jsonObject.getInt(BUDGET));
            event.setName(jsonObject.getString(NAME));
            event.setDescription(jsonObject.getString(DESCRIPTION));
            event.setStart(jsonObject.getString(START));
            event.imageUrl = jsonObject.optString(Utils.MINI_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getBudget_min() {
        return budget_min;
    }

    public void setBudget_min(int budget_min) {
        this.budget_min = budget_min;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

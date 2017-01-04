package com.tolpa.model;

import com.tolpa.Utils;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by polina on 03.07.16.
 */
public class Comment {

    private String id;
    private String text;
    private String createdBy;
    private int createdById;
    private String createdByUrl;
    private long createdAt;

    public Comment(JSONObject o) {
        id = o.optString("id");
        text = o.optString("text");
        JSONObject user = o.optJSONObject("user");
        createdBy = user.optString("first_name") + " " + user.optString("last_name");
        createdById = user.optInt("user_id", 0);
        createdByUrl = user.optString("mini_profile_url");
        try {
            createdAt = Utils.INPUT_FORMAT.parse(o.optString("created_at")).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdByUrl='" + createdByUrl + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByUrl() {
        return createdByUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }


    public int getCreatedById() {
        return createdById;
    }
}

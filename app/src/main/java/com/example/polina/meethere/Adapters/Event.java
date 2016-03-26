package com.example.polina.meethere.Adapters;

import java.util.Date;

/**
 * Created by polina on 08.03.16.
 */
public class Event {
    private int photo;
    private String name;
    private String details;
    private Date date;

    public Event(int photo, String name, String details, Date date) {
        this.photo = photo;
        this.name = name;
        this.details = details;
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

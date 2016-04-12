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
    private String rating;
    private String budget;

    public Event(int photo, String name, String details, Date date, String rating, String budget) {
        this.photo = photo;
        this.name = name;
        this.details = details;
        this.date = date;
        this.rating = rating;
        this.budget = budget;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
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

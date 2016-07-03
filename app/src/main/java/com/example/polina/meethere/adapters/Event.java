package com.example.polina.meethere.adapters;

import java.util.Date;

/**
 * Created by polina on 08.03.16.
 */
public class Event {
    private int photo;
    private String name;
    private String description;
    private Date date;
    private String rating;
    private String budget;

    public Event(int photo, String name, String details, Date date, String rating, String budget) {
        this.photo = photo;
        this.name = name;
        this.description = details;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

package com.tolpa.model;

import java.util.List;

/**
 * Created by polina on 24.05.16.
 */
public class Event {


    public static final String ID = "id";
    public static final String ID_USER = "user_id";
    public static final String CREATED_BY = "created_by";

    public static final String NAME = "name";
    public static final String DESCRIPTION =  "description";
    public static final String  START="start";
    public static final String END =  "end";
    public static final String   PLACE =     "place";
    public static final String  ADDRESS   =  "address";
    public static final String   AGE_MIN=     "age_min";
    public static final String    AGE_MAX =   "age_max";
    public static final String   BUDGET_MIN=     "budget_min";
    public static final String    BUDGET_MAX =  "budget_max";
    public static final String   TAGS =     "tags";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String JOINED = "joined";
    public static final String ATTENDANCES = "attenders_count";



    String id;
    int userId;
    String name;
    String description;
    String start;
    String end;
    List<Double> place;
    String address;
    int ageMin;
    Double lat;
    Double lng;
    int ageMax;
    int budgetMin;
    int budgetMax;
    List<Integer> tag;
    Boolean join;
    int attendances;
    User createdBy;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAttendances() {
        return attendances;
    }

    public void setAttendances(int attendances) {
        this.attendances = attendances;
    }

    public Boolean getJoin() {
        return join;
    }

    public void setJoin(Boolean join) {
        this.join = join;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<Double> getPlace() {
        return place;
    }

    public void setPlace(List<Double> place) {
        this.place = place;
        lat = place.get(0);
        lng = place.get(1);
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getBudgetMin() {
        return budgetMin;
    }

    public void setBudgetMin(int budgetMin) {
        this.budgetMin = budgetMin;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(int budgetMax) {
        this.budgetMax = budgetMax;
    }

    public List<Integer> getTag() {
        return tag;
    }

    public void setTag(List<Integer> tag) {
        this.tag = tag;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}

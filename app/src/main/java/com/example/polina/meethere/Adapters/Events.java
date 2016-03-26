package com.example.polina.meethere.Adapters;

import java.util.List;

/**
 * Created by polina on 09.03.16.
 */
public class Events  {
    String category;
    List<Event> eventList;

    public Events(String category, List<Event> eventList) {
        this.category = category;
        this.eventList = eventList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}

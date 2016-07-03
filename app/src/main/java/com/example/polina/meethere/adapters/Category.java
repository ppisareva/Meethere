package com.example.polina.meethere.adapters;


/**
 * Created by polina on 09.03.16.
 */
public class Category {
    int id;
    String name;

    public Category(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getId() {
        return id;
    }

}

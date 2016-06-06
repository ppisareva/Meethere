package com.example.polina.meethere.Adapters;

import android.database.Cursor;

import java.util.List;

/**
 * Created by polina on 09.03.16.
 */
public class Events  {
    String category;
    Cursor cursor;

    public Events(String category,Cursor cursor) {
        this.category = category;
        this.cursor = cursor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }
}

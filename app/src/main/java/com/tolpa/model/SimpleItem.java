package com.tolpa.model;

/**
 * Created by polina on 11.04.16.
 */
public class SimpleItem {

    int image;
    String name;

    public SimpleItem(int image, String name) {
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String category) {
        this.name = category;
    }
}

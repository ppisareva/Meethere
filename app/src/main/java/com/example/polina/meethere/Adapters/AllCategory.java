package com.example.polina.meethere.Adapters;

/**
 * Created by polina on 11.04.16.
 */
public class AllCategory {

    int image;
    String category;

    public AllCategory(int image, String category) {
        this.image = image;
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

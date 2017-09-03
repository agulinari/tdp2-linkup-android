package com.tddp2.grupo2.linkup.model;

import android.graphics.Bitmap;



public class Image {

    private String fbid;
    private Bitmap image;
    private int order;

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

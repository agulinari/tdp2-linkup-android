package com.tddp2.grupo2.linkup.model;

import android.graphics.Bitmap;

import java.io.Serializable;


public class Image implements Serializable{

    private String fbid;
    private String image; //base64
    private int order;

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

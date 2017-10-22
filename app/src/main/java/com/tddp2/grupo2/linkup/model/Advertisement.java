package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Advertisement implements Serializable, Link {

    private String advertiser;
    private String url;
    private String image;

    public String getFbid() {
        return "";
    }

    public String getAdvertiser() {
        return this.advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

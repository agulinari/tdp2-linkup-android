package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Image implements Serializable{

    private String idImage;
    private String data; //base64

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

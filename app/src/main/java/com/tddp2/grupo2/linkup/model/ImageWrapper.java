package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;

public class ImageWrapper implements Serializable {

    private Image image;

    public ImageWrapper(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}

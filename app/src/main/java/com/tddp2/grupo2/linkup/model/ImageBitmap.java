package com.tddp2.grupo2.linkup.model;


import android.graphics.Bitmap;

public class ImageBitmap {

    private String imageId;
    private Bitmap bitmap;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

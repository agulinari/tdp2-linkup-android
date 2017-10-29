package com.tddp2.grupo2.linkup.model;


import java.util.List;

public class Images {

    private List<ImageBitmap> images;
    private boolean alreadyUpdatedFromServer;

    public List<ImageBitmap> getImages() {
        return images;
    }

    public void setImages(List<ImageBitmap> images) {
        this.images = images;
    }

    public boolean isAlreadyUpdatedFromServer() {
        return alreadyUpdatedFromServer;
    }

    public void setAlreadyUpdatedFromServer(boolean alreadyUpdatedFromServer) {
        this.alreadyUpdatedFromServer = alreadyUpdatedFromServer;
    }

    public Images(List<ImageBitmap> images, boolean alreadyUpdatedFromServer) {
        this.images = images;
        this.alreadyUpdatedFromServer = alreadyUpdatedFromServer;
    }
}

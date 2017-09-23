package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Image;

import java.io.Serializable;
import java.util.List;

public class ImageResponse implements Serializable {

    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}

package com.tddp2.grupo2.linkup.infrastructure.client.request;


import com.tddp2.grupo2.linkup.model.Recommend;

import java.io.Serializable;

public class RecommendRequest implements Serializable {

    private Recommend recommend;

    public RecommendRequest(Recommend recommend) {
        this.recommend = recommend;
    }

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }
}

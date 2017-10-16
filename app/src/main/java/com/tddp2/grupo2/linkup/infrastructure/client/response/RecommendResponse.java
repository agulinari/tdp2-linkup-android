package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Recommend;

import java.io.Serializable;

public class RecommendResponse implements Serializable{

    private Recommend recommend;

    public RecommendResponse(Recommend recommend) {
        this.recommend = recommend;
    }

    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }
}

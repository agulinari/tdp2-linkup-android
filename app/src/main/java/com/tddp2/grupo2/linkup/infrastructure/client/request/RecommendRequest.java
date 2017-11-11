package com.tddp2.grupo2.linkup.infrastructure.client.request;


import com.tddp2.grupo2.linkup.model.Recommendation;

import java.io.Serializable;

public class RecommendRequest implements Serializable {

    private Recommendation recommendation;

    public RecommendRequest(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }
}

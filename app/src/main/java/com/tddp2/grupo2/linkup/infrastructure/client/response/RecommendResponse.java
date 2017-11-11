package com.tddp2.grupo2.linkup.infrastructure.client.response;


import com.tddp2.grupo2.linkup.model.Recommendation;

import java.io.Serializable;

public class RecommendResponse implements Serializable{

    private Recommendation recommendation;

    public RecommendResponse(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }
}

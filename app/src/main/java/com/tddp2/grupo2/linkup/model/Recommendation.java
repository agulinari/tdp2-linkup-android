package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Recommendation implements Serializable {

    private String idFromUser;
    private String idToUser;
    private String idRecommendedUser;

    public String getIdFromUser() {
        return idFromUser;
    }

    public void setIdFromUser(String idFromUser) {
        this.idFromUser = idFromUser;
    }

    public String getIdToUser() {
        return idToUser;
    }

    public void setIdToUser(String idToUser) {
        this.idToUser = idToUser;
    }

    public String getIdRecommendedUser() {
        return idRecommendedUser;
    }

    public void setIdRecommendedUser(String idRecommendedUser) {
        this.idRecommendedUser = idRecommendedUser;
    }
}

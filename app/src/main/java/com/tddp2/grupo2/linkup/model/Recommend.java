package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Recommend implements Serializable {

    private String idRecommendedUser;
    private String idReceiverUser;

    public String getIdRecommendedUser() {
        return idRecommendedUser;
    }

    public void setIdRecommendedUser(String idRecommendedUser) {
        this.idRecommendedUser = idRecommendedUser;
    }

    public String getIdReceiverUser() {
        return idReceiverUser;
    }

    public void setIdReceiverUser(String idReceiverUser) {
        this.idReceiverUser = idReceiverUser;
    }
}

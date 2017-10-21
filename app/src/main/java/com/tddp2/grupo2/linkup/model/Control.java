package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Control implements Serializable {
    //private boolean isActive = true;
    private Boolean isPremium = null;
    //private String token = "";
    //private int availableSuperlinks = 5;
    //deactivationTime { type : Date, default: null }

   /* public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }*/

    public boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    /*public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAvailableSuperlinks() {
        return availableSuperlinks;
    }

    public void setAvailableSuperlinks(int availableSuperlinks) {
        this.availableSuperlinks = availableSuperlinks;
    }*/
}

package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Settings implements Serializable {

    private int maxDistance = 1;
    private int minAge = 18;
    private int maxAge = 99;
    private boolean onlyFriends = false;
    private boolean searchFemales = false;
    private boolean searchMales = false;
    private boolean notifications = false;
    private boolean invisible = false ;
    private boolean blockAds = false;

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isOnlyFriends() {
        return onlyFriends;
    }

    public void setOnlyFriends(boolean onlyFriends) {
        this.onlyFriends = onlyFriends;
    }

    public boolean isSearchFemales() {
        return searchFemales;
    }

    public void setSearchFemales(boolean searchFemales) {
        this.searchFemales = searchFemales;
    }

    public boolean isSearchMales() {
        return searchMales;
    }

    public void setSearchMales(boolean searchMales) {
        this.searchMales = searchMales;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isBlockAds() {
        return blockAds;
    }

    public void setBlockAds(boolean blockAds) {
        this.blockAds = blockAds;
    }
}

package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;


public class Settings implements Serializable {

    private int maxDistance;
    private int minAge;
    private int maxAge;
    private boolean onlyFriends;
    private boolean searchFemales;
    private boolean searchMales;
    private boolean notifications;
    private boolean invisible;

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
}

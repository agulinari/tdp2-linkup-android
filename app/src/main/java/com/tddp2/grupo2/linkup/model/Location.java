package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;

public class Location implements Serializable {
    private double latitude;
    private double longitude;

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLatitude() {return this.latitude;}

    public void setLongitude(double longitude) {this.longitude = longitude;}

    public double getLongitude() {return this.longitude;}
}

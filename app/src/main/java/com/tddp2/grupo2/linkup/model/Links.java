package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Links implements Serializable{

    private List<Link> links = new ArrayList<Link>();
    private int availableSuperlinks;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public int getAvailableSuperlinks() {
        return availableSuperlinks;
    }

    public void setAvailableSuperlinks(int availableSuperlinks) {
        this.availableSuperlinks = availableSuperlinks;
    }
}

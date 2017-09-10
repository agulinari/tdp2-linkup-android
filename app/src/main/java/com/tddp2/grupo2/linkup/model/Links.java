package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Links implements Serializable{

    private List<Profile> links = new ArrayList<Profile>();

    public List<Profile> getLinks() {
        return links;
    }

    public void setLinks(List<Profile> links) {
        this.links = links;
    }
}

package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyLinks implements Serializable{

    private List<MyLink> matches;

    public MyLinks() {
        this.matches = new ArrayList<MyLink>();
    }

    public List<MyLink> getLinks() {
        return matches;
    }

    public void setLinks(List<MyLink> links) {
        this.matches = matches;
    }

}

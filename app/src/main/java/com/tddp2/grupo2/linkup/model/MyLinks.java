package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyLinks implements Serializable{

    private List<MyLink> newlinks;
    private List<MyLink> chats;

    public MyLinks() {
        this.newlinks = new ArrayList<MyLink>();
        this.chats = new ArrayList<MyLink>();
    }

    public List<MyLink> getNewlinks() {
        return newlinks;
    }

    public void setNewlinks(List<MyLink> newlinks) {
        this.newlinks = newlinks;
    }

    public List<MyLink> getChats() {
        return chats;
    }

    public void setChats(List<MyLink> chats) {
        this.chats = chats;
    }
}

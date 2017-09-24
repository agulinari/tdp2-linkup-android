package com.tddp2.grupo2.linkup.model;


import android.support.annotation.NonNull;

import java.io.Serializable;

public class MyLink implements Serializable, Comparable<MyLink> {

    private String fbid;
    private String name;
    private String lastName;
    private String age;
    private String gender;
    private String photo;
    private String time;
    private transient ChatMessage lastMessage;

    public MyLink(String fbid, String name, String lastName, String age, String gender, String photo, String time) {
        this.fbid = fbid;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.photo = photo;
        this.gender = gender;
        this.time = time;
        this.lastMessage = null;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbidUser(String fbid) {
        this.fbid = fbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(@NonNull MyLink o) {
        if (this.getLastMessage()== null || o.getLastMessage()==null){
            return 0;
        }
        if (this.getLastMessage().getMessageTime() <= o.getLastMessage().getMessageTime()){
            return 1;
        }else{
            return -1;
        }
    }
}

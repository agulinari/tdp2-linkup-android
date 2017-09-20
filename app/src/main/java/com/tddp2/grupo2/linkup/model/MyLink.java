package com.tddp2.grupo2.linkup.model;


import android.support.annotation.NonNull;

import java.io.Serializable;

public class MyLink implements Serializable, Comparable<MyLink> {

    private String fbid;
    private String name;
    private String age;
    private int photoId;
    private ChatMessage lastMessage;

    public MyLink(String fbid, String name, String age, int photoId) {
        this.fbid = fbid;
        this.name = name;
        this.age = age;
        this.photoId = photoId;
        this.lastMessage = null;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
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

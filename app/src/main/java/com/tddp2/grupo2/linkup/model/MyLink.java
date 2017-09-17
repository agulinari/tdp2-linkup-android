package com.tddp2.grupo2.linkup.model;


import java.io.Serializable;

public class MyLink implements Serializable {

    private String fbid;
    private String name;
    private String age;
    private int photoId;

    public MyLink(String fbid, String name, String age, int photoId) {
        this.fbid = fbid;
        this.name = name;
        this.age = age;
        this.photoId = photoId;
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
}

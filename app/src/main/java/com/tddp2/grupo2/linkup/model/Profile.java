package com.tddp2.grupo2.linkup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Profile implements Serializable {

    private String fbid;
    private String token;
    private String firstName;
    private String lastName;
    private String birthday;
    private String gender;
    private String occupation;
    private String education;
    private String comments;
    private List<Interest> interests = new ArrayList<Interest>();
    private List<ImageWrapper> images = new ArrayList<ImageWrapper>();
    private ImageWrapper avatar;
    private Settings settings = new Settings();
    private Location location = new Location();

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<ImageWrapper> getImages() {
        return images;
    }

    public void setImages(List<ImageWrapper> images) {
        this.images = images;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ImageWrapper getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageWrapper avatar) {
        this.avatar = avatar;
    }
}

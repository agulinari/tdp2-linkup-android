package com.tddp2.grupo2.linkup.task;

public class LoadUserTaskResponse extends TaskResponse {
    public boolean isNewUser;
    public boolean hasBirthday;
    public boolean isAdult;
    public boolean hasProfilePicture;

    public LoadUserTaskResponse() {
        this.isNewUser = false;
        this.hasBirthday = false;
        this.isAdult = false;
        this.hasProfilePicture = false;
    }
}

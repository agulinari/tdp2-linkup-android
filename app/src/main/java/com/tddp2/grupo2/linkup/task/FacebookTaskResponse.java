package com.tddp2.grupo2.linkup.task;

public class FacebookTaskResponse extends TaskResponse {
    private String birthday;
    private boolean hasProfilePicture;

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setHasProfilePicture(boolean hasProfilePicture) {
        this.hasProfilePicture = hasProfilePicture;
    }

    public boolean hasProfilePicture() {
        return this.hasProfilePicture;
    }
}

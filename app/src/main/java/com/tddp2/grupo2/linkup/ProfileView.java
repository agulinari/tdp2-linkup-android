package com.tddp2.grupo2.linkup;

import android.graphics.Bitmap;

public interface ProfileView extends BaseView {

    void updateFirstNameAndAge(String firstName, String age);
    void updateOccupation(String occupation);
    void hideOccupation();
    void updateEducation(String education);
    void hideEducation();
    void updateComment(String comment);
    void updateProfilePicture(Bitmap picture);
}


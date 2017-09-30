package com.tddp2.grupo2.linkup.activity.view;

import android.graphics.Bitmap;

import com.tddp2.grupo2.linkup.activity.view.BaseView;

public interface ProfileView extends BaseView {

    void updateFirstNameAndAge(String firstName, String age);
    void updateOccupation(String occupation);
    void hideOccupation();
    void updateEducation(String education);
    void hideEducation();
    void updateComment(String comment);
    void updateProfilePicture(Bitmap picture);
    void updateLocationView(String locationName);
}


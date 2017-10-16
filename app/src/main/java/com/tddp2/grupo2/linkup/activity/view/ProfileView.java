package com.tddp2.grupo2.linkup.activity.view;

import android.graphics.Bitmap;

import java.util.List;

public interface ProfileView extends BaseView {

    void updateFirstNameAndAge(String firstName, String age);
    void updateOccupation(String occupation);
    void hideOccupation();
    void updateEducation(String education);
    void hideEducation();
    void updateComment(String comment);
    void updateUserPictures(List<Bitmap> pictures);
    void updateLocationView(String locationName);
}


package com.tddp2.grupo2.linkup.activity.view;

import android.os.Bundle;

import java.util.List;

public interface ProfileView extends BaseView {

    void updateFirstNameAndAge(String firstName, String age);
    void updateOccupation(String occupation);
    void hideOccupation();
    void updateEducation(String education);
    void hideEducation();
    void updateComment(String comment);
    void loadUserPictures();
    void updateLocationView(String locationName);
    void showImage(List<Bundle> bundles);
    void showLoadingImage();
    void hideLoadingImage();
}


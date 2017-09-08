package com.tddp2.grupo2.linkup;

public interface LoginView extends BaseView {
    void goProfileScreen();
    void showMissingAgeAndEnd();
    void showAgeRestrictionAndEnd();
    void showProfilePictureRestrictionAndEnd();
}


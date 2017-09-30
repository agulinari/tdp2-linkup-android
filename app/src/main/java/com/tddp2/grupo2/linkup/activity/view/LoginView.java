package com.tddp2.grupo2.linkup.activity.view;

import com.tddp2.grupo2.linkup.activity.view.BaseView;

public interface LoginView extends BaseView {
    void goProfileScreen();
    void goLinksScreen();
    void showMissingAgeAndEnd();
    void showAgeRestrictionAndEnd();
    void showProfilePictureRestrictionAndEnd();
}


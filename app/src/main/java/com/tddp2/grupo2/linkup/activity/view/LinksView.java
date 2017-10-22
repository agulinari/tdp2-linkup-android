package com.tddp2.grupo2.linkup.activity.view;

import android.graphics.Bitmap;

import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;


public interface LinksView  extends BaseView {

    void showLink(Profile profile, int currentLinkIndex);

    void showEmptyLinks();

    void disableActions();

    void enableActions();

    void showMatch(String matchName);

    void showImage(Bitmap bitmap);

    void hideLoadingImage();

    void showLoadingImage();

    void registerNextAndPreviousListeners();

    void blockCandidatesNavigation();

    void updateDistance(Location loggedUserLocation, Location linkLocation);

    void showInactiveAccountAlert();

    void showAdvertisement(String advertName, String advertUrl);
}

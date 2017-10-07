package com.tddp2.grupo2.linkup.activity.view;

import android.graphics.Bitmap;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;

public interface LinkProfileView extends BaseView {
    void showData(Profile profile);
    void showImage(Bitmap photo);
    void showLoadingImage();
    void hideLoadingImage();
    void updateDistance(Location loggedUserLocation, Location linkLocation);
    void showReportAbuseProgress();
    void showBlockUserProgress();
    void onReportAbuseSuccess();
    void onReportAbuseFailure();
    void onBlockUserSuccess();
    void onBlockUserFailure();
}

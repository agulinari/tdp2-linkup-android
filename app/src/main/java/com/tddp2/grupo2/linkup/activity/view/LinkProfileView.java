package com.tddp2.grupo2.linkup.activity.view;

import android.graphics.Bitmap;

import com.tddp2.grupo2.linkup.activity.view.BaseView;
import com.tddp2.grupo2.linkup.model.Profile;

public interface LinkProfileView extends BaseView {
    void showData(Profile profile);
    void showImage(Bitmap photo);
    void showLoadingImage();
    void hideLoadingImage();
}

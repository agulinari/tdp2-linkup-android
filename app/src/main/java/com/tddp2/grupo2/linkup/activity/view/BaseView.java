package com.tddp2.grupo2.linkup.activity.view;

import android.content.Context;


public interface BaseView {

    void showProgress();

    void hideProgress();

    void goToNext();

    Context getContext();

    void onError(String errorMsg);

}


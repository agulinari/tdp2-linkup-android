package com.tddp2.grupo2.linkup;

import android.content.Context;

import com.tddp2.grupo2.linkup.model.Profile;


public interface BaseView {

    void showProgress();

    void hideProgress();

    void goToNext();

    void sessionExpired();

    Context getContext();

    void onError(String errorMsg);

}


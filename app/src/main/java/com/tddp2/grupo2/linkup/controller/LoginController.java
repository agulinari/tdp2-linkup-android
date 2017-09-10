package com.tddp2.grupo2.linkup.controller;

import android.util.Log;

import com.tddp2.grupo2.linkup.LoginView;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadUserTask;
import com.tddp2.grupo2.linkup.task.LoadUserTaskResponse;

public class LoginController {
    private LoginService loginService;
    private LoginView view;

    public LoginController(LoginView view) {
        this.loginService = ServiceFactory.getLoginService();
        this.view = view;
    }

    public void loadUser() {
        LoadUserTask task = new LoadUserTask(loginService, this);
        task.execute();
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        LoadUserTaskResponse response = (LoadUserTaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
            if (response.isNewUser) {
                validateRequiredData(response);
            } else {
                view.goLinksScreen();
            }
        }
    }

    private void validateRequiredData(LoadUserTaskResponse response) {
        if (response.hasBirthday) {
            if (response.isAdult) {
                if (response.hasProfilePicture) {
                    view.goProfileScreen();
                } else {
                    view.showProfilePictureRestrictionAndEnd();
                }
            } else {
                view.showAgeRestrictionAndEnd();
            }
        } else {
            view.showMissingAgeAndEnd();
        }
    }
}

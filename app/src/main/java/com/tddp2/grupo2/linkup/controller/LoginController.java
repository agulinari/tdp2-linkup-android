package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.LoginView;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.FacebookTaskResponse;
import com.tddp2.grupo2.linkup.task.GetDataFromFacebookTask;

public class LoginController {
    private LoginService loginService;
    private LoginView view;

    public LoginController(LoginView view) {
        this.loginService = ServiceFactory.getLoginService();
        this.view = view;
    }

    public void loadProfile() {
        GetDataFromFacebookTask task = new GetDataFromFacebookTask(loginService, this);
        task.execute();
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        FacebookTaskResponse response = (FacebookTaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
           view.goProfileScreen();
        }
    }
}

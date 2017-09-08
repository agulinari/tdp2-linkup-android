package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LoginController;
import com.tddp2.grupo2.linkup.service.api.LoginService;

public class GetDataFromFacebookTask extends AsyncTask<Object, Void, TaskResponse> {

    private LoginService loginService;
    private LoginController controller;

    public GetDataFromFacebookTask(LoginService loginService, LoginController controller) {
        this.loginService = loginService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        FacebookTaskResponse facebookData = new FacebookTaskResponse();
        loginService.loadDataFromFacebook(facebookData);
        return facebookData;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishTask();

        controller.onResult(response);
    }
}

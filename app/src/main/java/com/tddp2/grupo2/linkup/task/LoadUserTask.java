package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LoginController;
import com.tddp2.grupo2.linkup.service.api.LoginService;

public class LoadUserTask extends AsyncTask<Object, Void, TaskResponse> {

    private LoginService loginService;
    private LoginController controller;

    public LoadUserTask(LoginService loginService, LoginController controller) {
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
        LoadUserTaskResponse facebookData = new LoadUserTaskResponse();
        loginService.loadUserData(facebookData);
        return facebookData;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishTask();

        controller.onResult(response);
    }
}

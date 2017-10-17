package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.LinkProfileController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinkUserService;

public class LoadLinkUserTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinkUserService linkUserService;
    private LinkProfileController controller;

    public LoadLinkUserTask(LinkUserService linkUserService, LinkProfileController controller) {
        this.linkUserService = linkUserService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null) {
            controller.initTask();
        }
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        String fbid = (String) params[0];
        TaskResponse response = new TaskResponse();
        try {
            Profile profile = linkUserService.loadUser(fbid);
            response.setResponse(profile);
        } catch (ServiceException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishTask();
            controller.onResult(response);
        }
    }
}

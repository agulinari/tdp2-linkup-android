package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;

/**
 * Created by agustin on 17/09/2017.
 */

public class RejectLinkTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinksController controller;

    public RejectLinkTask(LinksService linksService, LinksController controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initRejectTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse taskResponse = new TaskResponse();
        Links links;
        String fbidCandidate = (String) params[0];

        try {
            links = linksService.rejectLink(fbidCandidate);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(e.getMessage());
            response.setSessionExpired(e.isSessionExpired());
            return response;
        }
        taskResponse.setResponse(links);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishRejectTask();

        controller.onRejectResult(response);
    }

}

package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.service.api.LinksService;


public class AcceptLinkTask extends AsyncTask<Object, Void, TaskResponse>{

    private LinksService linksService;
    private LinksController controller;

    public AcceptLinkTask(LinksService linksService, LinksController controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initAcceptTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        String fbidCandidate = (String) params[0];
        String candidateName = (String) params[1];
        try {
            AcceptLinkTaskResponse acceptLinkResponse = linksService.acceptLink(fbidCandidate);
            acceptLinkResponse.setMatchName(candidateName);
            return acceptLinkResponse;
        } catch (ServiceException e) {
            AcceptLinkTaskResponse response = new AcceptLinkTaskResponse(e.getMessage());
            return response;
        }
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishAcceptTask();

        controller.onAcceptResult(response);
    }

}

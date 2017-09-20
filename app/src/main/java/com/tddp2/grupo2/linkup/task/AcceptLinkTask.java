package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.exception.UsersMatchException;
import com.tddp2.grupo2.linkup.model.Links;
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
        AcceptLinkTaskResponse taskResponse = new AcceptLinkTaskResponse();
        Links links;
        String fbidCandidate = (String) params[0];
        String candidateName = (String) params[1];

        try {
            links = linksService.acceptLink(fbidCandidate);
        } catch (UsersMatchException e) {
            AcceptLinkTaskResponse response = new AcceptLinkTaskResponse();
            response.setIsAMatch(true);
            response.setMatchName(candidateName);
            return response;
        } catch (ServiceException e) {
            AcceptLinkTaskResponse response = new AcceptLinkTaskResponse(e.getMessage());
            return response;
        }
        taskResponse.setIsAMatch(false);
        taskResponse.setLinks(links);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishAcceptTask();

        controller.onAcceptResult(response);
    }

}

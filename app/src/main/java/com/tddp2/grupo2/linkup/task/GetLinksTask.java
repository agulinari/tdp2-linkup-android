package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.InactiveAccountException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.service.api.LinksService;


public class GetLinksTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinksController controller;

    public GetLinksTask(LinksService linksService, LinksController controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initGetLinksTask();
    }

    @Override
    protected LinksTaskResponse doInBackground(Object... params) {
        LinksTaskResponse taskResponse = new LinksTaskResponse();
        Links links;
        try {
            links = linksService.getLinks();
        } catch (ServiceException e) {
            taskResponse.setError(e.getMessage());
            taskResponse.setResponse(linksService.getDatabase().getLinks());
            return taskResponse;
        } catch (InactiveAccountException e) {
            taskResponse.setError(e.getMessage());
            taskResponse.setResponse(linksService.getDatabase().getMyLinks());
            taskResponse.inactiveAccount = true;
            return taskResponse;
        }
        taskResponse.setResponse(links);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishGetLinksTask();
            LinksTaskResponse linksTaskResponse = (LinksTaskResponse) response;
            if (linksTaskResponse.inactiveAccount) {
                controller.showInactiveAccountError();
            } else {
                controller.onGetLinksResult(response);
            }
        }
    }
}
package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.MyLinksControllerInterface;
import com.tddp2.grupo2.linkup.exception.InactiveAccountException;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;


public class GetMyLinksTask extends AsyncTask<Object, Void, TaskResponse> {


    private MyLinksService myLinksService;
    private MyLinksControllerInterface controller;

    public GetMyLinksTask(MyLinksService myLinksService, MyLinksControllerInterface controller) {
        this.myLinksService = myLinksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initGetMyLinksTask();
    }

    @Override
    protected LinksTaskResponse doInBackground(Object... params) {
        LinksTaskResponse taskResponse = new LinksTaskResponse();
        MyLinks links;
        try {
            links = myLinksService.getMyLinks();
        } catch (ServiceException e) {
            taskResponse.setError(e.getMessage());
            taskResponse.setResponse(myLinksService.getDatabase().getMyLinks());
            return taskResponse;
        } catch (InactiveAccountException e) {
            taskResponse.setError(e.getMessage());
            taskResponse.setResponse(myLinksService.getDatabase().getMyLinks());
            taskResponse.inactiveAccount = true;
            return taskResponse;
        }
        taskResponse.setResponse(links);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishGetMyLinksTask();
            LinksTaskResponse linksTaskResponse = (LinksTaskResponse) response;
            if (linksTaskResponse.inactiveAccount) {
                controller.showInactiveAccountError();
            } else {
                controller.onGetMyLinksResult(response);
            }
        }
    }

}

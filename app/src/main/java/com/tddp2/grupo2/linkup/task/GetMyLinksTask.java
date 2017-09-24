package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.MyLinksController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.MyLinks;
import com.tddp2.grupo2.linkup.service.api.MyLinksService;


public class GetMyLinksTask extends AsyncTask<Object, Void, TaskResponse> {


    private MyLinksService myLinksService;
    private MyLinksController controller;

    public GetMyLinksTask(MyLinksService myLinksService, MyLinksController controller) {
        this.myLinksService = myLinksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initGetMyLinksTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse taskResponse = new TaskResponse();
        MyLinks links;
        try {
            links = myLinksService.getMyLinks();
        } catch (ServiceException e) {
            taskResponse.setError(e.getMessage());
            return taskResponse;
        }
        taskResponse.setResponse(links);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishGetMyLinksTask();

        controller.onGetMyLinksResult(response);
    }

}

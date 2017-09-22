package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.LinksController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Image;
import com.tddp2.grupo2.linkup.model.Links;
import com.tddp2.grupo2.linkup.service.api.LinksService;



public class LoadImageTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinksController controller;

    public LoadImageTask(LinksService linksService, LinksController controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initLoadImageTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse taskResponse = new TaskResponse();
        Image image;
        String fbidCandidate = (String) params[0];

        try {
            image = linksService.loadImage(fbidCandidate);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(e.getMessage());
            return response;
        }
        taskResponse.setResponse(image);
        return taskResponse;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishLoadImageTask();

        controller.onLoadImageResult(response);
    }

}

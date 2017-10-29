package com.tddp2.grupo2.linkup.task;


import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.LinkImageControllerInterface;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.service.api.LinksService;

public class UpdateImageCacheTask extends AsyncTask<Object, Void, TaskResponse> {

    private LinksService linksService;
    private LinkImageControllerInterface controller;

    public UpdateImageCacheTask(LinksService linksService, LinkImageControllerInterface controller) {
        this.linksService = linksService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {

        Boolean hasChanges = false;
        String fbidCandidate = (String) params[0];

        try {
            hasChanges = linksService.updateImagesCache(fbidCandidate);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(fbidCandidate);
            return response;
        }

        TaskResponse response = new TaskResponse();
        response.setResponse(hasChanges);

        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        Boolean hasChanges = (Boolean)response.getResponse();
        if (hasChanges!= null && hasChanges) {
            controller.reloadImages();
        }
    }

}

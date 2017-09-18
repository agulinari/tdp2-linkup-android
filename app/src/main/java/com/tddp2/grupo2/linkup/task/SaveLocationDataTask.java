package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.LocationController;
import com.tddp2.grupo2.linkup.service.api.ProfileService;

public class SaveLocationDataTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private LocationController controller;

    public SaveLocationDataTask(ProfileService profileService, LocationController controller) {
        this.profileService = profileService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        com.tddp2.grupo2.linkup.model.Location location = (com.tddp2.grupo2.linkup.model.Location) params[0];
        profileService.saveLocation(location);
        return new TaskResponse();
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        controller.onOperationFinished();
    }
}

package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;


public class UpdateProfileTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private ProfileController controller;

    public UpdateProfileTask(ProfileService profileService, ProfileController controller) {
        this.profileService = profileService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null)
            controller.initTask();
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        String comments = (String) params[0];
        try {

            Profile localProfile = profileService.getLocalProfile();
            localProfile.setComments(comments);
            profileService.updateProfile(localProfile);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(e.getMessage());
            return response;
        }

        return new TaskResponse();
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishTask();

        controller.onResult(response);
    }
}

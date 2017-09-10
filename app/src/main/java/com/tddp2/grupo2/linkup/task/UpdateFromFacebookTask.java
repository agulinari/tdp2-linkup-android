package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;

public class UpdateFromFacebookTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private ProfileController controller;

    public UpdateFromFacebookTask(ProfileService profileService, ProfileController controller) {
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
        try {
            Profile localProfile = profileService.getLocalProfile();
            profileService.updateFromFacebook(localProfile);
                return new TaskResponse();
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(e.getMessage());
            response.setSessionExpired(e.isSessionExpired());
            return response;
        }
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishTask();

        controller.onUpdateDataResult(response);
    }
}

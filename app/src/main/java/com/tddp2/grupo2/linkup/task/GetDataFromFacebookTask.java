package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import android.util.Log;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;

public class GetDataFromFacebookTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private ProfileController controller;

    public GetDataFromFacebookTask(ProfileService profileService, ProfileController controller) {
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
        Profile localProfile = new Profile();
        profileService.loadDataFromFacebook(localProfile);
        FacebookTaskResponse response = new FacebookTaskResponse();
        response.setProfileResponse(localProfile);
        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null)
            controller.finishTask();

        controller.onResult(response);
    }
}

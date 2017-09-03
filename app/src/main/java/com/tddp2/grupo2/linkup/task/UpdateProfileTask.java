package com.tddp2.grupo2.linkup.task;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Interest;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.controller.ProfileController;

import java.util.List;


public class UpdateProfileTask  extends AsyncTask<Object, Void, TaskResponse> {

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
        String fibd = (String) params[0];
        String name = (String) params[1];
        List<Interest> interests = (List<Interest>) params[2];
        Settings settings = (Settings) params[3];

        try {

            //Profile localProfile = profileService.getLocalProfile();
            Profile localProfile = new Profile();
            localProfile.setFbid(fibd);
            localProfile.setFirstName(name);
            profileService.updateProfile(localProfile);
        } catch (ServiceException e) {
            TaskResponse response = new TaskResponse(e.getMessage());
            response.setSessionExpired(e.isSessionExpired());
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

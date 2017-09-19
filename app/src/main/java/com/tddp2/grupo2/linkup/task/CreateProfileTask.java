package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;

import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;


public class CreateProfileTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private SettingsController controller;

    public CreateProfileTask(ProfileService profileService, SettingsController controller) {
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
        Settings settings = (Settings) params[0];
        try {
            Profile localProfile = profileService.getLocalProfile();
            //FIXME: aca conviene usar otro objeto Profile y no pisar el actual, por si no se logra guardar
            localProfile.setSettings(settings);
            profileService.createProfile(localProfile);
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

package com.tddp2.grupo2.linkup.task;

import android.os.AsyncTask;
import com.tddp2.grupo2.linkup.controller.PremiumPayFormController;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.service.api.ProfileService;


public class UpgradeAccountTypeTask extends AsyncTask<Object, Void, TaskResponse> {

    private ProfileService profileService;
    private PremiumPayFormController controller;

    public UpgradeAccountTypeTask(ProfileService profileService, PremiumPayFormController controller) {
        this.profileService = profileService;
        this.controller = controller;
    }

    @Override
    protected void onPreExecute() {
        if (controller != null) {
            controller.initTask();
        }
    }

    @Override
    protected TaskResponse doInBackground(Object... params) {
        TaskResponse response = new TaskResponse();
        try {
            profileService.upgradeAccountType();
        } catch (ServiceException e) {
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(TaskResponse response) {
        if (controller != null) {
            controller.finishTask();
            controller.onResult (response);
        }
    }
}

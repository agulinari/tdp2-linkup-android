package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.CreateProfileTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateSettingsTask;


public class SettingsController {

    private ProfileService profileService;
    private BaseView view;

    public SettingsController(BaseView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void createProfile(Settings settings) {

        CreateProfileTask task = new CreateProfileTask(profileService, this);
        task.execute(settings);

    }

    public void saveProfile(Settings settings) {

      UpdateSettingsTask task = new UpdateSettingsTask(profileService, this);
        task.execute(settings);

    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            view.goToNext();
        }
    }

}

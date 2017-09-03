package com.tddp2.grupo2.linkup.controller;

import android.view.View;

import com.tddp2.grupo2.linkup.BaseView;
import com.tddp2.grupo2.linkup.model.Interest;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateProfileTask;

import java.util.ArrayList;


public class SettingsController {

    private ProfileService profileService;
    private BaseView view;

    public SettingsController(BaseView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void saveProfile() {

      UpdateProfileTask task = new UpdateProfileTask(profileService, this);

        task.execute("1", "Juan", new ArrayList<Interest>(), new Settings());

    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
            view.goToNext();
        }
    }

}

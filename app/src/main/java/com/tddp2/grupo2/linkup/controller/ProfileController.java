package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.ProfileView;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.FacebookTaskResponse;
import com.tddp2.grupo2.linkup.task.GetDataFromFacebookTask;
public class ProfileController {

    private ProfileService profileService;
    private ProfileView view;

    public ProfileController(ProfileView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void updateProfile() {
        GetDataFromFacebookTask task = new GetDataFromFacebookTask(profileService, this);
        task.execute();
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
        FacebookTaskResponse response = (FacebookTaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
            Profile profile = response.getProfile();
            view.updateFirstName(profile.getFirstName());
        }
    }
}

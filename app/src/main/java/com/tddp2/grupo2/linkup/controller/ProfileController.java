package com.tddp2.grupo2.linkup.controller;

import android.graphics.Bitmap;
import com.tddp2.grupo2.linkup.ProfileView;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.model.Settings;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateProfileTask;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.task.UpdateSettingsTask;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

public class ProfileController {

    private ProfileService profileService;
    private ProfileView view;

    public ProfileController(ProfileView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void saveProfile(String comments) {

        UpdateProfileTask task = new UpdateProfileTask(profileService, this);
        task.execute(comments);

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

    public void update() {
        Profile profile = this.profileService.getLocalProfile();

        String age = "";
        try {
            age = String.valueOf(DateUtils.getAgeFromBirthday(profile.getBirthday()));
        } catch (MissingAgeException e) {
            e.printStackTrace();
        }

        if (!profile.getOccupation().equals("")) {
            view.updateOccupation(profile.getOccupation());
        }

        if (!profile.getEducation().equals("")) {
            view.updateEducation(profile.getEducation());
        }

        view.updateFirstNameAndAge(profile.getFirstName(), age);
        view.updateComment(profile.getComments());

        String image = profile.getImages().get(0).getImage();
        Bitmap bitmap = ImageUtils.base64ToBitmap(image);
        view.updateProfilePicture(bitmap);
    }

    public void saveNewComment(String newComment) {
        view.updateComment(newComment);
    }

    public ProfileService getProfileService() {
        return profileService;
    }

}

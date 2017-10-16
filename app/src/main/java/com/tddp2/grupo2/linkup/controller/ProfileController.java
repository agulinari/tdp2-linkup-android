package com.tddp2.grupo2.linkup.controller;

import android.graphics.Bitmap;
import com.tddp2.grupo2.linkup.activity.view.ProfileView;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.model.ImageWrapper;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateFromFacebookTask;
import com.tddp2.grupo2.linkup.task.UpdateProfileTask;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

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
        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            view.goToNext();
        }
    }

    public void onUpdateDataResult(Object result) {
        TaskResponse response = (TaskResponse) result;
        if (response.hasError()) {
            view.onError(response.getError());
        } else {
            update();
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

        if (profile.getOccupation().equals("")) {
            view.hideOccupation();
        } else {
            view.updateOccupation(profile.getOccupation());
        }

        if (profile.getEducation().equals("")) {
            view.hideEducation();
        } else {
            view.updateEducation(profile.getEducation());
        }

        if (!profile.getLocation().getName().equals("")) {
            view.updateLocationView(profile.getLocation().getName());
        }

        view.updateFirstNameAndAge(profile.getFirstName(), age);
        view.updateComment(profile.getComments());

        List<Bitmap> pictures = new ArrayList<>();
        for (ImageWrapper image : profile.getImages()) {
            String base64Image = image.getImage().getData();
            if (base64Image != null) {
                Bitmap bitmap = ImageUtils.base64ToBitmap(base64Image);
                pictures.add(bitmap);
            }
        }
        view.updateUserPictures(pictures);
    }

    public void saveNewComment(String newComment) {
        view.updateComment(newComment);
    }

    public ProfileService getProfileService() {
        return profileService;
    }

    public void reloadDataFromFacebook() {
        UpdateFromFacebookTask task = new UpdateFromFacebookTask(profileService, this);
        task.execute();
    }
}

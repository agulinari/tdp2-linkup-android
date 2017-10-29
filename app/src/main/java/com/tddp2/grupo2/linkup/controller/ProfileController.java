package com.tddp2.grupo2.linkup.controller;

import android.os.Bundle;

import com.tddp2.grupo2.linkup.activity.view.ProfileView;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LinksService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.LoadImagesTask;
import com.tddp2.grupo2.linkup.task.TaskResponse;
import com.tddp2.grupo2.linkup.task.UpdateFromFacebookTask;
import com.tddp2.grupo2.linkup.task.UpdateImageCacheTask;
import com.tddp2.grupo2.linkup.task.UpdateProfileTask;
import com.tddp2.grupo2.linkup.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ProfileController implements LinkImageControllerInterface{

    private ProfileService profileService;
    private LinksService linksService;
    private ProfileView view;

    public ProfileController(ProfileView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.linksService = ServiceFactory.getLinksService();
        this.view = view;
    }


    public void loadImages() {
        Profile profile = this.profileService.getLocalProfile();
        LoadImagesTask task = new LoadImagesTask(linksService, this);
        task.execute(profile.getFbid(), profile.getImages().size());
    }

    @Override
    public void updateImagesFromServer(String linkUserId) {
        Profile profile = this.profileService.getLocalProfile();
        UpdateImageCacheTask task = new UpdateImageCacheTask(linksService, this);
        task.execute(profile.getFbid());
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
        view.loadUserPictures();

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

    @Override
    public void initLoadImageTask() {
        view.showLoadingImage();
    }

    @Override
    public void finishLoadImageTask() {
        view.hideLoadingImage();
    }

    @Override
    public void onLoadImageResult(TaskResponse response) {
        Profile profile = this.profileService.getLocalProfile();
        if (response.hasError()) {
            view.showImage(new ArrayList<Bundle>());
        } else {
            List<Bundle> bundles = (List<Bundle>)response.getResponse();
            view.showImage(bundles);
        }
    }

    @Override
    public void reloadImages() {
        view.loadUserPictures();
    }


}

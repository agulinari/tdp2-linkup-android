package com.tddp2.grupo2.linkup.controller;

import android.util.Log;

import com.tddp2.grupo2.linkup.ProfileView;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ProfileController {

    private ProfileService profileService;
    private ProfileView view;

    public ProfileController(ProfileView view) {
        this.profileService = ServiceFactory.getProfileService();
        this.view = view;
    }

    public void initTask() {
        view.showProgress();
    }

    public void finishTask() {
        view.hideProgress();
    }

    public void onResult(Object result) {
       /* LoadUserTaskResponse response = (LoadUserTaskResponse) result;
        if (response.sessionExpired()) {
            view.sessionExpired();
        } else if (response.hasError()) {
            view.onError(response.getError());
        } else {
            //Profile profile = response.getProfile();
            //view.updateFirstName(profile.getFirstName());
        }*/
    }

    public void update() {
        Profile profile = this.profileService.getLocalProfile();

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
        LocalDate birthdayDate = formatter.parseLocalDate(profile.getBirthday());
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthdayDate, now);

        if (!profile.getOccupation().equals("")) {
            view.updateOccupation(profile.getOccupation());
        }

        if (!profile.getEducation().equals("")) {
            view.updateEducation(profile.getEducation());
        }

        view.updateFirstNameAndAge(profile.getFirstName(), age.getYears());
    }
}

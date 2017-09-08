package com.tddp2.grupo2.linkup.controller;

import com.tddp2.grupo2.linkup.LoginView;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.FacebookTaskResponse;
import com.tddp2.grupo2.linkup.task.GetDataFromFacebookTask;

import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LoginController {
    private LoginService loginService;
    private LoginView view;

    public LoginController(LoginView view) {
        this.loginService = ServiceFactory.getLoginService();
        this.view = view;
    }

    public void loadProfile() {
        GetDataFromFacebookTask task = new GetDataFromFacebookTask(loginService, this);
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
            try {
                int age = getAgeFromBirthday(profile.getBirthday());
                if (age >= 18) {
                    view.goProfileScreen();
                } else {
                    view.showAgeRestrictionAndEnd();
                }
            } catch (MissingAgeException e) {
                view.showMissingAgeAndEnd();
            }
        }
    }

    private int getAgeFromBirthday(String birthday) throws MissingAgeException {
        if (birthday.equals("")) {
            throw new MissingAgeException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            LocalDate birthdayDate = formatter.parseLocalDate(birthday);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthdayDate, now);
            return age.getYears();
        } catch (IllegalArgumentException e) {
            throw new MissingAgeException();
         }
    }
}

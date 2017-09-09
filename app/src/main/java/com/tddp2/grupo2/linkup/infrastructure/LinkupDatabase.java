package com.tddp2.grupo2.linkup.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.tddp2.grupo2.linkup.LinkupApplication;
import com.tddp2.grupo2.linkup.model.Profile;

/**
 * Created by agustin on 07/09/2017.
 */

public class LinkupDatabase implements Database
{

    private static final String DB = "DB";
    private static final String PROFILE = "PROFILE";


    private Profile profile;


    public LinkupDatabase() {
        profile = get(PROFILE, Profile.class);
    }

    @Override
    public Profile getProfile() {
        return profile;
    }

    @Override
    public void setProfile(Profile profile) {
        this.profile = profile;
        save(PROFILE, profile);
    }

    public void save(String key, Object value) {
        SharedPreferences sharedPref = LinkupApplication.getContext().getSharedPreferences(DB, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        prefsEditor.putString(key, json);
        prefsEditor.commit();
    }

    public <T extends Object> T get(String key, Class<T> classValue) {
        SharedPreferences sharedPref = LinkupApplication.getContext().getSharedPreferences(DB, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString(key, "");
        return gson.fromJson(json, classValue);
    }
}

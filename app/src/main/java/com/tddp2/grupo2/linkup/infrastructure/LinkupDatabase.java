package com.tddp2.grupo2.linkup.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.tddp2.grupo2.linkup.LinkupApplication;
import com.tddp2.grupo2.linkup.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LinkupDatabase implements Database
{

    private static final String DB = "DB";
    private static final String PROFILE = "PROFILE";
    private static final String LINKS = "LINKS";
    private static final String MY_LINKS = "MY_LINKS";
    private static final String TOKEN = "TOKEN";

    private Profile profile;
    private Links links;
    private MyLinks myLinks;
    private String token;

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

    @Override
    public Links getLinks() {
        if (links == null){
            links = getLinksFromDatabase();
        }
        return links;
    }

    @Override
    public void setLinks(Links links) {
        this.links = links;
        save(LINKS, links);
    }

    @Override
    public MyLinks getMyLinks() {
        if (myLinks == null){
            myLinks = get(MY_LINKS, MyLinks.class);
        }
        return myLinks;
    }

    @Override
    public void setMyLinks(MyLinks myLinks) {
        this.myLinks = myLinks;
        save(MY_LINKS, myLinks);
    }

    @Override
    public String getToken() {
        if (token == null){
            token = get(TOKEN, String.class);
        }
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
        save(TOKEN, token);
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

    private Links getLinksFromDatabase() {
        SharedPreferences sharedPref = LinkupApplication.getContext().getSharedPreferences(DB, Context.MODE_PRIVATE);
        String json = sharedPref.getString(LINKS, "");
        List<Link> links = new ArrayList<>();
        try {
            JSONObject linksJson = new JSONObject(json);
            JSONArray array = linksJson.getJSONArray("links");
            Gson gson = new Gson();
            for(int i = 0 ; i < array.length(); i++){
                JSONObject jsonObject  = array.getJSONObject(i);
                if (jsonObject.has("advertiser")) {
                    Advertisement advertisement = gson.fromJson(jsonObject.toString(), Advertisement.class);
                    links.add(advertisement);
                } else {
                    Profile profile = gson.fromJson(jsonObject.toString(), Profile.class);
                    links.add(profile);
                }
            }
        } catch (JSONException e) {
            Log.i("LINKSBASE", e.getMessage());
        }
        Links linksObject = new Links();
        linksObject.setLinks(links);
        return linksObject;
    }
}

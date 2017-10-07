package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.tddp2.grupo2.linkup.controller.MyLinkProfileController;
import com.tddp2.grupo2.linkup.model.Profile;

public class MyLinkProfileActivity extends AbstractLinkProfileActivity {
    private MyLinkProfileController controller;
    private GoogleMap locationMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "MyLinkProfileActivity";

        Bundle b = getIntent().getExtras();
        String linkUserId = (b != null) ? b.getString("LINK_USER_ID") : "";

        controller = new MyLinkProfileController(this);
        controller.loadUser(linkUserId);
    }

    @Override
    public void showData(Profile profile) {
        super.showData(profile);
        loadMap();
    }

    @Override
    public void getCoordinatesAndUpdateDistance() {
        controller.getCoordinatesAndUpdateDistance();
    }
}

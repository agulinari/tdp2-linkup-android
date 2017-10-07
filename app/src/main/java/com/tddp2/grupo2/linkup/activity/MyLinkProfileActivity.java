package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.tddp2.grupo2.linkup.controller.MyLinkProfileController;
import com.tddp2.grupo2.linkup.model.Location;

public class MyLinkProfileActivity extends AbstractLinkProfileActivity {
    private MyLinkProfileController controller;

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
    public void onMapReady(GoogleMap map) {}

    public void updateDistance(Location loggedUserLocation, Location linkLocation) {}
}

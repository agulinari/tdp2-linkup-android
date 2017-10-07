package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import com.tddp2.grupo2.linkup.controller.LinkProfileController;

public class LinkProfileActivity extends AbstractLinkProfileActivity {
    private LinkProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "LinkProfileActivity";

        Bundle b = getIntent().getExtras();
        int currentLink = (b != null) ? b.getInt("currentLinkIndex") : -1;

        controller = new LinkProfileController(this);
        controller.showLinkData(currentLink);

        loadMap();
    }

    @Override
    public void getCoordinatesAndUpdateDistance() {
        controller.getCoordinatesAndUpdateDistance();
    }
}

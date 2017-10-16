package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import com.tddp2.grupo2.linkup.controller.MyLinkProfileController;
import com.tddp2.grupo2.linkup.model.Profile;

public class MyLinkProfileActivity extends AbstractLinkProfileActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = "MyLinkProfileActivity";

        Bundle b = getIntent().getExtras();
        String linkUserId = (b != null) ? b.getString("LINK_USER_ID") : "";

        controller = new MyLinkProfileController(this);
        controller.showLinkData(linkUserId);
        controller.loadMyLinks();
    }

    @Override
    public void showData(Profile profile) {
        super.showData(profile);
        loadMap();
    }
}

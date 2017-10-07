package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.controller.MyLinkProfileController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;

public class MyLinkProfileActivity extends AbstractLinkProfileActivity {
    private MyLinkProfileController controller;

    @BindView(R.id.linkProfileNameText)
    TextView textViewLinkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        String linkUserId = (b != null) ? b.getString("LINK_USER_ID") : "";

        controller = new MyLinkProfileController(this);
        controller.loadUser(linkUserId);
    }

    public void showProgress() {}

    public void hideProgress() {}

    public void goToNext() {}

    public Context getContext() {
        return this.getContext();
    }

    public void onError(String errorMsg) {}

    @Override
    public void showData(Profile profile) {
        textViewLinkName.setText(profile.getFirstName());
    }

    @Override
    public void showImage(Bitmap photo) {}

    @Override
    public void showLoadingImage() {}

    @Override
    public void hideLoadingImage() {}

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {}

    @Override
    public void onMapReady(GoogleMap map) {}

    public void updateDistance(Location loggedUserLocation, Location linkLocation) {}
}

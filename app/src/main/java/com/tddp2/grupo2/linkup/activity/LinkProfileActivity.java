package com.tddp2.grupo2.linkup.activity;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tddp2.grupo2.linkup.controller.LinkProfileController;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.utils.MapUtils;

public class LinkProfileActivity extends AbstractLinkProfileActivity {
    private LinkProfileController controller;
    private GoogleMap locationMap;

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
    public void onMapReady(GoogleMap map) {
        this.locationMap = map;
        UiSettings mapSettings = this.locationMap.getUiSettings();
        mapSettings.setAllGesturesEnabled(false);
        controller.getCoordinatesAndUpdateDistance();
    }

    public void updateDistance(Location loggedUserLocation, Location linkLocation) {
        float distanceInMeters = MapUtils.getDistanceBetweenLocationsInMeters(loggedUserLocation, linkLocation);

        String distanceText = MapUtils.getDistanceTextFromMeters(distanceInMeters, this);
        textViewLinkDistance.setText(distanceText);

        LatLng centerPoint = MapUtils.getCenterPoint(loggedUserLocation, linkLocation);
        double radiusInMeters = distanceInMeters / 2;
        LatLngBounds centerPointBounds = MapUtils.getLocationBounds(centerPoint, radiusInMeters);
        this.locationMap.moveCamera(CameraUpdateFactory.newLatLngBounds(centerPointBounds, MapUtils.MAP_PADDING));

        CircleOptions circleOptions = MapUtils.getCircleOptions(centerPoint, radiusInMeters);
        this.locationMap.addCircle(circleOptions);
    }
}

package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.controller.LinkProfileController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinkProfileActivity extends BroadcastActivity implements LinkProfileView, OnMapReadyCallback {

    private static final String TAG = "LinkProfileActivity";
    private LinkProfileController controller;
    private GoogleMap locationMap;

    @BindView(R.id.linkProfileCoordinatorLayout)
    CoordinatorLayout coordView;

    @BindView(R.id.linkCommentCard)
    CardView cardViewCommentCard;

    @BindView(R.id.linkCommentText)
    TextView textViewLinkComment;

    @BindView(R.id.linkOccupationCard)
    CardView cardViewOccupationCard;

    @BindView(R.id.linkWorkText)
    TextView textViewLinkWork;

    @BindView(R.id.linkEducationCard)
    CardView cardViewEducationCard;

    @BindView(R.id.linkStudiesText)
    TextView textViewLinkStudies;

    @BindView(R.id.linkProfilePhoto)
    ImageView imageViewLinkProfilePhoto;

    @BindView(R.id.linkProfileImageProgress)
    ProgressBar progressBarImage;

    @BindView(R.id.linkLocationText)
    TextView textViewLinkDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        int currentLink = (b != null) ? b.getInt("currentLinkIndex") : -1;

        controller = new LinkProfileController(this);
        controller.showLinkData(currentLink);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.linkMap);
        mapFragment.getMapAsync(this);
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
        String comment = profile.getComments();
        if (!comment.equals("")) {
            cardViewCommentCard.setVisibility(View.VISIBLE);
            textViewLinkComment.setText(comment);
        } else {
            cardViewCommentCard.setVisibility(View.GONE);
        }

        String occupation = profile.getOccupation();
        if (!occupation.equals("")) {
            cardViewOccupationCard.setVisibility(View.VISIBLE);
            textViewLinkWork.setText(occupation);
        } else {
            cardViewOccupationCard.setVisibility(View.GONE);
        }

        String studies = profile.getEducation();
        if (!studies.equals("")) {
            cardViewEducationCard.setVisibility(View.VISIBLE);
            textViewLinkStudies.setText(studies);
        } else {
            cardViewEducationCard.setVisibility(View.GONE);
        }
    }

    @Override
    public void showImage(Bitmap photo) {
        imageViewLinkProfilePhoto.setImageBitmap(photo);
    }

    @Override
    public void showLoadingImage() {
        progressBarImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingImage() {
        progressBarImage.setVisibility(View.GONE);
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");
        if (notification!=null) {
            Snackbar snackbar = Snackbar.make(coordView, notification.messageBody, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.locationMap = map;
        UiSettings mapSettings = this.locationMap.getUiSettings();
        mapSettings.setAllGesturesEnabled(false);
        controller.getCoordinatesAndUpdateDistance();
    }

    public void updateDistance(Location loggedUserLocation, Location linkLocation) {
        android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(loggedUserLocation.getLatitude());
        loc1.setLongitude(loggedUserLocation.getLongitude());

        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(linkLocation.getLatitude());
        loc2.setLongitude(linkLocation.getLongitude());

        float distanceInMeters = loc1.distanceTo(loc2);
        Log.i("DISTANCE", String.valueOf(distanceInMeters) + " meters");

        if (distanceInMeters <= 100) {
            textViewLinkDistance.setText(getString(R.string.link_distance_close));
        } else {
            float distanceInKilometers = distanceInMeters / 1000;
            Log.i("DISTANCE", String.valueOf(distanceInKilometers) + " kilometers");
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(1);
            if (((distanceInKilometers >= 1) && (distanceInKilometers < 2))) {
                textViewLinkDistance.setText(getString(R.string.link_distance_singular, decimalFormat.format(distanceInKilometers)));
            } else {
                textViewLinkDistance.setText(getString(R.string.link_distance_plural, decimalFormat.format(distanceInKilometers)));
            }
        }

        double latitude1 = loggedUserLocation.getLatitude();
        double latitude2 = linkLocation.getLatitude();
        double longitude1 = loggedUserLocation.getLongitude();
        double longitude2 = linkLocation.getLongitude();

        double southLatitude = (latitude1 < latitude2) ? latitude1 : latitude2;
        double northLatitude = (latitude1 < latitude2) ? latitude2 : latitude1;
        double westLongitude = (longitude1 < longitude2) ? longitude1 : longitude2;
        double eastLongitude = (longitude1 < longitude2) ? longitude2 : longitude1;

        LatLng southwest = new LatLng(southLatitude, westLongitude);
        LatLng northeast = new LatLng(northLatitude, eastLongitude);
        LatLngBounds centerPoint = new LatLngBounds(southwest, northeast);

        int padding = 0;
        this.locationMap.moveCamera(CameraUpdateFactory.newLatLngBounds(centerPoint, padding));
    }
}

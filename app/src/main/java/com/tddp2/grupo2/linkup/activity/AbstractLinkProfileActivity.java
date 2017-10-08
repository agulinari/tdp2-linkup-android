package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.controller.AbstractLinkProfileController;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Location;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.utils.DateUtils;
import com.tddp2.grupo2.linkup.utils.MapUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class AbstractLinkProfileActivity extends BroadcastActivity implements LinkProfileView, OnMapReadyCallback {
    @BindView(R.id.linkProfileCoordinatorLayout)
    CoordinatorLayout coordView;

    @BindView(R.id.linkProfileNameText)
    TextView textViewLinkName;

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

    @BindView(R.id.reportAbuseButton)
    Button buttonReportAbuse;

    @BindView(R.id.blockuserButton)
    Button buttonBlockUser;

    protected String TAG;
    private ProgressDialog progressDialog;
    protected GoogleMap locationMap;
    protected AbstractLinkProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);

        registerListeners();
    }

    private void registerListeners() {
        buttonReportAbuse.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openReportAbusePopUp();
            }
        });

        buttonBlockUser.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openBlockUserPopUp();
            }
        });
    }

    private void openReportAbusePopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.report_abuse_popup));
        CharSequence[] categories = {
                getString(R.string.report_abuse_category_1),
                getString(R.string.report_abuse_category_2),
                getString(R.string.report_abuse_category_3),
                getString(R.string.report_abuse_category_4)
        };
        builder.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int idCategory = which + 1;
                openInsertCommentPopUp(idCategory);
            }
        });
        builder.setNeutralButton(getString(R.string.report_abuse_popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openBlockUserPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.block_user_popup));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.block_user_popup_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                controller.blockUser();
            }
        });
        builder.setNeutralButton(getString(R.string.block_user_popup_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openInsertCommentPopUp(int idCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(Boolean.FALSE);
        final int idC = idCategory;
        builder.setTitle(getResources().getString(R.string.report_abuse_comment_title));

        final EditText newCommentInput = new EditText(this);
        newCommentInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newCommentInput);

        builder.setPositiveButton(getResources().getString(R.string.report_abuse_comment_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.reportAbuse(idC, newCommentInput.getText().toString());
            }
        });

        builder.show();
    }

    protected void loadMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.linkMap);
        mapFragment.getMapAsync(this);
    }

    public void showProgress() {
        String message = getResources().getString(R.string.loading_link_profile);
        progressDialog = ProgressDialog.show(this, "", message, true, false);
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void goToNext() {}

    public Context getContext() {
        return this;
    }

    public void onError(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(Boolean.FALSE);
        builder.setMessage(R.string.error_loading_link_profile);
        builder.setNeutralButton(R.string.error_loading_link_profile_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void showData(Profile profile) {
        String age = "";
        try {
            age = String.valueOf(DateUtils.getAgeFromBirthday(profile.getBirthday()));
        } catch (MissingAgeException e) {
            e.printStackTrace();
        }
        textViewLinkName.setText(profile.getFirstName() + ", " + age);

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
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            Snackbar snackbar = Snackbar.make(coordView, text, Snackbar.LENGTH_INDEFINITE);
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

    @Override
    public void showReportAbuseProgress() {
        String message = getResources().getString(R.string.report_abuse_loading);
        progressDialog = ProgressDialog.show(this, "", message, true, false);
        progressDialog.show();
    }

    @Override
    public void showBlockUserProgress() {
        String message = getResources().getString(R.string.block_user_loading);
        progressDialog = ProgressDialog.show(this, "", message, true, false);
        progressDialog.show();
    }

    @Override
    public void onReportAbuseSuccess() {
        showAfterTaskDialog(R.string.report_abuse_success, R.string.report_abuse_success_ok, true);
    }

    @Override
    public void onReportAbuseFailure() {
        showAfterTaskDialog(R.string.report_abuse_failure, R.string.report_abuse_failure_ok, false);
    }

    @Override
    public void onBlockUserSuccess() {
        showAfterTaskDialog(R.string.block_user_success, R.string.block_user_success_ok, true);
    }

    @Override
    public void onBlockUserFailure() {
        showAfterTaskDialog(R.string.block_user_failure, R.string.block_user_failure_ok, false);
    }

    private void showAfterTaskDialog(int description, int textButton, final boolean leaveActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(description));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(textButton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (leaveActivity) {
                    Intent intent = new Intent(getContext(), LinksActivity.class);
                    Notification notification = new Notification();
                    intent.putExtra("notification", notification);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

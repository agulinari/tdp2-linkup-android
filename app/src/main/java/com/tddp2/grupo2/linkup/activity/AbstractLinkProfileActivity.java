package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LinkProfileView;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Profile;
import com.tddp2.grupo2.linkup.utils.DateUtils;

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

    protected String TAG;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_profile);

        ButterKnife.bind(this);
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
            Snackbar snackbar = Snackbar.make(coordView, notification.messageBody, Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();

    }
}

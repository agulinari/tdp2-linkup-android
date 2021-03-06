package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import com.daimajia.slider.library.SliderLayout;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.PictureSliderView;
import com.tddp2.grupo2.linkup.activity.view.ProfileLocationView;
import com.tddp2.grupo2.linkup.controller.LocationController;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Interest;
import com.tddp2.grupo2.linkup.model.Profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends BroadcastActivity implements ProfileLocationView {
    private ProgressDialog progressDialog;

    ProfileController controller;
    LocationController locationController;

    @BindView(R.id.userNameAndAge)
    TextView textViewUserNameAndAge;

    @BindView(R.id.userOccupationCard)
    CardView cardViewUserOccupation;

    @BindView(R.id.userWorkText)
    TextView textViewUserWork;

    @BindView(R.id.userEducationCard)
    CardView cardViewUserEducation;

    @BindView(R.id.userStudiesText)
    TextView textViewUserStudies;

    @BindView(R.id.userProfilePicture)
    SliderLayout profilePicture;

    @BindView(R.id.userCommentText)
    TextView textViewUserComment;

    @BindView(R.id.editCommentIcon)
    ImageView imageEditComment;

    @BindView(R.id.userLocationText)
    TextView textViewUserLocation;

    @BindView(R.id.userLocationCard)
    CardView cardViewUserLocation;

    @BindView(R.id.profileImageProgress)
    ProgressBar progressBarImage;

    @BindView(R.id.tagcontainerLayout)
    TagContainerLayout mTagContainerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        imageEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommentEditorPopUp();
            }
        });

        controller = new ProfileController(this);
        controller.update();
        locationController = new LocationController(this, this);
        checkPermissionsAndLoadLocation();
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void updateFirstNameAndAge(String firstName, String age) {
        textViewUserNameAndAge.setText(firstName + ", " + age);
    }

    @Override
    public void updateOccupation(String occupation) {
        cardViewUserOccupation.setVisibility(View.VISIBLE);
        textViewUserWork.setText(occupation);
    }

    @Override
    public void hideOccupation() {
        cardViewUserOccupation.setVisibility(View.GONE);
    }

    @Override
    public void updateEducation(String education) {
        cardViewUserEducation.setVisibility(View.VISIBLE);
        textViewUserStudies.setText(education);
    }

    @Override
    public void hideEducation() {
        cardViewUserEducation.setVisibility(View.GONE);
    }

    @Override
    public void updateComment(String comment) {
        textViewUserComment.setText(comment);
        Profile localProfile = controller.getProfileService().getLocalProfile();
        localProfile.setComments(comment);
    }

    @Override
    public void loadUserPictures() {
        profilePicture.stopAutoCycle();
        profilePicture.removeAllSliders();

        controller.loadImages();

    }

    @Override
    public void loadInterests(List<Interest> interests) {
        mTagContainerLayout.removeAllTags();
        List<String> tags = new ArrayList<>();
        for (Interest interest : interests) {
            tags.add(interest.getInterest());
        }
        mTagContainerLayout.setTags(tags);
    }

    @Override
    public void showProgress() {
        findViewById(R.id.loadingUser).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.loadingUser).setVisibility(View.GONE);
    }

    @Override
    public void goToNext() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void openCommentEditorPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.comment_edit));

        final EditText newCommentInput = new EditText(this);
        newCommentInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newCommentInput);

        builder.setPositiveButton(getResources().getString(R.string.comment_edit_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comment = newCommentInput.getText().toString();
                controller.saveNewComment(comment);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.comment_edit_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    protected void showPopUpAndEnd(String message) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finishAndRemoveTask();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void checkPermissionsAndLoadLocation() {
        locationController.checkPermissionsAndLoadLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationController.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationController.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsDenied() {
        showPopUpAndEnd(getResources().getString(R.string.location_restriction));
    }

    @Override
    public void onLocationError() {
        showPopUpAndEnd(getResources().getString(R.string.location_error));
    }

    @Override
    public void onChangeSettingsDenied() {
        showPopUpAndEnd(getResources().getString(R.string.location_restriction));
    }

    @Override
    public void showFetchingLocationMessage() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.fetching_location), true, false);
        progressDialog.show();
    }

    @Override
    public void hideFetchingLocationMessage() {
        progressDialog.hide();
    }

    @Override
    public void updateLocationView(String locationName) {
        cardViewUserLocation.setVisibility(View.VISIBLE);
        textViewUserLocation.setText(locationName);
    }

    @Override
    public void showImage(List<Bundle> bundles) {
        profilePicture.stopAutoCycle();
        profilePicture.removeAllSliders();
        for (Bundle bundle : bundles) {
            PictureSliderView pictureSliderView = new PictureSliderView(this);
            pictureSliderView.bundle(bundle);
            profilePicture.addSlider(pictureSliderView);
        }
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
        broadcastReceiver.abortBroadcast();
    }
}

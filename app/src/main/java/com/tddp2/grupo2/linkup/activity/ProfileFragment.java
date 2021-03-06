package com.tddp2.grupo2.linkup.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import com.daimajia.slider.library.SliderLayout;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.PictureSliderView;
import com.tddp2.grupo2.linkup.activity.view.ProfileLocationView;
import com.tddp2.grupo2.linkup.controller.LocationController;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.model.Interest;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends BroadcastFragment implements ProfileLocationView {

    private static final String TAG = "ProfileFragment";

    ProfileController controller;
    LocationController locationController;
    private Context activity;

    @BindView(R.id.profileCoordinatorLayout)
    CoordinatorLayout coordView;

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

    @BindView(R.id.reloadFromFacebookButton)
    Button buttonReloadFacebook;

    @BindView(R.id.reloadLocationButton)
    Button buttonReloadLocation;

    @BindView(R.id.saveProfileButton)
    Button buttonSave;

    @BindView(R.id.userLocationText)
    TextView textViewUserLocation;

    @BindView(R.id.userLocationCard)
    CardView cardViewUserLocation;

    @BindView(R.id.profileImageProgress)
    ProgressBar progressBarImage;

    @BindView(R.id.tagcontainerLayout)
    TagContainerLayout mTagContainerLayout;

    private ProgressDialog progressDialog;
    private boolean savingProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        // Toolbar:
        setHasOptionsMenu(true);

        // View:
        View mainView = inflater.inflate(R.layout.fragment_profile, container, false);
        mainView.setTag(TAG);


        ButterKnife.bind(this, mainView);

        registerListeners();

        controller = new ProfileController(this);
        controller.update();
        locationController = new LocationController(this, getActivity());

        return mainView;
    }

    private void registerListeners() {

        buttonSave.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                savingProfile = true;
                saveProfile(v);
            }
        });

        buttonReloadFacebook.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                savingProfile = false;
                reloadDataFromFacebook(v);
            }
        });

        buttonReloadLocation.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkPermissionsAndLoadLocation();
            }
        });

        imageEditComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommentEditorPopUp();
            }
        });
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
        String message = savingProfile ? getResources().getString(R.string.saving_profile) : getResources().getString(R.string.fetching_facebook_info);
        progressDialog = ProgressDialog.show(getActivity(), "", message, true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showAfterProfileSaveDialog(boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        int title = success ? R.string.save_profile_title_ok : R.string.save_profile_title_error;
        builder.setTitle(title);
        int text = success ? R.string.save_profile_text_ok : R.string.save_profile_text_error;
        builder.setMessage(getString(text));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.save_settings_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void goToNext() {
        if (savingProfile) {
            showAfterProfileSaveDialog(true);
        }
    }

    @Override
    public Context getContext() {
        return this.getActivity();
    }

    @Override
    public void onError(String errorMsg) {
        if (savingProfile) {
            showAfterProfileSaveDialog(false);
        }
        Toast.makeText(getActivity().getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    private void openCommentEditorPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getResources().getString(R.string.comment_edit));

        final EditText newCommentInput = new EditText(this.getActivity());
        newCommentInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newCommentInput);

        builder.setPositiveButton(getResources().getString(R.string.comment_edit_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.saveNewComment(newCommentInput.getText().toString());
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

    /* On Click button saveProfile */
    public void saveProfile(View view){
        String comment = textViewUserComment.getText().toString();
        controller.saveProfile(comment);
    }

    public void reloadDataFromFacebook(View view){
        controller.reloadDataFromFacebook();
    }

    @Override
    public void checkPermissionsAndLoadLocation() {
        locationController.checkPermissionsAndLoadLocation();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationController.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationController.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsDenied() {
        showPopUp(getResources().getString(R.string.location_profile_fragment_permission_denied));
    }

    @Override
    public void onLocationError() {
        showPopUp(getResources().getString(R.string.location_profile_error));
    }

    @Override
    public void onChangeSettingsDenied() {
        showPopUp(getResources().getString(R.string.location_profile_fragment_settings_denied));
    }

    @Override
    public void showFetchingLocationMessage() {
        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.fetching_location), true, false);
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
        for (Bundle b : bundles) {
            PictureSliderView pictureSliderView = new PictureSliderView(getActivity());
            pictureSliderView.bundle(b);
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

    private void showPopUp(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton(getResources().getString(R.string.location_popup_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");

        if (notification!=null && isAdded()) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }

            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            Log.i(TAG, "Sending notification");

            if (ServiceFactory.getProfileService()!=null && ServiceFactory.getProfileService().getLocalProfile()!=null){
                String fbid = ServiceFactory.getProfileService().getLocalProfile().getFbid();
                if (!notification.fbidTo.equals(fbid)){
                    return;
                }
                if (notification.motive.equals(Notification.BAN)){
                    ServiceFactory.getLinksService().getDatabase().setActive(false);
                    return;
                }
            }else{
                return;
            }
            // First create Parcel and write your data to it
            Parcel parcel = Parcel.obtain();
            notification.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
            notificationIntent.putExtra("notification",parcel.marshall());

            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_linkup)
                    .setContentTitle(notification.messageTitle)
                    .setVibrate(new long[] { 1000, 1000})
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(android.app.Notification.PRIORITY_MAX);

            mNotificationManager.notify("default-push", 1, mBuilder.build());
        }

        broadcastReceiver.abortBroadcast();
    }
}

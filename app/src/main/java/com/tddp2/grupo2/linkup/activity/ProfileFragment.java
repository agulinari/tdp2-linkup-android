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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.daimajia.slider.library.SliderLayout;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.PictureSliderView;
import com.tddp2.grupo2.linkup.activity.view.ProfileLocationView;
import com.tddp2.grupo2.linkup.controller.LocationController;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

import java.io.ByteArrayOutputStream;

/**
 * Created by agustin on 09/09/2017.
 */

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
    public void updateProfilePicture(Bitmap picture) {
        PictureSliderView pictureSliderView = new PictureSliderView(getActivity());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Bundle b = new Bundle();
        b.putByteArray("image", byteArray);
        pictureSliderView.bundle(b);

        profilePicture.stopAutoCycle();
        profilePicture.addSlider(pictureSliderView);
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

        if (notification!=null) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            Snackbar snackbar = Snackbar.make(coordView, text, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();
    }
}

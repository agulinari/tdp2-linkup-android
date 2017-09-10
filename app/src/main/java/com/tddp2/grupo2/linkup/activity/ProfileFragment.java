package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.LoginActivity;
import com.tddp2.grupo2.linkup.MainActivity;
import com.tddp2.grupo2.linkup.ProfileView;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.SettingsActivity;
import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.controller.SettingsController;
import com.tddp2.grupo2.linkup.model.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by agustin on 09/09/2017.
 */

public class ProfileFragment extends Fragment implements ProfileView{

    private static final String TAG = "ProfileFragment";

    ProfileController controller;
    private Context activity;

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
    ImageView profilePicture;

    @BindView(R.id.userCommentText)
    TextView textViewUserComment;

    @BindView(R.id.editCommentIcon)
    ImageView imageEditComment;

    @BindView(R.id.reloadFromFacebookButton)
    Button buttonUpdate;

    private ProgressDialog progressDialog;

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

        return mainView;
    }

    private void registerListeners() {

        buttonUpdate.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveProfile(v);
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
    public void updateFirstNameAndAge(String firstName, int age) {
        textViewUserNameAndAge.setText(firstName + ", " + String.valueOf(age));
    }

    @Override
    public void updateOccupation(String occupation) {
        cardViewUserOccupation.setVisibility(View.VISIBLE);
        textViewUserWork.setText(occupation);
    }

    @Override
    public void updateEducation(String education) {
        cardViewUserEducation.setVisibility(View.VISIBLE);
        textViewUserStudies.setText(education);
    }

    @Override
    public void updateComment(String comment) {
        textViewUserComment.setText(comment);
    }

    @Override
    public void updateProfilePicture(Bitmap picture) {
        profilePicture.setImageBitmap(picture);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.saving_profile), true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void goToNext() {

    }

    @Override
    public void sessionExpired() {}

    @Override
    public Context getContext() {
        return this.getActivity();
    }

    @Override
    public void onError(String errorMsg) {
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

}

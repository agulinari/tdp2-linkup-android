package com.tddp2.grupo2.linkup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.controller.ProfileController;

public class ProfileActivity extends AppCompatActivity implements ProfileView {
    ProfileController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        controller = new ProfileController(this);
        controller.update();
    }

    public void onSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void updateFirstNameAndAge(String firstName, int age) {
        TextView textView = (TextView) findViewById(R.id.userNameAndAge);
        textView.setText(firstName + ", " + String.valueOf(age));
    }

    @Override
    public void updateOccupation(String occupation) {
        CardView cardView = (CardView) findViewById(R.id.userOccupationCard);
        cardView.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.userWorkText);
        textView.setText(occupation);
    }

    @Override
    public void updateEducation(String education) {
        CardView cardView = (CardView) findViewById(R.id.userEducationCard);
        cardView.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.userStudiesText);
        textView.setText(education);
    }

    @Override
    public void updateComment(String comment) {
        CardView cardView = (CardView) findViewById(R.id.userCommentCard);
        cardView.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.userCommentText);
        textView.setText(comment);
    }

    @Override
    public void updateProfilePicture(Bitmap picture) {
        ImageView profilePicture = (ImageView) findViewById(R.id.userProfilePicture);
        profilePicture.setImageBitmap(picture);
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void sessionExpired() {}

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}

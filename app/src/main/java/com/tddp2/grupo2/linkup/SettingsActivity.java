package com.tddp2.grupo2.linkup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import com.tddp2.grupo2.linkup.model.Profile;

import com.tddp2.grupo2.linkup.controller.ProfileController;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class SettingsActivity extends AppCompatActivity implements BaseView {

    @BindView(R.id.seek_age)
    SeekBar seekBarAge;

    @BindView(R.id.seek_distance)
    SeekBar seekBarDistance;

    ProfileController controller;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ServiceFactory.init();
        controller = new ProfileController(this);
    }

    /* On Click button saveProfile */
    public void saveProfile(View view){
        controller.saveProfile();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.saving_profile), true, false);
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}

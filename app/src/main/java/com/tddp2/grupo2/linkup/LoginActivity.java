package com.tddp2.grupo2.linkup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tddp2.grupo2.linkup.controller.LoginController;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "user_birthday",
                "user_education_history",
                "user_work_history",
                "user_about_me"
        ));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loadUser();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void showProgress() {
        findViewById(R.id.loadingLogin).setVisibility(View.VISIBLE);
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.fetching_facebook_info), true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.loadingLogin).setVisibility(View.GONE);
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
    public void sessionExpired() {}

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    public void goProfileScreen() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showProfilePictureRestrictionAndEnd() {
        showPopUpAndEnd("Lo sentimos, es necesario tener una foto de perfil en Facebook para poder utilizar esta aplicación. Por favor, agrega una y vuelve a intentarlo.");
    }

    public void showAgeRestrictionAndEnd() {
        showPopUpAndEnd("Lo sentimos, debes ser mayor de 18 años para utilizar esta aplicación.");
    }

    public void showMissingAgeAndEnd() {
        showPopUpAndEnd("Lo sentimos, es necesario conocer tu edad para que puedas usar esta aplicación. Por favor, agrega esa información en Facebook y vuelve a intentarlo.");
    }

    private void showPopUpAndEnd(String message) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loadUser() {
        LoginController controller = new LoginController(this);
        controller.loadUser();
    }
}

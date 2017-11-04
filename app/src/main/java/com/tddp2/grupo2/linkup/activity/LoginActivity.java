package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.LoginView;
import com.tddp2.grupo2.linkup.controller.LoginController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private static final String TAG = "Login Activity";

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private Notification n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        n = getIntent().getParcelableExtra("notification");

        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(
                "user_birthday",
                "user_education_history",
                "user_work_history",
                "user_about_me",
                "user_photos",
                "user_likes"
        ));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loadUser();
                        handleFacebookAccessToken(loginResult.getAccessToken());
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

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ServiceFactory.getLinksService().getDatabase().setActive(true);
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


    public void goLinksScreen() {
        Intent intent = new Intent(this, LinksActivity.class);
        intent.putExtra("notification",n);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showProfilePictureRestrictionAndEnd() {
        showPopUpAndEnd(getResources().getString(R.string.missing_photo));
    }

    public void showAgeRestrictionAndEnd() {
        showPopUpAndEnd(getResources().getString(R.string.age_restriction));
    }

    public void showMissingAgeAndEnd() {
        showPopUpAndEnd(getResources().getString(R.string.missing_age));
    }

    private void showPopUpAndEnd(String message) {
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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null) {
            Log.i(TAG, currentUser.getDisplayName());

        }else{
            Log.i(TAG, "No currentUser");
        }
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

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

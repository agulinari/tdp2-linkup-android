package com.tddp2.grupo2.linkup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        byte[] byteArrayExtra = getIntent().getByteArrayExtra("notification");
        Notification notification = null;
        if (byteArrayExtra != null) {
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(byteArrayExtra, 0, byteArrayExtra.length);
            parcel.setDataPosition(0);
            notification = Notification.CREATOR.createFromParcel(parcel);
            Log.i(TAG, notification.fbid);
        }else{
            Log.i(TAG, "Bundle NULL");
            notification = new Notification("","","");
        }

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen(notification);
        }else {
            boolean isUserRegistered = ServiceFactory.getLoginService().isUserRegistered();
            if (!isUserRegistered) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen(notification);
            } else {
                goLinksScreen(notification);
            }
        }
    }

    private void goLoginScreen(Notification notification) {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goLinksScreen(Notification notification) {
        Intent intent = new Intent(this, LinksActivity.class);
        intent.putExtra("notification", notification);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

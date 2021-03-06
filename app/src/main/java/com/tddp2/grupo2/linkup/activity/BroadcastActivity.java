package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;


public abstract class BroadcastActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter("io.esparta.notifications.BROADCAST_NOTIFICATION");
        filter.setPriority(1);

        registerReceiver(notificationReceiver, filter);

        if (!ServiceFactory.getLinksService().getDatabase().isActive()){
            logout();
            return;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("notification", new Notification());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Do something with this incoming message here
            // Since we will process the message and update the UI, we don't need to show a message in Status Bar
            // To do this, we call abortBroadcast()

            Notification notification = intent.getParcelableExtra("notification");
            Profile profile = Profile.getCurrentProfile();
            if (profile!=null && !notification.fbidTo.equals(profile.getId())){
                notificationReceiver.abortBroadcast();
            }else if (notification.motive.equals(Notification.BAN)){
                notificationReceiver.abortBroadcast();
                logout();
            }else{
                handleNotification(notification, this);
            }
        }
    };

    protected abstract void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver);
}

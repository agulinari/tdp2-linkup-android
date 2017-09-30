package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class LinkupMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final int FM_NOTIFICATION_ID = 4132 ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.i(TAG, "Notification received");

        Map<String, String> data = remoteMessage.getData();

        //you can get your text message here.
        String fbid = data.get("fbid");
        String title= data.get("title");
        String body = data.get("body");

        Intent intent = new Intent(this, PushReceiverIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putString("fbid", fbid);
        bundle.putString("title", title);
        bundle.putString("body", body);
        intent.putExtras(bundle);
        startService(intent);
    }
}
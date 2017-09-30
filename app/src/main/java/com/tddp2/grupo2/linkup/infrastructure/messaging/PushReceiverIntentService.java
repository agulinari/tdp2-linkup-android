package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PushReceiverIntentService extends IntentService {

    private static final String TAG = "PushRIntentService";

    Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                throw new IllegalArgumentException("PUSH_RECEIVED NOT HANDLED!");
            }
    };


    public PushReceiverIntentService() {
        super("PushReceiverIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String body = intent.getStringExtra("body");
        String title = intent.getStringExtra("title");
        Notification notification = new Notification(title, body);

        sendNotification(notification);

        //GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Notification notification) {
        sendNotification(notification, new Bundle());
    }

    private void sendNotification(Notification notification, Bundle extras) {
        Log.i(TAG, "Sending Broadcast");
        Intent broadcast = new Intent();
        extras.putParcelable("notification", notification);
        broadcast.putExtras(extras);
        broadcast.setAction("io.esparta.notifications.BROADCAST_NOTIFICATION");

        sendOrderedBroadcast(broadcast, null, null, new Handler(callback), Activity.RESULT_OK, null, extras);
    }
}

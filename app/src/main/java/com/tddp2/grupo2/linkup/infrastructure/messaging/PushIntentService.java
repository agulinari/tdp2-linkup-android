package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tddp2.grupo2.linkup.activity.MainActivity;
import com.tddp2.grupo2.linkup.R;

public class PushIntentService extends IntentService {

    private static final String TAG = "PushIntentService";


    public PushIntentService() {
        super("PushIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handleNotification(intent.getExtras());
    }

    private void handleNotification(Bundle extras) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = extras.getParcelable("notification");

        Log.i(TAG, "Sending notification");

        if (notification == null) {
            return;
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_linkup)
                .setContentTitle(notification.title)
                .setContentText(notification.message)
                .setAutoCancel(true);

        mNotificationManager.notify("default-push", 1, mBuilder.build());
    }

}

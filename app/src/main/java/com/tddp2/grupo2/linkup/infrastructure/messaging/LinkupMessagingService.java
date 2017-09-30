package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.content.Intent;
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
        String title= data.get("title");
        String body = data.get("body");

        Intent intent = new Intent(this, PushReceiverIntentService.class);

        intent.putExtra("title", title);
        intent.putExtra("body", body);
        startService(intent);

       /* Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_linkup)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_linkup))
                .setTicker(res.getString(R.string.ticker))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(body);
        Notification n = builder.build();

        nm.notify(FM_NOTIFICATION_ID, n);*/
    }
}
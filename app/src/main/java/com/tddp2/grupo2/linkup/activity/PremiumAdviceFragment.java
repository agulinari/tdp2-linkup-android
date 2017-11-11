package com.tddp2.grupo2.linkup.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class PremiumAdviceFragment extends BroadcastFragment {

    private static final String TAG = "PremiumAdviceFragment";
    private Context activity;

    @BindView(R.id.premiumAdviceCoordinatorLayout)
    CoordinatorLayout coordView;

    @BindView(R.id.goToPremiumPayFormButton)
    Button buttonGoToPayForm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        setHasOptionsMenu(true);

        View mainView = inflater.inflate(R.layout.fragment_premium_advice, container, false);
        mainView.setTag(TAG);

        ButterKnife.bind(this, mainView);

        registerListeners();

        return mainView;
    }

    private void registerListeners() {
        buttonGoToPayForm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PremiumPayFormActivity.class);
                //Bundle b = new Bundle();
                //b.putInt("currentLinkIndex", currentLinkIndex);
                //intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");

        if (notification!=null && isAdded()) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            Log.i(TAG, "Sending notification");

            if (ServiceFactory.getProfileService()!=null && ServiceFactory.getProfileService().getLocalProfile()!=null){
                String fbid = ServiceFactory.getProfileService().getLocalProfile().getFbid();
                if (!notification.fbidTo.equals(fbid)){
                    return;
                }
                if (notification.motive.equals(Notification.BAN)){
                    ServiceFactory.getLinksService().getDatabase().setActive(false);
                    return;
                }
            }else{
                return;
            }
            // First create Parcel and write your data to it
            Parcel parcel = Parcel.obtain();
            notification.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
            notificationIntent.putExtra("notification",parcel.marshall());

            PendingIntent contentIntent = PendingIntent.getActivity(getActivity(),
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                    .setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_linkup)
                    .setContentTitle(notification.messageTitle)
                    .setVibrate(new long[] { 1000, 1000})
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setPriority(android.app.Notification.PRIORITY_MAX);

            mNotificationManager.notify("default-push", 1, mBuilder.build());
        }

        broadcastReceiver.abortBroadcast();
    }
}

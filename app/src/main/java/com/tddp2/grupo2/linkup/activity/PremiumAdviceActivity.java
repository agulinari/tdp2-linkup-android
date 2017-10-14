package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumAdviceActivity extends BroadcastActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_advice);
        ButterKnife.bind(this);
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        broadcastReceiver.abortBroadcast();
    }
}

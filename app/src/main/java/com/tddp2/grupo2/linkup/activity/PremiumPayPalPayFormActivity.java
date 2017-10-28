package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumPayPalPayFormActivity extends BroadcastActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_pay_pal_pay_form);
        ButterKnife.bind(this);
        registerListeners();
    }

    private void registerListeners() {

    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        //FIXME: No la deberia handlear en vez de abortarla?
        broadcastReceiver.abortBroadcast();
    }
}

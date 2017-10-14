package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import butterknife.ButterKnife;
import com.braintreepayments.cardform.view.CardForm;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumPayFormActivity extends BroadcastActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_pay_form);
        ButterKnife.bind(this);

        CardForm cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(this);
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        broadcastReceiver.abortBroadcast();
    }
}

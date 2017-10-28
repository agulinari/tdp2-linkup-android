package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumPayFormActivity extends BroadcastActivity {
    @BindView(R.id.creditCardOption)
    LinearLayout linearLayoutCreditCardOption;

    @BindView(R.id.payPalOption)
    LinearLayout linearLayoutPayPalOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_pay_form);
        ButterKnife.bind(this);
        registerListeners();
    }

    private void registerListeners() {
        linearLayoutCreditCardOption.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
               startCreditCardFormActivity();
            }
        });

        linearLayoutPayPalOption.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayPalFormActivity();
            }
        });
    }

    private void startCreditCardFormActivity() {
        Intent intent = new Intent(this, PremiumCreditCardPayFormActivity.class);
        startActivity(intent);
        finish();
    }

    private void startPayPalFormActivity() {
        Intent intent = new Intent(this, PremiumPayPalLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        //FIXME: No la deberia handlear en vez de abortarla?
        broadcastReceiver.abortBroadcast();
    }
}

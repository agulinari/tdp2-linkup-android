package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.BaseView;
import com.tddp2.grupo2.linkup.controller.PremiumPayFormController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumPayFormActivity extends BroadcastActivity implements BaseView {
    @BindView(R.id.payFormButton)
    Button buttonPayForm;

    private PremiumPayFormController controller;
    private ProgressDialog progressDialog;
    private CardForm cardForm;
    private CardType cardType;

    private static final String VISA = "VISA";
    private static final String MASTERCARD = "MASTERCARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_pay_form);
        ButterKnife.bind(this);

        this.cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(this);


        this.controller = new PremiumPayFormController(this);
        registerListeners();
    }

    private void registerListeners() {
        cardForm.setOnCardTypeChangedListener(new CardEditText.OnCardTypeChangedListener() {
            @Override
            public void onCardTypeChanged(CardType card) {
                cardType = card;
            }
        });

        buttonPayForm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardForm.isValid()) {
                    showMissingFieldsAlert();
                } else if (!cardType.name().equals(VISA) && !cardType.name().equals(MASTERCARD)) {
                    showInvalidCardAlert();
                } else {
                    controller.upgradeAccount();
                }
            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.button_pay_loading_text), true, false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void goToNext() {}

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        broadcastReceiver.abortBroadcast();
    }

    private void showMissingFieldsAlert() {
        showErrorAlert(R.string.pay_missing_fields);
    }

    private void showInvalidCardAlert() {
        showErrorAlert(R.string.pay_invalid_card);
    }

    private void showErrorAlert(int textId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(textId));
        builder.setCancelable(Boolean.FALSE);
        builder.setPositiveButton(getString(R.string.pop_up_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

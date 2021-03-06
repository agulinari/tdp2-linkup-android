package com.tddp2.grupo2.linkup.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.BaseView;
import com.tddp2.grupo2.linkup.controller.PremiumPayFormController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class PremiumCreditCardPayFormActivity extends BroadcastActivity implements BaseView {
    @BindView(R.id.creditCardPayFormRelativeLayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.payFormButton)
    Button buttonPayForm;

    private PremiumPayFormController controller;
    private ProgressDialog progressDialog;
    private CardForm cardForm;
    private CardType cardType;

    private static final String VISA = "VISA";
    private static final String MASTERCARD = "MASTERCARD";

    private static final String TAG = "CreditCardPayFormActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_credit_card_pay_form);
        ButterKnife.bind(this);

        this.cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
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
    public void goToNext() {
        showAfterPayDialog(true);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onError(String errorMsg) {
        showAfterPayDialog(false);
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        Log.i(TAG, "Notificacion RECIBIDA");

        if (notification!=null) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
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

            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.putExtra("notification",parcel.marshall());

            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
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

    private void showAfterPayDialog(boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int title = success ? R.string.premium_pay_attempt_success_title : R.string.premium_pay_attempt_failure_title;
        builder.setTitle(title);
        int text = success ? R.string.premium_pay_attempt_success_text : R.string.premium_pay_attempt_failure_text;
        builder.setMessage(getString(text));
        builder.setCancelable(Boolean.FALSE);
        final boolean successOperation = success;
        builder.setPositiveButton(getString(R.string.save_settings_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (successOperation) {
                    Intent intent = new Intent(getContext(), LinksActivity.class);
                    Notification notification = new Notification();
                    intent.putExtra("notification", notification);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

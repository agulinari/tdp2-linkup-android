package com.tddp2.grupo2.linkup.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.activity.view.BaseView;
import com.tddp2.grupo2.linkup.controller.PremiumPayFormController;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

public class PremiumPayPalPayFormActivity extends BroadcastActivity implements BaseView {
    @BindView(R.id.payPalPayFormRelativeLayout)
    RelativeLayout relativeLayout;

    @BindView(R.id.payPalAvailableText)
    TextView textAvailable;

    @BindView(R.id.payPalPriceText)
    TextView textPrice;

    @BindView(R.id.payPalTotalText)
    TextView textTotal;

    @BindView(R.id.payPalPayError)
    TextView priceError;

    @BindView(R.id.payPalPayFormButton)
    Button buttonPay;

    private static final String TAG = "PayPalPayFormActivity";
    private final int PRICE = 100;

    private PremiumPayFormController controller;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_pay_pal_pay_form);
        ButterKnife.bind(this);

        this.controller = new PremiumPayFormController(this);
        registerListeners();

        Bundle b = getIntent().getExtras();
        int saldo = (b != null) ? b.getInt("credit") : 0;
        showInfo(saldo);
    }

    private void showInfo(int saldo) {
        String available = "$" + String.valueOf(saldo);
        String price = "- $" + String.valueOf(PRICE);
        String total = "--";
        if (PRICE <= saldo) {
            int res = saldo - PRICE;
            total = "$" + String.valueOf(res);
        } else {
            priceError.setVisibility(View.VISIBLE);
            buttonPay.setVisibility(View.GONE);
        }
        textAvailable.setText(available);
        textPrice.setText(price);
        textTotal.setText(total);
    }

    private void registerListeners() {
        buttonPay.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.upgradeAccount();
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
            Snackbar snackbar = Snackbar.make(relativeLayout, text, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();
    }
}

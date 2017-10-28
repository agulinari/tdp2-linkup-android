package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

import java.util.HashMap;
import java.util.Map;

public class PremiumPayPalLoginActivity extends BroadcastActivity {
    @BindView(R.id.payPalLogin)
    Button buttonPayPalLogin;

    @BindView(R.id.payPalLoginUsername)
    EditText editTextUsername;

    @BindView(R.id.payPalLoginPassword)
    EditText editTextPassword;

    private Map<String, Map<String, String>> allowedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_premium_pay_pal_login);
        ButterKnife.bind(this);

        this.allowedUsers = getAllowedUsers();

        registerListeners();
    }

    private void registerListeners() {
        buttonPayPalLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.equals("") || password.equals("")) {
                    showMissingFieldsError();
                } else {
                    if (!allowedUsers.containsKey(username)) {
                        showInvalidUserError();
                    } else {
                        goToPayForm(allowedUsers.get(username).get("credit"));
                    }
                }
            }
        });
    }

    private void goToPayForm(String saldo) {
        Intent intent = new Intent(this, PremiumPayPalPayFormActivity.class);
        Bundle b = new Bundle();
        b.putInt("credit", Integer.parseInt(saldo));
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }

    private void showMissingFieldsError() {
        showErrorAlert(R.string.premium_paypal_missing_fields);
    }

    private void showInvalidUserError() {
        showErrorAlert(R.string.premium_paypal_invalid_user);
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

    private Map<String, Map<String, String>> getAllowedUsers() {
        Map<String, Map<String, String>> allowedUsers = new HashMap<>();

        Map<String, String> user1 = new HashMap<>();
        user1.put("password", "taller2");
        user1.put("credit", "500");

        allowedUsers.put("user1@gmail.com", user1);

        Map<String, String> user2 = new HashMap<>();
        user2.put("password", "taller2");
        user2.put("credit", "50");

        allowedUsers.put("user2@gmail.com", user2);

        return allowedUsers;
    }

    @Override
    protected void handleNotification(Notification notification, BroadcastReceiver broadcastReceiver) {
        //FIXME: No la deberia handlear en vez de abortarla?
        broadcastReceiver.abortBroadcast();
    }
}

package com.tddp2.grupo2.linkup.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.infrastructure.messaging.Notification;

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

        if (notification!=null) {
            String text;
            if (notification.motive.equals(Notification.CHAT)) {
                text = notification.firstName + ": " + "'" + notification.messageBody + "'";
            }else{
                text = notification.messageBody;
            }
            Snackbar snackbar = Snackbar.make(coordView, text, Snackbar.LENGTH_INDEFINITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            snackbar.show();
        }

        broadcastReceiver.abortBroadcast();
    }
}

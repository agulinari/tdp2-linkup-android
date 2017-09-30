package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Messsage received");
        Intent push = new Intent(context, PushIntentService.class);
        push.putExtras(getResultExtras(true));
        context.startService(push);
        setResultCode(Activity.RESULT_OK);
    }
}
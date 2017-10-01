package com.tddp2.grupo2.linkup.infrastructure.messaging;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tddp2.grupo2.linkup.exception.ServiceException;
import com.tddp2.grupo2.linkup.service.api.NotificationService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        ServiceFactory.getNotificationService().saveToken(refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        NotificationService notificationService = ServiceFactory.getNotificationService();

        try {
            notificationService.updateToken(token);
        } catch (ServiceException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}
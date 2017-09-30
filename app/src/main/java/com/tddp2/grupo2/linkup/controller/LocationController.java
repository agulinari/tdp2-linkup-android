package com.tddp2.grupo2.linkup.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.tddp2.grupo2.linkup.activity.view.ProfileLocationView;
import com.tddp2.grupo2.linkup.service.api.FetchAddressIntentService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;
import com.tddp2.grupo2.linkup.task.SaveLocationDataTask;

import static android.app.Activity.RESULT_OK;

public class LocationController {
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 1;
    private ProfileLocationView view;
    private Activity activity;
    private ProfileService profileService;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private com.tddp2.grupo2.linkup.model.Location userLocation;

    public LocationController(ProfileLocationView view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.profileService = ServiceFactory.getProfileService();

        mFusedLocationClient = new FusedLocationProviderClient(activity);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        userLocation = new com.tddp2.grupo2.linkup.model.Location();
    }

    public void checkPermissionsAndLoadLocation() {
        view.showFetchingLocationMessage();
        if (ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    LocationController.PERMISSION_REQUEST_ACCESS_LOCATION);
        } else {
            loadLocation();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length < 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionsDenied();
                } else {
                    loadLocation();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationController.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getLastLocation();
            } else {
                Log.i("LOCATION", "cancel");
                onChangeSettingsDenied();
            }
        }
    }

    private void loadLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getLastLocation();
            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity, LocationController.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            Log.i("LOCATION", "ignore");
                            onLocationError();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        Log.i("LOCATION", "error");
                        onLocationError();
                        break;
                }
            }
        });
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                afterLocationFetch(location);
                            } else {
                                waitForLocation();
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.e("LOCATION", e.getMessage());
            onLocationError();
        }
    }

    private void waitForLocation() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLocations().get(0);
                onLocationFetched(location);
            }
        };
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        } catch (SecurityException e) {
            Log.e("LOCATION", e.getMessage());
            onLocationError();
        }
    }

    private void onLocationFetched(Location location) {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        afterLocationFetch(location);
    }

    private void afterLocationFetch(Location location) {
        userLocation.setLongitude(location.getLongitude());
        userLocation.setLatitude(location.getLatitude());
        Log.i("LOCATION", "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());
        loadLocationName(location);
    }

    private void loadLocationName(Location location) {
        Intent intent = new Intent(view.getContext(), FetchAddressIntentService.class);

        class AddressResultReceiver extends ResultReceiver {
            public AddressResultReceiver(Handler handler) {
                super(handler);
            }

            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                    String locationName = resultData.getString(FetchAddressIntentService.RESULT_DATA_KEY);
                    userLocation.setName(locationName);
                    saveLocation();
                } else {
                    onLocationError();
                }
            }
        }

        intent.putExtra(FetchAddressIntentService.RECEIVER, new AddressResultReceiver(new Handler()));
        intent.putExtra(FetchAddressIntentService.LOCATION_DATA_EXTRA, location);
        activity.startService(intent);
    }

    private void saveLocation() {
        SaveLocationDataTask saveLocationDataTask = new SaveLocationDataTask(profileService, this);
        saveLocationDataTask.execute(userLocation);
    }

    private void onPermissionsDenied() {
        view.hideFetchingLocationMessage();
        view.onPermissionsDenied();
    }

    private void onLocationError() {
        view.hideFetchingLocationMessage();
        view.onLocationError();
    }

    private void onChangeSettingsDenied() {
        view.hideFetchingLocationMessage();
        view.onChangeSettingsDenied();
    }

    public void onOperationFinished() {
        view.updateLocationView(userLocation.getName());
        view.hideFetchingLocationMessage();
    }
}

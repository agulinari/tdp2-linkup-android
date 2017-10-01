package com.tddp2.grupo2.linkup.utils;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tddp2.grupo2.linkup.R;
import com.tddp2.grupo2.linkup.model.Location;

import java.text.DecimalFormat;

public class DistanceUtils {

    public static final int MAP_PADDING = 0;

    public static LatLngBounds getLocationBounds(Location linkUpLocation1, Location linkUpLocation2) {
        double latitude1 = linkUpLocation1.getLatitude();
        double latitude2 = linkUpLocation2.getLatitude();
        double longitude1 = linkUpLocation1.getLongitude();
        double longitude2 = linkUpLocation2.getLongitude();

        double southLatitude = (latitude1 < latitude2) ? latitude1 : latitude2;
        double northLatitude = (latitude1 < latitude2) ? latitude2 : latitude1;
        double westLongitude = (longitude1 < longitude2) ? longitude1 : longitude2;
        double eastLongitude = (longitude1 < longitude2) ? longitude2 : longitude1;

        LatLng southwest = new LatLng(southLatitude, westLongitude);
        LatLng northeast = new LatLng(northLatitude, eastLongitude);
        return new LatLngBounds(southwest, northeast);
    }

    public static String getDistanceTextBetweenLocations(Location linkUpLocation1, Location linkUpLocation2, Context context) {
        float distanceInMeters = getDistanceBetweenLocationsInMeters(linkUpLocation1, linkUpLocation2);
        return getDistanceTextFromMeters(distanceInMeters, context);
    }

    public static float getDistanceBetweenLocationsInMeters(Location linkUpLocation1, Location linkUpLocation2) {
        android.location.Location loc1 = new android.location.Location("");
        loc1.setLatitude(linkUpLocation1.getLatitude());
        loc1.setLongitude(linkUpLocation1.getLongitude());

        android.location.Location loc2 = new android.location.Location("");
        loc2.setLatitude(linkUpLocation2.getLatitude());
        loc2.setLongitude(linkUpLocation2.getLongitude());

        float distanceInMeters = loc1.distanceTo(loc2);
        Log.i("DISTANCE", String.valueOf(distanceInMeters) + " meters");

        return distanceInMeters;
    }

    public static String getDistanceTextFromMeters(float distanceInMeters, Context context) {
        if (distanceInMeters <= 100) {
            return context.getString(R.string.link_distance_close);
        } else {
            float distanceInKilometers = distanceInMeters / 1000;
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(1);
            if (distanceInKilometers == 1.0) {
                return context.getString(R.string.link_distance_singular, decimalFormat.format(distanceInKilometers));
            } else {
                return context.getString(R.string.link_distance_plural, decimalFormat.format(distanceInKilometers));
            }
        }
    }
}

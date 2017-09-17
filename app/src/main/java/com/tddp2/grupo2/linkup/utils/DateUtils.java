package com.tddp2.grupo2.linkup.utils;

import android.util.Log;
import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
    private static final String FACEBOOK_FORMAT = "MM/dd/yyyy";
    private static final String LINK_UP_FORMAT = "yyyy/MM/dd";

    public static int getAgeFromBirthday(String birthday) throws MissingAgeException {
        if (birthday.equals("")) {
            throw new MissingAgeException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(LINK_UP_FORMAT);
            LocalDate birthdayDate = formatter.parseLocalDate(birthday);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthdayDate, now);
            return age.getYears();
        } catch (IllegalArgumentException e) {
            throw new MissingAgeException();
        }
    }

    public static String facebookToLinkupFormat(String facebookDate) {
        try {
            Log.i("BIRTHDAY", "Facebook format: " + facebookDate);
            DateTimeFormatter facebookFormatter = DateTimeFormat.forPattern(FACEBOOK_FORMAT);
            LocalDate date = facebookFormatter.parseLocalDate(facebookDate);
            DateTimeFormatter linkupFormatter = DateTimeFormat.forPattern(LINK_UP_FORMAT);
            String linkupDate = date.toString(linkupFormatter);
            Log.i("BIRTHDAY", "LinkUp format: " + linkupDate);
            return linkupDate;
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}

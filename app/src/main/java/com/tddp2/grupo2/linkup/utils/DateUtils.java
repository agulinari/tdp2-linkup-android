package com.tddp2.grupo2.linkup.utils;

import com.tddp2.grupo2.linkup.exception.MissingAgeException;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
    public static int getAgeFromBirthday(String birthday) throws MissingAgeException {
        if (birthday.equals("")) {
            throw new MissingAgeException();
        }
        try {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
            LocalDate birthdayDate = formatter.parseLocalDate(birthday);
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthdayDate, now);
            return age.getYears();
        } catch (IllegalArgumentException e) {
            throw new MissingAgeException();
        }
    }
}

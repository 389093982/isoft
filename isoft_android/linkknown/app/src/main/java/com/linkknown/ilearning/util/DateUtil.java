package com.linkknown.ilearning.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN2 = "yyyyMMdd";
    public static final String PATTERN3 = "yyyy年MM月dd HH:mm:ss";

    public static boolean isNowTimeBetween (String startTime, String endTime) {
        return isNowTimeBetween(startTime, endTime, PATTERN);
    }

    public static boolean isNowTimeBetween (String startTime, String endTime, String pattern) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);
            Date now = new Date();
            return start.before(now) && end.after(now);
        } catch (ParseException e) {
            Log.e("DateUtil", "DateUtil dateFormat.parse error!");
            return false;
        }
    }

    public static String formateDate (Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN);
        return dateFormat.format(date);
    }

    public static String formateYYYYMMDDToDate (String date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(PATTERN2);
            Date dateTime = dateFormat.parse(date);
            return formateDate(dateTime);
        } catch (ParseException e) {
            Log.e("DateUtil", "DateUtil dateFormat.parse error!");
            return date;
        }
    }

    public static String formateDate (Date date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}

package com.linkknown.ilearning.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN2 = "yyyyMMdd";
    public static final String PATTERN3 = "yyyy年MM月dd HH:mm:ss";
    public static final String PATTERN4 = "MMddHHmmss";

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

    /**
     * 输入14位时间戳 返回 标准时间 yyyyMMdd hh:mm:ss
     * 或者输入一个Date 做格式化
     * @return
     */
    public static String formatDate_StandardForm(String timeStamp) {
        String formatDate = "";
        if (timeStamp.length()==14){
            formatDate =  timeStamp.substring(0,4)+"-"+timeStamp.substring(4,6)+"-"+timeStamp.substring(6,8) + " "
                    + timeStamp.substring(8,10)+":"+timeStamp.substring(10,12)+":"+timeStamp.substring(12,14);
        }else if (timeStamp.indexOf("T")>0){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(timeStamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
        return formatDate;
    }


    /**
     * 用工具类获取今日日期
     * @return
     */
    public static String Today_yyyyMMdd(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(new Date());
        return format;
    }

    /**
     * 秒转分秒
     * 28800 ->  08:00
     * @param time
     * @return
     */
    public static String secToMinuteAndSec(int time) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer minute = time / 60;
        Integer sec = time % 60;
        return String.format("%2d:%2d", minute, sec);
    }

}

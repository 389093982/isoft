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
    public static final String PATTERN5 = "MM-dd HH:mm:ss";
    public static final String PATTERN6 = "yyyy-MM-dd'T'HH:mm:ss";

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

    public static Date getDate (String date) {
        String[] patterns = new String[]{PATTERN, PATTERN6};
        for (String pattern : patterns) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(pattern);
                Date dateTime = dateFormat.parse(date);
                return dateTime;
            }  catch (ParseException e) {}
        }
        return null;
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
     * 输入14位时间戳 返回 标准时间 yyyy-MM-dd hh:mm:ss
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
     * 用工具类获取今日时间戳
     * @return
     */
    public static String Today_yyyyMMddHHmmss(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(new Date());
        return format;
    }


    /**
     * 将date转为 yyyyMMdd
     * @param date
     * @return
     */
    public static String formateDate_2_yyyyMMdd (Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String dateTime = dateFormat.format(date);
            return dateTime;
        } catch (Exception e) {
            Log.e("DateUtil", "DateUtil dateFormat.parse error!");
            return "";
        }
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


    //格式化发布时间
    public static String formatPublishTime(String timeStamp) {
        String dateTime = formatDate_StandardForm(timeStamp);
        Date timeStampDate = null;
        try {
            timeStampDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //1.判断是不是今年
        if (dateTime.substring(0,4).equals(today.substring(0,4))){
            int days = (int)((new Date().getTime() - timeStampDate.getTime())/(3600*1000*24));
            //2.判断是不是3天以内
            if (days<=2){
                if (days==0){
                    return "今天"+dateTime.substring(dateTime.length()-8,dateTime.length());
                }else if (days==1){
                    return "昨天"+dateTime.substring(dateTime.length()-8,dateTime.length());
                }else if (days==2){
                    return "前天"+dateTime.substring(dateTime.length()-8,dateTime.length());
                }else{
                    return dateTime;
                }
            }else{
                return dateTime.substring(5,dateTime.length());
            }
        }else{
            //非今年
            return dateTime;
        }
    }



}

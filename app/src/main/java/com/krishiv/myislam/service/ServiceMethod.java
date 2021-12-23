package com.krishiv.myislam.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceMethod {
    public static boolean load = true;
    static SimpleDateFormat formatterTime = new SimpleDateFormat("KK:mm aa");
    static SimpleDateFormat formatterDate = new SimpleDateFormat("MMM dd,yyyy");

    public static boolean checkConn(Context ctx) {
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static String getOnlyDate(String strDate) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(strDate);
            return new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String cu_time(String strDate) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }


    public static String getMyDate(String strDate) {


        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
            return  new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "00-00-0000";
    }

    public static String getDate(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        return formatterDate.format(date);
    }

    public static String getDateDDMMYYYY(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(date);
    }

    public static String getTime(String strDate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        //Integer datetimestamp = Integer.parseInt(strDate.replaceAll("\\D", ""));
        //Date date = new Date(datetimestamp);
        formatterTime.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        return formatterTime.format(date);
    }

    //time
    public static String getTime1(String strDate) {
        try {
            Date dt = formatterTime.parse(strDate);
            formatterTime.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            return formatterTime.format(dt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateFormate(String strDate, String strFormate) {
        Long datetimestamp = Long.parseLong(strDate.replaceAll("\\D", ""));
        Timestamp stamp = new Timestamp(datetimestamp);
        Date date = new Date(stamp.getTime());
        return new SimpleDateFormat(strFormate, Locale.getDefault()).format(date);
    }
}

package com.krishiv.myislam.utils;

import android.net.ParseException;

import com.krishiv.myislam.dto.PrayerTimingModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimingUtils {

    public static final String Month = "MM";
    public static final String MonthNameShort = "MMM";
    public static final String DateOfMonth = "dd";
    public static final String Year = "yyyy";
    public static final String DayOfTheWeek  = "EEEE";
    public static final String PrayerTimeDateFormat = "dd MMM yyyy";  //03 Feb 2019
    public static final String DisplayDateSlashFormat = "dd/MM/yyyy";
    public static final String DailyReminderDateFormat = "yyyyMMdd";
    public static final String SQLiteDateFormat = "yyyy-MM-dd";
    public static final String HourMinutesTimeFormat = "HH:mm";
    public static final String HourMinutesAMPMTimeFormat = "hh:mm a";
    public static final String AlarmTimeDateFormat = SQLiteDateFormat + " " + HourMinutesTimeFormat;
    public static final String CalendarTitleFormat = "MMMM dd, yyyy";
    public static final String ServerTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";


    private static String getTimeBasedOnFormat(String timeFormat, Date date){
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        return timeStampFormat.format(date);
    }

    public static Date getStringToDateFormat(String timeFormat, String value){
        SimpleDateFormat format = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        try {
            return format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStringTimeBasedOnFormat(String currentFormat, String requiredFormat, String value){
        Date dateCurrentFormate = getStringToDateFormat(currentFormat, value);
        return getDateToString(requiredFormat, dateCurrentFormate);
    }

    public static String getDateToString(String timeFormat, Date value){
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        try {
            return dateFormat.format(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDayByAddSub(String timeFormat, int value){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, value);
        return getTimeBasedOnFormat(timeFormat, calendar.getTime());
    }

//    /*/**/*/

    public static String getPrevDay(String timeFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return getTimeBasedOnFormat(timeFormat, calendar.getTime());
    }

    public static String getToday(String timeFormat){
        return getTimeBasedOnFormat(timeFormat, Calendar.getInstance().getTime());
    }

    public static String getNextDay(String timeFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return getTimeBasedOnFormat(timeFormat, calendar.getTime());
    }

    public static Date getTimeStampOfTime(String time){
        return getStringToDateFormat(HourMinutesTimeFormat, time);
    }

    public static String getTimeFromTimeStamp(String timeStamp){
        return getDateToString(HourMinutesTimeFormat, new Date(Long.parseLong(timeStamp)));
    }

    public static String getApiPassDate(){
        return getToday("yyyy-MM-dd") + " 00:00:00.000";
    }
}

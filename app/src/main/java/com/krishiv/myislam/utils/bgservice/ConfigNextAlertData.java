package com.krishiv.myislam.utils.bgservice;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.Date;

public class ConfigNextAlertData {

    Shared pref;
    boolean isAlarmNextAlarmSet = false;

    public ConfigNextAlertData(Context context) {
        configUpcomingAlert(context);
    }

    private void configUpcomingAlert(Context context) {

        isAlarmNextAlarmSet = false;
        pref = new Shared(context);
        PrayerTimingDAO db = new PrayerTimingDAO(context);

        //switches data
        AppConfigModel appConfigModel = new Gson().fromJson(pref.getString(pref.appConfig,""), AppConfigModel.class);
        appConfigModel = appConfigModel==null? new AppConfigModel() : appConfigModel;

        //prayer time data
        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        if (prayerTimingModel == null)
            return;

        Log.e("Time","Fajr : " + prayerTimingModel.getFajr());
        Log.e("Time","Dhudr : " + prayerTimingModel.getDhuhr());
        Log.e("Time","Asar : " + prayerTimingModel.getAsr());
        Log.e("Time","Magrib : " + prayerTimingModel.getMaghrib());
        Log.e("Time","Isha : " + prayerTimingModel.getIsha());

        Resources res = context.getResources();
        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));

//        Fajr
        if (currenTimeStamp.getTime() < TimingUtils.getTimeStampOfTime(prayerTimingModel.getFajr()).getTime()){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getFajr())){
                if (appConfigModel.isIshaNotification()) {
                    PrayerTimingModel prayerTimingModel1 = db.getByDate(TimingUtils.getPrevDay(TimingUtils.SQLiteDateFormat));
                    setAlarmData(1, prayerTimingModel1.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getFajr())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.isha),
                                    res.getString(R.string.yesterday)), res.getString(R.string.isha), prayerTimingModel.getDate());
                    setAlarm();
                }else{
                    if (appConfigModel.isFajrAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr", res.getString(R.string.fajr), prayerTimingModel.getDate());
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isFajrAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr", res.getString(R.string.fajr), prayerTimingModel.getDate());
                    setAlarm();
                }
            }
        }

//        Dhudr
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < TimingUtils.getTimeStampOfTime(prayerTimingModel.getDhuhr()).getTime()){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getDhuhr())){
                if (appConfigModel.isFajrNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getDhuhr())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.fajr),
                                    res.getString(R.string.today)), res.getString(R.string.fajr), prayerTimingModel.getDate());
                    setAlarm();
                }else{
                    if (appConfigModel.isDhudrAlarm()){
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(),"Alarm for Dhudr", res.getString(R.string.dhudr), prayerTimingModel.getDate());
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isDhudrAlarm()){
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(),"Alarm for Dhudr", res.getString(R.string.dhudr), prayerTimingModel.getDate());
                    setAlarm();
                }
            }
        }

//        Asar
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getAsr())){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getAsr())){
                if (appConfigModel.isDhudrNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getAsr())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.dhudr),
                                    res.getString(R.string.today)), res.getString(R.string.dhudr), prayerTimingModel.getDate());
                    setAlarm();
                }else{
                    if (appConfigModel.isAsarAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asar", res.getString(R.string.asar), prayerTimingModel.getDate());
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isAsarAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asar", res.getString(R.string.asar), prayerTimingModel.getDate());
                    setAlarm();
                }
            }
        }

//        Magrib
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getMaghrib())){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getMaghrib())){
                if (appConfigModel.isAsarNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getMaghrib())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.asar),
                                    res.getString(R.string.today)), res.getString(R.string.asar), prayerTimingModel.getDate());
                    setAlarm();
                }else {
                    if (appConfigModel.isMagribAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Magrib", res.getString(R.string.magrib), prayerTimingModel.getDate());
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isMagribAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Magrib", res.getString(R.string.magrib),prayerTimingModel.getDate());
                    setAlarm();
                }
            }
        }

//        Isha
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getIsha())){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getIsha())){
                if (appConfigModel.isMagribNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getIsha())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.magrib),
                                    res.getString(R.string.today)), res.getString(R.string.magrib), prayerTimingModel.getDate());
                    setAlarm();
                }else{
                    if (appConfigModel.isIshaAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha", res.getString(R.string.isha), prayerTimingModel.getDate());
                        setAlarm();
                        //Application  crash on 169 AppConfigModel.isIshaAlarm() // null pointer
                    }
                }
            }else{
                if (appConfigModel.isIshaAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha", res.getString(R.string.isha), prayerTimingModel.getDate());
                    setAlarm();
                }
            }
        }

//        set alarm if all flags are enable for next day
        if(!isAlarmNextAlarmSet){
            //All for next day
            prayerTimingModel = db.getByDate(TimingUtils.getNextDay(TimingUtils.SQLiteDateFormat));

            if(appConfigModel.isIshaNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getFajr())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.isha),
                                res.getString(R.string.yesterday)), res.getString(R.string.isha), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isFajrAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr - Next Day", res.getString(R.string.fajr), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isFajrNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getDhuhr())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.fajr),
                                res.getString(R.string.today)), res.getString(R.string.fajr), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isDhudrAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), "Alarm for Dhudr - Next Day", res.getString(R.string.dhudr), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isDhudrNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getAsr())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.dhudr),
                                res.getString(R.string.today)), res.getString(R.string.dhudr), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isAsarAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asr - Next Day", res.getString(R.string.asar), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isAsarNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getMaghrib())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.asar),
                                res.getString(R.string.today)), res.getString(R.string.asar), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isMagribAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Maghrib - Next Day", res.getString(R.string.magrib), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isMagribNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getIsha())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.magrib),
                                res.getString(R.string.today)), res.getString(R.string.magrib), prayerTimingModel.getDate());
                setAlarm();
            }else if(appConfigModel.isIshaAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha - Next Day", res.getString(R.string.isha), prayerTimingModel.getDate());
                setAlarm();
            }else {
                pref.putString(pref.upcomingAlarmData, "");
                Log.e("Time"," : Alarm NOT Set -- (Disable)");
                Log.e("Schedule", "Save data for next alert is BLANK");
            }
        }
    }

    NotiHandler notiHandler;
    private void setAlarmData(int action, String date, String time, String msg, String prayerType, String inwokeTime){
        notiHandler = new NotiHandler();
        notiHandler.msg = msg;
        notiHandler.date = date;
        notiHandler.time = time;
        notiHandler.action = action;
        notiHandler.prayerType = prayerType;
        notiHandler.inwokeTime = inwokeTime + " " + time;
    }

    private long getTimeStampLess30(String time) {
        long ss = TimingUtils.getTimeStampOfTime(time).getTime();
        return ss - (1000*60*30);
    }

    private Long getTimeStamp(String tine){
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }

    private void setAlarm() {
        isAlarmNextAlarmSet = true;
        pref.putString(pref.upcomingAlarmData, new Gson().toJson(notiHandler));
        Log.e("Time", notiHandler.date+ "_" +notiHandler.time + "_" + notiHandler.msg);
        Log.e("Schedule", "Save data for next alert");
    }
}

package com.krishiv.myislam.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.krishiv.myislam.MainActivity;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.PopupPrayed;
import com.krishiv.myislam.activity.Welcome;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.dto.LoginModel;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTimingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    Shared pref;
    PrayerTimingDAO db;
    boolean isAlarmNextAlarmSet;
    NotiHandler notiHandler;
    PowerManager.WakeLock screenWakeLock;
    Context context;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        isAlarmNextAlarmSet = false;
        pref = new Shared(context);
        db = new PrayerTimingDAO(context);
        notiHandler = new NotiHandler();

        Shared shared = new Shared(context);
        if (shared.getString(Shared.userProfile,"").contentEquals("")){
            return;
        }

        if (screenWakeLock == null)
        {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "ScreenLock tag from AlarmListener");
            screenWakeLock.acquire();
        }

        performPreviousAction();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                configUpcomingAlert();
                screenWakeLock.release();
            }
        },1500);
    }

    private void configUpcomingAlert() {
        AppConfigModel appConfigModel = new Gson().fromJson(pref.getString(pref.appConfig,""), AppConfigModel.class);
        appConfigModel = appConfigModel==null? new AppConfigModel() : appConfigModel;

        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));

//        Log.e("Time","currenTimeStamp : "+ currenTimeStamp.getTime());
//        Log.e("Time","-30 : " + (currenTimeStamp.getTime() - (1000*60*30)));
//        Log.e("Time","previous Time : " + TimingUtils.getTimeFromTimeStamp((currenTimeStamp.getTime() - (1000*60*30))+""));

        if (prayerTimingModel == null)
            return;

        Log.e("Time","Fajr : " + prayerTimingModel.getFajr());
        Log.e("Time","Dhudr : " + prayerTimingModel.getDhuhr());
        Log.e("Time","Asar : " + prayerTimingModel.getAsr());
        Log.e("Time","Magrib : " + prayerTimingModel.getMaghrib());
        Log.e("Time","Isha : " + prayerTimingModel.getIsha());

        Resources res = context.getResources();

//        Fajr
        if (currenTimeStamp.getTime() < TimingUtils.getTimeStampOfTime(prayerTimingModel.getFajr()).getTime()){
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getFajr())){
                if (appConfigModel.isIshaNotification()) {
                    PrayerTimingModel prayerTimingModel1 = db.getByDate(TimingUtils.getPrevDay(TimingUtils.SQLiteDateFormat));
                    setAlarmData(1, prayerTimingModel1.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getFajr())+""),
                            String.format(res.getString(R.string.notification_msg), res.getString(R.string.isha),
                                    res.getString(R.string.yesterday)), res.getString(R.string.isha));
                    setAlarm();
                }else{
                    if (appConfigModel.isFajrAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr", res.getString(R.string.fajr));
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isFajrAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr", res.getString(R.string.fajr));
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
                                    res.getString(R.string.today)), res.getString(R.string.fajr));
                    setAlarm();
                }else{
                    if (appConfigModel.isDhudrAlarm()){
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(),"Alarm for Dhudr", res.getString(R.string.dhudr));
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isDhudrAlarm()){
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(),"Alarm for Dhudr", res.getString(R.string.dhudr));
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
                                    res.getString(R.string.today)), res.getString(R.string.dhudr));
                    setAlarm();
                }else{
                    if (appConfigModel.isAsarAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asar", res.getString(R.string.asar));
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isAsarAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asar", res.getString(R.string.asar));
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
                                    res.getString(R.string.today)), res.getString(R.string.asar));
                    setAlarm();
                }else {
                    if (appConfigModel.isMagribAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Magrib", res.getString(R.string.magrib));
                        setAlarm();
                    }
                }
            }else{
                if (appConfigModel.isMagribAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Magrib", res.getString(R.string.magrib));
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
                                    res.getString(R.string.today)), res.getString(R.string.magrib));
                    setAlarm();
                }else{
                    if (appConfigModel.isIshaAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha", res.getString(R.string.isha));
                        setAlarm();
                        //Application  crash on 169 AppConfigModel.isIshaAlarm() // null pointer
                    }
                }
            }else{
                if (appConfigModel.isIshaAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha", res.getString(R.string.isha));
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
                                res.getString(R.string.yesterday)), res.getString(R.string.isha));
                setAlarm();
            }else if(appConfigModel.isFajrAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Alarm for Fajr - Next Day", res.getString(R.string.fajr));
                setAlarm();
            }else if(appConfigModel.isFajrNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getDhuhr())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.fajr),
                                res.getString(R.string.today)), res.getString(R.string.fajr));
                setAlarm();
            }else if(appConfigModel.isDhudrAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), "Alarm for Dhudr - Next Day", res.getString(R.string.dhudr));
                setAlarm();
            }else if(appConfigModel.isDhudrNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getAsr())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.dhudr),
                                res.getString(R.string.today)), res.getString(R.string.dhudr));
                setAlarm();
            }else if(appConfigModel.isAsarAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Alarm for Asr - Next Day", res.getString(R.string.asar));
                setAlarm();
            }else if(appConfigModel.isAsarNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getMaghrib())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.asar),
                                res.getString(R.string.today)), res.getString(R.string.asar));
                setAlarm();
            }else if(appConfigModel.isMagribAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Alarm for Maghrib - Next Day", res.getString(R.string.magrib));
                setAlarm();
            }else if(appConfigModel.isMagribNotification()){
                setAlarmData(1, prayerTimingModel.getDate(), TimingUtils.getTimeFromTimeStamp(getTimeStampLess30(prayerTimingModel.getIsha())+""),
                        String.format(res.getString(R.string.notification_msg), res.getString(R.string.magrib),
                                res.getString(R.string.today)), res.getString(R.string.magrib));
                setAlarm();
            }else if(appConfigModel.isIshaAlarm()){
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Alarm for Isha - Next Day", res.getString(R.string.isha));
                setAlarm();
            }else {
                Log.e("Time"," : Alarm NOT Set -- (Disable)");
            }
        }
    }

    private void performPreviousAction() {
        try {
            NotiHandler previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);

            if (previouData == null || previouData.action == 0)
                return;

            if (!previouData.time.contentEquals(TimingUtils.getTimeFromTimeStamp(Calendar.getInstance().getTimeInMillis()+"")))
                return;

            if (previouData.action == 2){

                Intent resultIntent = new Intent(context, Welcome.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

                Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.azan);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String channelId = "my_channel_01";
                NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(context, channelId);

                Notification notification;
                notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("MyIshlam").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("MyIshlam")
                        .setContentIntent(pendingIntent)
                        .setSound(alarmSound)
//                        .setStyle(inboxStyle)
//                        .setWhen(getTimeMilliSec(timeStamp))
                        .setSmallIcon(R.drawable.ic_notification)
//                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(previouData.msg)
                        .build();


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(channelId, "dev", NotificationManager.IMPORTANCE_HIGH);
                    Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.azan);

                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();
                    mChannel.setSound(sound, attributes);
                    notificationManager.createNotificationChannel(mChannel);
                }

                notificationManager.notify(100, notification);
            }else {

                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                myKM.inKeyguardRestrictedInputMode();

                Intent myIntent = new Intent(context, PopupPrayed.class);
                myIntent.putExtra("data", pref.getString(pref.upcomingAlarmData,""));
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlarmData(int action, String date, String time, String msg, String prayerType){
        notiHandler.msg = msg;
        notiHandler.date = date;
        notiHandler.time = time;
        notiHandler.action = action;
        notiHandler.prayerType = prayerType;
    }

    private void setAlarm() {
        isAlarmNextAlarmSet = true;
        Log.e("Time", notiHandler.date + notiHandler.msg);

        /*Start Notification Setting*/
        Intent intentP = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentP, 0);

        Calendar calendar = Calendar.getInstance();
        long current = calendar.getTime().getTime();

        Calendar calendarNext = Calendar.getInstance();
        String mm = TimingUtils.getDateToString("mm", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime()));
        String HH = TimingUtils.getDateToString("HH", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime()));
        String dd = TimingUtils.getDateToString("dd", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime()));
        String MM = TimingUtils.getDateToString("MM", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime()));
        String yyyy = TimingUtils.getDateToString("yyyy", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime()));
        calendarNext.set(Calendar.MINUTE, Integer.parseInt(mm));
        calendarNext.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HH));
        calendarNext.set(Calendar.YEAR, Integer.parseInt(yyyy));
        calendarNext.set(Calendar.MONTH, Integer.parseInt(MM)-1);
        calendarNext.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
        long upcoming = calendarNext.getTimeInMillis();

//        long current = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, TimingUtils.getToday(TimingUtils.AlarmTimeDateFormat)).getTime();
//        long upcoming = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime();

        Log.e("Time",":Current:" + TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(current)));
        Log.e("Time",":Upcoming:"+ TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(upcoming)));

        long millis = upcoming - current;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        /*if (android.os.Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+millis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+millis, pendingIntent);
        }*/

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+millis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+millis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+millis, pendingIntent);
        }

        pref.putString(pref.upcomingAlarmData, new Gson().toJson(notiHandler));
    }

    private long getTimeStampLess30(String time) {

        Long ss = TimingUtils.getTimeStampOfTime(time).getTime();
        return ss - (1000*60*30);
    }

    private Long getTimeStamp(String tine){
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }
}
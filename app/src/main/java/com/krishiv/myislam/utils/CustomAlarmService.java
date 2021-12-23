package com.krishiv.myislam.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.Dashboard;
import com.krishiv.myislam.activity.PopupPrayed;
import com.krishiv.myislam.activity.Welcome;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTimingModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class CustomAlarmService extends Service {

    private int NOTIFICATION_ID = 1337;
    public static PrayerTimingDAO db;
    private Timer timer;
    private TimerTask timerTask;
    NotiHandler notiHandler;
    Shared pref;

    public static boolean isServiceRunning = false;
    Context context;
    boolean isAlarmNextAlarmSet;
    PowerManager.WakeLock screenWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundForOreoAndAbove();
        } else {
            startForegroundForBelowOreo();
        }
        pref = new Shared(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundForOreoAndAbove(){
        String NOTIFICATION_CHANNEL_ID = "com.taskhuman.insights";
        String channelName = "TaskHuman Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MyIslam service is running")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void startForegroundForBelowOreo(){
        Intent notificationIntent = new Intent(this, Dashboard.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MyIslam service is running")
                .setContentIntent(pendingIntent).build();

        startForeground(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notiHandler = new NotiHandler();
        db = new PrayerTimingDAO(this);
        startTimer();

        return START_STICKY;
    }

    public void startTimer() {
        if (timer != null){
            stopTimerTask();
        }
        timer = new Timer();
        initializeTimerTask();

        /*SimpleDateFormat format = new SimpleDateFormat("m", Locale.ENGLISH);
        try {
            Date currentDate = format.parse(new SimpleDateFormat("s", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            long delayW = 60*1000;
            delayW = 60000 - currentDate.getTime();
            timer.schedule(timerTask, 1000, delayW);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
            timer.schedule(timerTask, 1000, 60*1000);
        }*/

        timer.schedule(timerTask, 1000, 60*1000);
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                performPreviousAction();
            }
        };
    }

    @SuppressLint("InvalidWakeLockTag")
    private void performPreviousAction() {
        try {
            NotiHandler previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);

            /*previouData.setAction(1);
            previouData.setDate("2019-04-04");
            previouData.setTime("20:10");
            previouData.setMsg("Testing");
            previouData.setPrayerType("Fajr");*/

            if (previouData == null || previouData.action == 0) {
                configUpcomingAlert();
                return;
            }

            if (!previouData.time.contentEquals(TimingUtils.getTimeFromTimeStamp(Calendar.getInstance().getTimeInMillis()+""))){

                long current = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, TimingUtils.getToday(TimingUtils.AlarmTimeDateFormat)).getTime();
                long upcoming = TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, notiHandler.date + " " + notiHandler.time).getTime();

                Log.e("Time",":Current:" + TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(current)));
                Log.e("Time",":Upcoming:"+ TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(upcoming)));

                if (upcoming<current)
                    configUpcomingAlert();

                return;
            }

            if (screenWakeLock == null) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                screenWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "ScreenLock tag from AlarmListener");
            }

            if (screenWakeLock != null)
                screenWakeLock.acquire();

            if (previouData.action == 2){
                Intent resultIntent = new Intent(context, Welcome.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

                Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.azan);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                String channelId = "my_channel_01";
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId);

                 mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("MyIshlam").setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle("MyIshlam")
                        .setContentIntent(pendingIntent)
//                        .setStyle(inboxStyle)
//                        .setWhen(getTimeMilliSec(timeStamp))
                        .setSmallIcon(R.drawable.ic_notification)
//                        .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                        .setContentText(previouData.msg);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(channelId, "dev", NotificationManager.IMPORTANCE_HIGH);
                    Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.azan);

                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build();
                    mChannel.setSound(sound, attributes);
                    notificationManager.createNotificationChannel(mChannel);
                }else{
                    mBuilder.setSound(alarmSound);
                }

                notificationManager.notify(100, mBuilder.build());
            }else {
                boolean isopen = false;
                PrayerTimingModel prayerTimingModel = db.getByDate(previouData.date);
                if (previouData.prayerType.equalsIgnoreCase(getResources().getString(R.string.fajr))){
                    isopen = prayerTimingModel.isIsprayed_fajr();
                }
                else if (previouData.prayerType.equalsIgnoreCase(getResources().getString(R.string.dhudr))){
                    isopen = prayerTimingModel.isIsprayed_dhuhr();
                }
                else if (previouData.prayerType.equalsIgnoreCase(getResources().getString(R.string.asar))){
                    isopen = prayerTimingModel.isIsprayed_asr();
                }
                else if (previouData.prayerType.equalsIgnoreCase(getResources().getString(R.string.magrib))){
                    isopen = prayerTimingModel.isIsprayed_maghrib();
                }
                else if (previouData.prayerType.equalsIgnoreCase(getResources().getString(R.string.isha))){
                    isopen = prayerTimingModel.isIsprayed_isha();
                }

                if(!isopen){
                    KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                    myKM.inKeyguardRestrictedInputMode();

                    Intent myIntent = new Intent(context, PopupPrayed.class);
                    myIntent.putExtra("data", pref.getString(pref.upcomingAlarmData,""));
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
                }
            }

            if (screenWakeLock != null)
                screenWakeLock.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        configUpcomingAlert();
    }

    private void configUpcomingAlert() {
        AppConfigModel appConfigModel = new Gson().fromJson(pref.getString(pref.appConfig,""), AppConfigModel.class);
        appConfigModel = appConfigModel==null? new AppConfigModel() : appConfigModel;

        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));

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
                pref.putString(pref.upcomingAlarmData, "");
                Log.e("Time"," : Alarm NOT Set -- (Disable)");
            }
        }

        isAlarmNextAlarmSet = false;
    }

    private void setAlarm() {
        isAlarmNextAlarmSet = true;
        pref.putString(pref.upcomingAlarmData, new Gson().toJson(notiHandler));
        Log.e("Time", notiHandler.date+ "_" +notiHandler.time + "_" + notiHandler.msg);
    }

    private void setAlarmData(int action, String date, String time, String msg, String prayerType){
        notiHandler.msg = msg;
        notiHandler.date = date;
        notiHandler.time = time;
        notiHandler.action = action;
        notiHandler.prayerType = prayerType;
    }

    private long getTimeStampLess30(String time) {

        long ss = TimingUtils.getTimeStampOfTime(time).getTime();
        return ss - (1000*60*30);
    }

    private String getResourceString(int id){
        return getResources().getString(id);
    }

    private Long getTimeStamp(String tine){
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (!pref.getString(Shared.userProfile,"").isEmpty()){
            Intent serviceIntent = new Intent(this, CustomAlarmService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            }else {
                startService(serviceIntent);
            }
        }
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
    }
}

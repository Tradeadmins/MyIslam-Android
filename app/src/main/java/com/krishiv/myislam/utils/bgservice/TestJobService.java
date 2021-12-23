package com.krishiv.myislam.utils.bgservice;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.activity.PopupPrayed;
import com.krishiv.myislam.activity.Welcome;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.utils.Shared;

public class TestJobService extends JobService {
    private static final String TAG = "SyncService";

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public boolean onStartJob(JobParameters params) {
        PowerManager.WakeLock screenWakeLock = null;
        try {
            Context context = this;
            Shared pref = new Shared(context);

            if (pref.getString(Shared.userProfile, "").isEmpty()) {
                return true;
            }

            NotiHandler previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);

            if (previouData == null || previouData.action == 0){
                new ConfigNextAlertData(context);
                previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);
                if (previouData != null || previouData.action != 0){
                    Util.scheduleJob(context);
                }
                return true;
            }

            //DB check is checked
            /*if (new PrayerTimingDAO(context).isLastPopupOpened(previouData.getDate(), previouData.getPrayerType())){
                new ConfigNextAlertData(context);
                previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);
                if (previouData != null || previouData.action != 0){
                    Util.scheduleJob(context);
                }
                return true;
            }*/
            //DB check is checked

            /*if (!previouData.time.contentEquals(TimingUtils.getTimeFromTimeStamp(Calendar.getInstance().getTimeInMillis()+"")))
                return true;*/

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
                jobFinished(params, false);
            }else {

                KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                myKM.inKeyguardRestrictedInputMode();

                Intent myIntent = new Intent(context, PopupPrayed.class);
                myIntent.putExtra("data", pref.getString(pref.upcomingAlarmData,""));
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(myIntent);
                jobFinished(params, false);
            }

            if (screenWakeLock != null)
                screenWakeLock.release();

            Log.e("Schedule", "Requst for next alert data");
            new ConfigNextAlertData(context);
            Util.scheduleJob(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}

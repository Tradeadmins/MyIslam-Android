package com.krishiv.myislam.utils.bgservice;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.RequiresApi;

public class Util {

    // schedule the start of the service every 10 - 30 seconds
    @TargetApi(Build.VERSION_CODES.M)
    public static void scheduleJob1(Context context) {
        try {
            Log.e("Schedule", "setNew Schedule");

            Shared pref = new Shared(context);
            NotiHandler previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);

            Log.e("Schedule", "For___"+previouData.date+ "_" +previouData.time + "_" + previouData.msg);

            /**/

            Calendar calendar = Calendar.getInstance();
            long current = calendar.getTime().getTime();

            Calendar calendarNext = Calendar.getInstance();
            String mm = TimingUtils.getDateToString("mm", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.inwokeTime).getTime()));
            String HH = TimingUtils.getDateToString("HH", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.inwokeTime).getTime()));
            String dd = TimingUtils.getDateToString("dd", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.inwokeTime).getTime()));
            String MM = TimingUtils.getDateToString("MM", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.inwokeTime).getTime()));
            String yyyy = TimingUtils.getDateToString("yyyy", new Date(TimingUtils.getStringToDateFormat(TimingUtils.AlarmTimeDateFormat, previouData.inwokeTime).getTime()));
            calendarNext.set(Calendar.MINUTE, Integer.parseInt(mm));
            calendarNext.set(Calendar.HOUR_OF_DAY, Integer.parseInt(HH));
            calendarNext.set(Calendar.YEAR, Integer.parseInt(yyyy));
            calendarNext.set(Calendar.MONTH, Integer.parseInt(MM)-1);
            calendarNext.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
            long upcoming = calendarNext.getTimeInMillis();

            Log.e("Time",":Current:" + TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(current)));
            Log.e("Time",":Upcoming:"+ TimingUtils.getDateToString(TimingUtils.AlarmTimeDateFormat, new Date(upcoming)));

            long wakeUpDelay = upcoming - current;

            /**/
            Log.e("Schedule", "Delay___"+(wakeUpDelay));
            Log.e("Schedule", "Delay60___"+(wakeUpDelay/60));
            Log.e("Schedule", "DelayTime___"+new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(wakeUpDelay));

            JobScheduler oldJobs = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            oldJobs.cancelAll();

            ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(wakeUpDelay - 5 * 1000); // wait at least
            builder.setOverrideDeadline(wakeUpDelay); // maximum delay
           // builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE); // require unmetered network
          //  builder.setRequiresDeviceIdle(false); // device should be idle
           // builder.setRequiresCharging(false); // we don't care if the device is charging or not
            builder.setPersisted(true);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
            //Toast.makeText(context, "Alarmed for "+previouData.msg, Toast.LENGTH_LONG).show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        try {
            Log.e("Schedule", "setNew Schedule");

            Shared pref = new Shared(context);
            NotiHandler previouData = new Gson().fromJson(pref.getString(pref.upcomingAlarmData,""), NotiHandler.class);

            Log.e("Schedule", "For___"+previouData.date+ "_" +previouData.time + "_" + previouData.msg);

            String timeFormat = "yyyy-MM-dd HH:mm";
            SimpleDateFormat timeStampFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
            Date wakeTime = timeStampFormat.parse(previouData.getInwokeTime());

            long wakeMillis = wakeTime.getTime();
            long currentMillis = System.currentTimeMillis();
            long wakeUpDelay = wakeMillis - currentMillis;

            Log.e("Schedule", "Delay___"+(wakeUpDelay/60));
            Log.e("Schedule", "DelayTime___"+new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(wakeUpDelay));

            JobScheduler oldJobs = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            oldJobs.cancelAll();

            ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
            JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
            builder.setMinimumLatency(wakeUpDelay - 5 * 1000); // wait at least
            builder.setOverrideDeadline(wakeUpDelay); // maximum delay
            // builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE); // require unmetered network
            //  builder.setRequiresDeviceIdle(false); // device should be idle
            // builder.setRequiresCharging(false); // we don't care if the device is charging or not
            builder.setPersisted(true);
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
            //Toast.makeText(context, "Alarmed for "+previouData.msg, Toast.LENGTH_LONG).show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

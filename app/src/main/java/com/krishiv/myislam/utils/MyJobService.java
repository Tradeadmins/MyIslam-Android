package com.krishiv.myislam.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class MyJobService extends JobIntentService {

    public static final int JOB_ID = 0x01;
    private static Context context;

    public static void enqueueWork(Context context, Intent work) {
        MyJobService.context = context;
        enqueueWork(context, MyJobService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (!new Shared(context).getString(Shared.userProfile, "").isEmpty()) {
            Intent serviceIntent = new Intent(context, CustomAlarmService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
        }
    }

}
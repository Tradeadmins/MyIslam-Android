package com.krishiv.myislam.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootCompleted","BootCompleted");

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            MyJobService.enqueueWork(context, new Intent());
        }
    }
}

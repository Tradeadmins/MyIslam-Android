package com.krishiv.myislam.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.AlarmReceiver;
import com.krishiv.myislam.utils.bgservice.ConfigNextAlertData;
import com.krishiv.myislam.utils.bgservice.Util;

public class PrayerTimeNotification extends BaseMenuActivity{

    AppConfigModel appConfigModel;
    SwitchCompat tik_fajr_alarm, tik_fajr_track, tik_dhudr_alarm, tik_dhudr_track, tik_asar_alarm,
            tik_asar_track, tik_magrib_alarm, tik_magrib_track, tik_isha_alarm, tik_isha_track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_prayer_time_notification);

        appConfigModel = new Gson().fromJson(shared.getString(shared.appConfig,""), AppConfigModel.class);
        appConfigModel = appConfigModel==null? new AppConfigModel() : appConfigModel;

        tik_fajr_alarm = findViewById(R.id.tik_fajr_alarm);
        tik_dhudr_alarm = findViewById(R.id.tik_dhudr_alarm);
        tik_asar_alarm = findViewById(R.id.tik_asar_alarm);
        tik_magrib_alarm = findViewById(R.id.tik_magrib_alarm);
        tik_isha_alarm = findViewById(R.id.tik_isha_alarm);

        tik_fajr_track = findViewById(R.id.tik_fajr_track);
        tik_dhudr_track = findViewById(R.id.tik_dhudr_track);
        tik_asar_track = findViewById(R.id.tik_asar_track);
        tik_magrib_track = findViewById(R.id.tik_magrib_track);
        tik_isha_track = findViewById(R.id.tik_isha_track);

        tik_fajr_alarm.setChecked(appConfigModel.isFajrAlarm());
        tik_dhudr_alarm.setChecked(appConfigModel.isDhudrAlarm());
        tik_asar_alarm.setChecked(appConfigModel.isAsarAlarm());
        tik_magrib_alarm.setChecked(appConfigModel.isMagribAlarm());
        tik_isha_alarm.setChecked(appConfigModel.isIshaAlarm());
        tik_fajr_track.setChecked(appConfigModel.isFajrNotification());
        tik_dhudr_track.setChecked(appConfigModel.isDhudrNotification());
        tik_asar_track.setChecked(appConfigModel.isAsarNotification());
        tik_magrib_track.setChecked(appConfigModel.isMagribNotification());
        tik_isha_track.setChecked(appConfigModel.isIshaNotification());


        findViewById(R.id.arrow_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setAlarmConfig();
        super.onBackPressed();
    }

    private void setAlarmConfig() {
        appConfigModel.setFajrAlarm(tik_fajr_alarm.isChecked());
        appConfigModel.setDhudrAlarm(tik_dhudr_alarm.isChecked());
        appConfigModel.setAsarAlarm(tik_asar_alarm.isChecked());
        appConfigModel.setMagribAlarm(tik_magrib_alarm.isChecked());
        appConfigModel.setIshaAlarm(tik_isha_alarm.isChecked());

        appConfigModel.setFajrNotification(tik_fajr_track.isChecked());
        appConfigModel.setDhudrNotification(tik_dhudr_track.isChecked());
        appConfigModel.setAsarNotification(tik_asar_track.isChecked());
        appConfigModel.setMagribNotification(tik_magrib_track.isChecked());
        appConfigModel.setIshaNotification(tik_isha_track.isChecked());

        shared.putString(shared.appConfig, new Gson().toJson(appConfigModel));

        shared.putString(shared.upcomingAlarmData,"");
        /*Intent intent = new Intent(context, AlarmReceiver.class);
        sendBroadcast(intent);*/
        Log.e("Schedule", "Requst for next alert data");
        new ConfigNextAlertData(context);
        Util.scheduleJob(context);
    }
}

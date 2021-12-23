package com.krishiv.myislam.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.AppConfigModel;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.utils.GlobalVariable;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.Date;

public class ActivityAlarm extends AppCompatActivity {

    EditText editText;
    Context context;
    Shared pref;
    PrayerTimingDAO db;
    boolean isAlarmNextAlarmSet;
    NotiHandler notiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_config1);
        context = this;

        editText = findViewById(R.id.edt_time);
        editText.setText("5:00");

        context = this;

        findViewById(R.id.arrow_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.getRegistrationModel().setFajrNotification(((CheckBox) findViewById(R.id.tik_fajr_track)).isChecked());
                GlobalVariable.getRegistrationModel().setFajrAlarm(((CheckBox) findViewById(R.id.tik_fajr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setDhudrNotification(((CheckBox) findViewById(R.id.tik_dhudr_track)).isChecked());
                GlobalVariable.getRegistrationModel().setDhudrAlarm(((CheckBox) findViewById(R.id.tik_dhudr_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setAsarNotification(((CheckBox) findViewById(R.id.tik_asar_track)).isChecked());
                GlobalVariable.getRegistrationModel().setAsarAlarm(((CheckBox) findViewById(R.id.tik_asar_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setMagribNotification(((CheckBox) findViewById(R.id.tik_magrib_track)).isChecked());
                GlobalVariable.getRegistrationModel().setMagribAlarm(((CheckBox) findViewById(R.id.tik_magrib_alarm)).isChecked());
                GlobalVariable.getRegistrationModel().setIshaNotification(((CheckBox) findViewById(R.id.tik_isha_track)).isChecked());
                GlobalVariable.getRegistrationModel().setIshaAlarm(((CheckBox) findViewById(R.id.tik_isha_alarm)).isChecked());
//                GlobalVariable.getRegistrationModel().setLanguageCode(strLanguage[spnLanguage.getSelectedItemPosition()]);
                //GlobalVariable.getRegistrationModel().setSubscriptionType(strLanguage[spnLanguage.getSelectedItemPosition()]);

                AppConfigModel appConfigModel = new AppConfigModel();
                appConfigModel.setFajrAlarm(GlobalVariable.getRegistrationModel().isFajrAlarm());
                appConfigModel.setDhudrAlarm(GlobalVariable.getRegistrationModel().isDhudrAlarm());
                appConfigModel.setAsarAlarm(GlobalVariable.getRegistrationModel().isAsarAlarm());
                appConfigModel.setMagribAlarm(GlobalVariable.getRegistrationModel().isMagribAlarm());
                appConfigModel.setIshaAlarm(GlobalVariable.getRegistrationModel().isIshaAlarm());

                appConfigModel.setFajrNotification(GlobalVariable.getRegistrationModel().isFajrNotification());
                appConfigModel.setDhudrNotification(GlobalVariable.getRegistrationModel().isDhudrNotification());
                appConfigModel.setAsarNotification(GlobalVariable.getRegistrationModel().isAsarNotification());
                appConfigModel.setMagribNotification(GlobalVariable.getRegistrationModel().isMagribNotification());
                appConfigModel.setIshaNotification(GlobalVariable.getRegistrationModel().isIshaNotification());

                pref = new Shared(context);
                pref.putString(pref.appConfig, new Gson().toJson(appConfigModel));

                TestAlarm();
            }
        });
    }

    private void TestAlarm() {
        isAlarmNextAlarmSet = false;
        pref = new Shared(context);
        db = new PrayerTimingDAO(context);
        notiHandler = new NotiHandler();

        AppConfigModel appConfigModel = new Gson().fromJson(pref.getString(pref.appConfig, ""), AppConfigModel.class);
        PrayerTimingModel prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

//        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));
        //fake timing
        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(editText.getText().toString());

//        Log.e("Time","currenTimeStamp : "+ currenTimeStamp.getTime());
//        Log.e("Time","-30 : " + (currenTimeStamp.getTime() - (1000*60*30)));
//        Log.e("Time","previous Time : " + TimingUtils.getTimeFromTimeStamp((currenTimeStamp.getTime() - (1000*60*30))+""));

        Log.e("Time", "Fajr : " + prayerTimingModel.getFajr());
        Log.e("Time", "Dhudr : " + prayerTimingModel.getDhuhr());
        Log.e("Time", "Asar : " + prayerTimingModel.getAsr());
        Log.e("Time", "Magrib : " + prayerTimingModel.getMaghrib());
        Log.e("Time", "Isha : " + prayerTimingModel.getIsha());

//        Fajr
        if (currenTimeStamp.getTime() < TimingUtils.getTimeStampOfTime(prayerTimingModel.getFajr()).getTime()) {
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getFajr())) {
                if (appConfigModel.isIshaNotification()) {
                    PrayerTimingModel prayerTimingModel1 = db.getByDate(TimingUtils.getPrevDay(TimingUtils.SQLiteDateFormat));
                    setAlarmData(1, prayerTimingModel1.getDate(), prayerTimingModel1.getIsha(), " : Notification 4 Isha Prayed - Previous Day");
                    setAlarm();
                } else {
                    if (appConfigModel.isFajrAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), " : Alarm 4 Fajr");
                        setAlarm();
                    }
                }
            } else {
                if (appConfigModel.isFajrAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), " : Alarm 4 Fajr");
                    setAlarm();
                }
            }
        }

//        Dhudr
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < TimingUtils.getTimeStampOfTime(prayerTimingModel.getDhuhr()).getTime()) {
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getDhuhr())) {
                if (appConfigModel.isFajrNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), " : Notification 4 Fajr Prayed");
                    setAlarm();
                } else {
                    if (appConfigModel.isDhudrAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), " : Alarm 4 Dhudr");
                        setAlarm();
                    }
                }
            } else {
                if (appConfigModel.isDhudrAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), " : Alarm 4 Dhudr");
                    setAlarm();
                }
            }
        }

//        Asar
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getAsr())) {
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getAsr())) {
                if (appConfigModel.isDhudrNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), " : Notification 4 Dhudr Prayed");
                    setAlarm();
                } else {
                    if (appConfigModel.isAsarAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), " : Alarm 4 Asar");
                        setAlarm();
                    }
                }
            } else {
                if (appConfigModel.isAsarAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), " : Alarm 4 Asar");
                    setAlarm();
                }
            }
        }

//        Magrib
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getMaghrib())) {
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getMaghrib())) {
                if (appConfigModel.isAsarNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), " : Notification 4 Asar Prayed");
                    setAlarm();
                } else {
                    if (appConfigModel.isMagribAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), " : Alarm 4 Magrib");
                        setAlarm();
                    }
                }
            } else {
                if (appConfigModel.isMagribAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), " : Alarm 4 Magrib");
                    setAlarm();
                }
            }
        }

//        Isha
        if (!isAlarmNextAlarmSet && currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getIsha())) {
            if (currenTimeStamp.getTime() < getTimeStampLess30(prayerTimingModel.getIsha())) {
                if (appConfigModel.isMagribNotification()) {
                    setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), " : Notification 4 Magrib Prayed");
                    setAlarm();
                } else {
                    if (appConfigModel.isIshaAlarm()) {
                        setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), " : Alarm 4 Isha");
                        setAlarm();
                    }
                }
            } else {
                if (appConfigModel.isIshaAlarm()) {
                    setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), " : Alarm 4 Isha");
                    setAlarm();
                }
            }
        }

//        set alarm if all flags are enable for next day
        if (!isAlarmNextAlarmSet) {

            prayerTimingModel = db.getByDate(TimingUtils.getNextDay(TimingUtils.SQLiteDateFormat));

            if (appConfigModel.isIshaNotification()) {
                setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), "Notification 4 Isha Prayed - Next Day");
                setAlarm();
            } else if (appConfigModel.isFajrAlarm()) {
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), " : Alarm 4 Fajr - Next Day");
                setAlarm();
            } else if (appConfigModel.isFajrNotification()) {
                setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getFajr(), "Notification 4 Fajr Prayed - Next Day");
                setAlarm();
            } else if (appConfigModel.isDhudrAlarm()) {
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), " : Alarm 4 Dhudr - Next Day");
                setAlarm();
            } else if (appConfigModel.isDhudrNotification()) {
                setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getDhuhr(), "Notification 4 Dhudr Prayed - Next Day");
                setAlarm();
            } else if (appConfigModel.isAsarAlarm()) {
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), " : Alarm 4 Asr - Next Day");
                setAlarm();
            } else if (appConfigModel.isAsarNotification()) {
                setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getAsr(), "Notification 4 Asr Prayed - Next Day");
                setAlarm();
            } else if (appConfigModel.isMagribAlarm()) {
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), " : Alarm 4 Maghrib - Next Day");
                setAlarm();
            } else if (appConfigModel.isMagribNotification()) {
                setAlarmData(1, prayerTimingModel.getDate(), prayerTimingModel.getMaghrib(), "Notification 4 Maghrib Prayed - Next Day");
                setAlarm();
            } else if (appConfigModel.isIshaAlarm()) {
                setAlarmData(2, prayerTimingModel.getDate(), prayerTimingModel.getIsha(), " : Alarm 4 Isha - Next Day");
                setAlarm();
            } else {
                setAlarmData(1, "", "", " : Alarm NOT Set -- (Disable)");
                setAlarm();
            }
        }
    }

    private void setAlarmData(int action, String date, String time, String msg) {
        notiHandler.msg = msg;
        notiHandler.date = date;
        notiHandler.time = time;
        notiHandler.action = action;
    }

    private void setAlarm() {
        isAlarmNextAlarmSet = true;
        Log.e("Time", notiHandler.date + notiHandler.msg);
    }

    private long getTimeStampLess30(String time) {

        Long ss = TimingUtils.getTimeStampOfTime(time).getTime();
        return ss - (1000 * 60 * 30);
    }

    private Long getTimeStamp(String tine) {
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }

    class NotiHandler {
        String time, date, msg;
        int action; //1-Notif -- 2-Alarm
    }
}

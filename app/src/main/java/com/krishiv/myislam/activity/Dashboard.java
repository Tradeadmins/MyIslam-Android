package com.krishiv.myislam.activity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.ResultComman;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.API.Api;
import com.krishiv.myislam.utils.GlobalTask;
import com.krishiv.myislam.utils.LocaleHelper;
import com.krishiv.myislam.utils.Shared;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends BaseMenuActivity {
    String comeFrom;

    protected void attachBaseContext(Context newBase) {
        String currentLanguage = LocaleHelper.getLanguage(newBase);
        if(currentLanguage==null)
        {
            currentLanguage = "en";
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
        else
        {
            super.attachBaseContext(LocaleHelper.setLocale(newBase,currentLanguage));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = setContentViev(R.layout.activity_dashboard);

        Intent extras = getIntent();
        if (extras != null) {
            comeFrom = extras.getStringExtra("comeFrom");

        }

        findViewById(R.id.btn_community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Community.class));
            }
        });

        findViewById(R.id.btn_faith).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Faith.class));
            }
        });

        findViewById(R.id.btn_prayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Prayer.class));
            }
        });

        findViewById(R.id.btn_pilgrimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Pilgrim.class));
            }
        });
        findViewById(R.id.btn_fasting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FastingActivity.class));
            }
        });
        findViewById(R.id.btn_charity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CharityActivity.class));
            }
        });
        bottom_line.setVisibility(View.GONE);
        isItPopupTime();

        /*Calendar calendar = Calendar.getInstance();
        if(!db.isAvailable(TimingUtils.getDateToString(TimingUtils.PrayerTimeDateFormat, calendar.getTime())))
            callApiForPrayerTimes(TimingUtils.getDateToString(TimingUtils.Month, calendar.getTime()), TimingUtils.getDateToString(TimingUtils.Year, calendar.getTime()),null);*/


        //openTstNotification();
    }

    private void openTstNotification() {
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
                .setContentText("Testing Notification")
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
    }

    private void isItPopupTime() {

        if (shared.getLong(Shared.lastDailyQuote, 0L) < Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat))) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6) {
                callApiJumaQuote();
            }
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                try {
                    List<String> dataCount = db.getTrackerWMYData();
                    openPopup(String.format(getResources().getString(R.string.popup_total_prayed), dataCount.get(0)));
                }catch (Exception e){
                    Log.e("Exception", "Popup");
                }
            }
            callApiDailyQuote();
        }
    }

    private void openPopup(String txt) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.popup_jummah);

        ((TextView) dialog.findViewById(R.id.txt_desc)).setText(txt);

        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callApiJumaQuote() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();
        Api.getClient().GetJumaQuoteByLang_Date("2", TimingUtils.getApiPassDate(), "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                shared.putLong(Shared.lastDailyQuote, Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat)));
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        if (jsonObject.has("jumaQuoteText")) {
                            openPopup(jsonObject.get("jumaQuoteText").toString());
                        }
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void callApiDailyQuote() {
        final ProgressDialog progressDialog = GlobalTask.getProgressDialog(context);
        progressDialog.show();

        Api.getClient().GetDailyQuoteByLang_Date("2", TimingUtils.getApiPassDate(), "Bearer " + userProfile.getAccess_token()).enqueue(new Callback<ResultComman>() {
            @Override
            public void onResponse(Call<ResultComman> call, Response<ResultComman> response) {
                ResultComman resultObj = response.body();
                shared.putLong(Shared.lastDailyQuote, Long.parseLong(TimingUtils.getToday(TimingUtils.DailyReminderDateFormat)));
                if (resultObj != null) {
                    if (resultObj.getContent() instanceof String) {
                        //GlobalTask.showToastMessage(context, resultObj.getContent().toString());
                    } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                        JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                        if (jsonObject.has("dailyQuoteText")) {
                            openPopup(jsonObject.get("dailyQuoteText").toString());
                        } else{

                        }
//                            GlobalTask.showToastMessage(context, "Daily quote text not available");
                    }
                } else {
                    //GlobalTask.showTostErrorResponse(context, response.errorBody());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResultComman> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}

package com.krishiv.myislam.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.db.PrayerTimingDAO;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTimingModel;

public class PopupPrayed extends AppCompatActivity{

    Context context;
    PrayerTimingDAO db;
    Resources res;
    NotiHandler previouData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_black);
        
        context = this;
        res = getResources();

        try {
            previouData = new Gson().fromJson(getIntent().getStringExtra("data"), NotiHandler.class);
            openPopup();
        }catch (Exception e){
            Log.e("Exception_Rad","PopupPrayed");
        }
    }

    private void openPopup() {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.popup_prayed);
        dialog.setCancelable(false);

        ((TextView)dialog.findViewById(R.id.txt)).setText(previouData.msg);

        dialog.findViewById(R.id.btn_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new PrayerTimingDAO(context);
                PrayerTimingModel prayerTimingModel = db.getByDate(previouData.date);
                if (previouData.prayerType.equalsIgnoreCase(res.getString(R.string.fajr))){
                    prayerTimingModel.setIsprayed_fajr(true);
                }
                else if (previouData.prayerType.equalsIgnoreCase(res.getString(R.string.dhudr))){
                    prayerTimingModel.setIsprayed_dhuhr(true);
                }
                else if (previouData.prayerType.equalsIgnoreCase(res.getString(R.string.asar))){
                    prayerTimingModel.setIsprayed_asr(true);
                }
                else if (previouData.prayerType.equalsIgnoreCase(res.getString(R.string.magrib))){
                    prayerTimingModel.setIsprayed_maghrib(true);
                }
                else if (previouData.prayerType.equalsIgnoreCase(res.getString(R.string.isha))){
                    prayerTimingModel.setIsprayed_isha(true);
                }
                db.save(prayerTimingModel);
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }


    /*if (previouData.msg.contains("fajr")){
                    prayerTimingModel.setIsprayed_fajr(true);
                }
                else if (previouData.msg.contains("dhuhr")){
                    prayerTimingModel.setIsprayed_dhuhr(true);
                }
                else if (previouData.msg.contains("asr")){
                    prayerTimingModel.setIsprayed_asr(true);
                }
                else if (previouData.msg.contains("maghrib")){
                    prayerTimingModel.setIsprayed_maghrib(true);
                }
                else if (previouData.msg.contains("isha")){
                    prayerTimingModel.setIsprayed_isha(true);
                }*/
}

package com.krishiv.myislam.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.NotiHandler;
import com.krishiv.myislam.dto.PrayerTableModal;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.AlarmReceiver;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Prayer extends BaseMenuActivity implements View.OnClickListener{

    RelativeLayout rlt_day_image;
    LinearLayout lnr_slot, lnr_prayed, lnr_tracker,lnr_prayer_table;
    TextView txt_slot_prayer, txt_is_prayed;
    List<List<PrayerTableModal>> prayerTableData;
    PrayerTimingModel prayerTimingModel;
    String nextPrayer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.prayer_activity);

        rlt_day_image = findViewById(R.id.rlt_day_image);
        lnr_slot = findViewById(R.id.lnr_slot);
        txt_slot_prayer = findViewById(R.id.txt_slot_prayer);
        txt_is_prayed = findViewById(R.id.txt_is_prayed);
        lnr_prayed = findViewById(R.id.lnr_prayed);
        lnr_tracker = findViewById(R.id.lnr_tracker);
        lnr_prayer_table = findViewById(R.id.lnr_prayer_table);

        setDefaultValueOfTable();
        setDataForPage();

        ((CheckBox) findViewById(R.id.tik_prayed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(!((CheckBox) findViewById(R.id.tik_prayed)).isChecked());
                lnr_prayed.performClick();
            }
        });
    }

    private void setDefaultValueOfTable() {
        prayerTableData = new ArrayList<>();

        List<PrayerTableModal> dataObj = new ArrayList<>();
        dataObj.add(new PrayerTableModal("Prayer",getResources().getString(R.string.fajr), getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Fard","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","0", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Total Rakkats","4", getResources().getColor(R.color.colorWhite)));
        prayerTableData.add(dataObj);

        dataObj = new ArrayList<>();
        dataObj.add(new PrayerTableModal("Prayer",getResources().getString(R.string.dhudr), getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Fard","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Nafl","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Total Rakkats","12", getResources().getColor(R.color.colorWhite)));
        prayerTableData.add(dataObj);

        dataObj = new ArrayList<>();
        dataObj.add(new PrayerTableModal("Prayer",getResources().getString(R.string.asar), getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Fard","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Total Rakkats","8", getResources().getColor(R.color.colorWhite)));
        prayerTableData.add(dataObj);

        dataObj = new ArrayList<>();
        dataObj.add(new PrayerTableModal("Prayer",getResources().getString(R.string.magrib), getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Fard","3", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Nafl","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Total Rakkats","7", getResources().getColor(R.color.colorWhite)));
        prayerTableData.add(dataObj);

        dataObj = new ArrayList<>();
        dataObj.add(new PrayerTableModal("Prayer",getResources().getString(R.string.isha), getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Fard","4", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Sunnah","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Nafl","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Witr","3", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Nafl","2", getResources().getColor(R.color.colorWhite)));
        dataObj.add(new PrayerTableModal("Total Rakkats","17", getResources().getColor(R.color.colorWhite)));
        prayerTableData.add(dataObj);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lnr_prayed:

               /* if (((CheckBox) findViewById(R.id.tik_prayed)).isChecked())
                    return;*/


                boolean isChecked = !((CheckBox) findViewById(R.id.tik_prayed)).isChecked();

                if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.fajr))) {
                    prayerTimingModel.setIsprayed_fajr(isChecked);
                } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.dhudr))) {
                    prayerTimingModel.setIsprayed_dhuhr(isChecked);
                } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.asar))) {
                    prayerTimingModel.setIsprayed_asr(isChecked);
                } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.magrib))) {
                    prayerTimingModel.setIsprayed_maghrib(isChecked);
                } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.isha))) {
                    prayerTimingModel.setIsprayed_isha(isChecked);
                }

                db.save(prayerTimingModel);
                ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(isChecked);

                break;

            case R.id.lnr_tracker:
                startActivity(new Intent(context, PrayerTracker.class));
                break;

            case R.id.txt_qibla_locator:
                startActivity(new Intent(context, Qibla.class));
                break;

            case R.id.txt_how_to_pray:
                startActivity(new Intent(context,Instruction.class));
                break;

            case R.id.txt_duas_and_misbaha:
                startActivity(new Intent(context,ActivityDuas.class));
                break;

            case R.id.txt_hadiths:
                startActivity(new Intent(context, Hadiths.class));
                break;

            case R.id.txt_mosques:
                startActivity(new Intent(context, Mosque.class).putExtra("activity","mosque"));
                break;

            case R.id.txt_the_quran:
                startActivity(new Intent(context, ActivityThequran.class));
                break;

            case R.id.arrow_back:
                finish();
                break;
        }
    }

    private void setDataForPage() {
        prayerTimingModel = db.getByDate(TimingUtils.getToday(TimingUtils.SQLiteDateFormat));

        if (prayerTimingModel == null)
            return;

        final int count;

        Date currenTimeStamp = TimingUtils.getTimeStampOfTime(TimingUtils.getToday(TimingUtils.HourMinutesTimeFormat));
        if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getFajr())){
            prayerTimingModel = db.getByDate(TimingUtils.getPrevDay(TimingUtils.SQLiteDateFormat));
            nextPrayer = getResources().getString(R.string.isha);
            count = 4;
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getDhuhr())){
            nextPrayer = getResources().getString(R.string.fajr);
            count = 0;
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getAsr())){
            nextPrayer = getResources().getString(R.string.dhudr);
            count = 1;
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getMaghrib())){
            nextPrayer = getResources().getString(R.string.asar);
            count = 2;
        }else if (currenTimeStamp.getTime() < getTimeStamp(prayerTimingModel.getIsha())){
            nextPrayer = getResources().getString(R.string.magrib);
            count = 3;
        }else{
            nextPrayer = getResources().getString(R.string.isha);
            count = 4;
        }/*else{
            prayerTimingModel = db.getByDate(TimingUtils.getNextDay(TimingUtils.PrayerTimeDateFormat));
            nextPrayer = "Isha";
            count = 4;
        }*/

        rlt_day_image.post(new Runnable() {
            @Override
            public void run() {
                int width = rlt_day_image.getWidth();
                width = width/5;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.FILL_PARENT);
                params.setMargins(width*count,0,0,0);
                lnr_slot.setLayoutParams(params);
            }
        });

        txt_slot_prayer.setText(nextPrayer);
        txt_is_prayed.setText(getResources().getString(R.string.i_prayed) + " " + nextPrayer);

        generateTable(count);


        if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.fajr))) {
            ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(prayerTimingModel.isIsprayed_fajr());
        } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.dhudr))) {
            ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(prayerTimingModel.isIsprayed_dhuhr());
        } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.asar))) {
            ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(prayerTimingModel.isIsprayed_asr());
        } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.magrib))) {
            ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(prayerTimingModel.isIsprayed_maghrib());
        } else if (nextPrayer.equalsIgnoreCase(getResources().getString(R.string.isha))) {
            ((CheckBox) findViewById(R.id.tik_prayed)).setChecked(prayerTimingModel.isIsprayed_isha());
        }
    }

    private void generateTable(int index) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        lnr_prayer_table.removeAllViews();

        for (int i=0; i<prayerTableData.get(index).size(); i++){

            View view = inflater.inflate(R.layout.item_prayer_table_row,null);

            TextView txt_key = view.findViewById(R.id.txt_key);
            txt_key.setText(prayerTableData.get(index).get(i).getKey());

            TextView txt_value = view.findViewById(R.id.txt_value);
            txt_value.setText(prayerTableData.get(index).get(i).getValue());

            lnr_prayer_table.addView(view);
        }
    }

    private Long getTimeStamp(String tine){
        return TimingUtils.getTimeStampOfTime(tine).getTime();
    }
}

package com.krishiv.myislam.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.TimingUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends BaseMenuActivity{

    CompactCalendarView compactCalendarView;
    ImageView img_prev, img_next;
    TextView txt_islam_date, txt_english_date, txt_holiday;
    SimpleDateFormat dateFormatForDisplaying;
    PrayerTimingModel prayerTimingModel;
    //LinearLayout lnr_holiday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_calendar);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        img_prev = findViewById(R.id.img_prev);
        img_next = findViewById(R.id.img_next);
        txt_islam_date = findViewById(R.id.txt_islam_date);
        txt_english_date = findViewById(R.id.txt_english_date);
        //lnr_holiday = findViewById(R.id.lnr_holiday);
        txt_holiday = findViewById(R.id.txt_holiday);
        //lnr_holiday.setVisibility(View.GONE);

        txt_holiday.setText("No Holiday Today");

        customizeCalendar();

        img_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollLeft();
            }
        });

        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compactCalendarView.scrollRight();
            }
        });

        findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        txt_english_date.setText(dateFormatForDisplaying.format(calendar.getTime()));

        if(!db.isAvailable(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, calendar.getTime())))
            callApiForPrayerTimes(TimingUtils.getDateToString(TimingUtils.Month, calendar.getTime()), TimingUtils.getDateToString(TimingUtils.Year, calendar.getTime()), new CallBackTask() {
                @Override
                public void onTaskDone() {
                    prayerTimingModel = db.getByDate(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, calendar.getTime()));
                    if (prayerTimingModel != null)
                        setClickedDateData();
                }
            });
        else{
            prayerTimingModel = db.getByDate(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, calendar.getTime()));
            if (prayerTimingModel != null)
                setClickedDateData();
        }
    }

    private void customizeCalendar() {
        prayerTimingModel = new PrayerTimingModel();
        dateFormatForDisplaying = new SimpleDateFormat(TimingUtils.CalendarTitleFormat);
//        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
//        compactCalendarView.setLocale(Locale.CHINESE);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Log.e("Calendar", "Day was clicked: " + dateClicked);
                txt_english_date.setText(dateFormatForDisplaying.format(dateClicked));
                prayerTimingModel = db.getByDate(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, dateClicked));
                if (prayerTimingModel != null)
                    setClickedDateData();
            }

            @Override
            public void onMonthScroll(final Date firstDayOfNewMonth) {
                txt_english_date.setText(dateFormatForDisplaying.format(firstDayOfNewMonth));
                Log.e("Calendar", "Month was scrolled to: " + firstDayOfNewMonth);

                if(!db.isAvailable(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, firstDayOfNewMonth))) {
                    callApiForPrayerTimes(TimingUtils.getDateToString(TimingUtils.Month, firstDayOfNewMonth), TimingUtils.getDateToString(TimingUtils.Year, firstDayOfNewMonth), new CallBackTask() {
                        @Override
                        public void onTaskDone() {
                            prayerTimingModel = db.getByDate(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, firstDayOfNewMonth));
                            if (prayerTimingModel != null)
                                setClickedDateData();
                        }
                    });
                }else {
                    prayerTimingModel = db.getByDate(TimingUtils.getDateToString(TimingUtils.SQLiteDateFormat, firstDayOfNewMonth));
                    if (prayerTimingModel != null)
                        setClickedDateData();
                }

            }
        });
    }

    private void setClickedDateData() {

        txt_islam_date.setText(prayerTimingModel.getHijriModel().getMonth_en() + " "
        + prayerTimingModel.getHijriModel().getYear() + " " + prayerTimingModel.getHijriModel().getDesignation_abb());

        if (prayerTimingModel.getHijriModel().getHolidays().contentEquals("") || prayerTimingModel.getHijriModel().getHolidays().length()<=2) {
            //lnr_holiday.setVisibility(View.GONE);
            txt_holiday.setText("No Holiday Today");
        }
        else {
            //lnr_holiday.setVisibility(View.VISIBLE);

            String strH = "" + prayerTimingModel.getHijriModel().getHolidays().substring(1,prayerTimingModel.getHijriModel().getHolidays().length()-1).replaceAll("\"","");

            txt_holiday.setText(strH +
            "\n"+prayerTimingModel.getHijriModel().getDay() + " " +
            prayerTimingModel.getHijriModel().getMonth_en() + " " +
            prayerTimingModel.getHijriModel().getYear() + " " +
            prayerTimingModel.getHijriModel().getDesignation_abb() +
            "\n"+ txt_english_date.getText().toString());

        }
    }
}

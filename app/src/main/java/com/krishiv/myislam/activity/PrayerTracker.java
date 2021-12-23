package com.krishiv.myislam.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.krishiv.myislam.R;
import com.krishiv.myislam.dto.GraphDataModel;
import com.krishiv.myislam.menu.BaseMenuActivity;
import com.krishiv.myislam.utils.TimingUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class PrayerTracker extends BaseMenuActivity implements View.OnClickListener{

    TextView txt_yearly, txt_monthly​, txt_weekly;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViev(R.layout.activity_prayer_tracker);

        chart = (LineChart) findViewById(R.id.chart);
        txt_yearly = findViewById(R.id.txt_yearly);
        txt_monthly​ = findViewById(R.id.txt_monthly);
        txt_weekly = findViewById(R.id.txt_weekly);

        try {
            List<String> dataCount = db.getTrackerWMYData();

            txt_yearly.setText(dataCount.get(2));
            txt_monthly​.setText(dataCount.get(1));
            txt_weekly.setText(dataCount.get(0));
            setDataToGraph();
        }catch (Exception e){
            Log.e("Exception", "Prayer Tracker");
        }
    }

    List<Entry> entries;
    private void setDataToGraph() throws Exception{
        List<GraphDataModel> graphData = db.getTrackerData();
        entries = new ArrayList<Entry>();
        //new DateFormatSymbols().getMonths()[cur.getInt(0)-1]
        entries.add(new Entry(0, 0, ""));

        if (graphData.size()>0){
            for (int i=1; i<graphData.get(0).getMonth() ;i++){
                entries.add(new Entry(i, 0, new DateFormatSymbols().getMonths()[i-1]));
            }
        }

        for (GraphDataModel data : graphData) {
            entries.add(new Entry(data.getPinPointX(), data.getPinPointY(), new DateFormatSymbols().getMonths()[data.getMonth()-1]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(getResources().getColor(R.color.colorYellow));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawCircles(false);
        LineData lineData = new LineData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
//        xAxis.setLabelRotationAngle(270);
//        xAxis.setLabelCount(entries.size());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (String) entries.get((int)value).getData();
            }
        });

        chart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) Math.floor(value));
            }
        });

//        chart.getAxisLeft().setLabelCount(max);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setGranularity(1f);

        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawAxisLine(false);

        chart.getAxisLeft().setTextColor(Color.WHITE);

        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);

        chart.setData(lineData);
        chart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.arrow_back:
                finish();
                break;
        }
    }
}

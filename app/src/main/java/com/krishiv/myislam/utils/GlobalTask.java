package com.krishiv.myislam.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.dto.PrayerTimingHijriModel;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.dto.RequestPrayerModel;
import com.krishiv.myislam.dto.ResultComman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class GlobalTask {

    private static ProgressDialog progressDialog;
    public static Toast mToast=null;

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        return progressDialog;
    }

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public static void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static void showTostErrorResponse(Context context, ResponseBody responseBody){
        try {
//            String data = response.errorBody().string();
            String data = responseBody.string();
            Log.e("API Eroor", data);
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("error_description"))
                GlobalTask.showToastMessage(context, jsonObject.getString("error_description"));
            else if (jsonObject.has("errorMessage"))
                GlobalTask.showToastMessage(context, jsonObject.getString("errorMessage"));
            else if (jsonObject.has("message"))
                GlobalTask.showToastMessage(context, jsonObject.getString("message"));
            else if (jsonObject.has("result")){
                jsonObject = new JSONObject(jsonObject.getString("result"));
                GlobalTask.showToastMessage(context, jsonObject.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static <T> ArrayList<T> toList(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        Gson gson = new Gson();
        return (ArrayList<T>) gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }

    public static void showToastMessage(Context context, String msg){
        if(mToast!=null)
            mToast.cancel();
        mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static ArrayList<PrayerTimingModel> filterCalendarData(ResultComman resultObj){
        ArrayList<PrayerTimingModel> data = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(resultObj.getData().toString());

            JSONObject jsonObject;
            PrayerTimingModel model;
            for (int i = 0; i < jsonArray.length(); i++) {
                model = new PrayerTimingModel();
                jsonObject = jsonArray.getJSONObject(i);

                JSONObject jTiming = jsonObject.getJSONObject("timings");
                JSONObject jDate = jsonObject.getJSONObject("date");

                model.setFajr(jTiming.getString("Fajr").substring(0, 5));
                model.setDhuhr(jTiming.getString("Dhuhr").substring(0, 5));
                model.setAsr(jTiming.getString("Asr").substring(0, 5));
                model.setMaghrib(jTiming.getString("Maghrib").substring(0, 5));
                model.setIsha(jTiming.getString("Isha").substring(0, 5));

                model.setDate(jDate.getString("readable"));

                PrayerTimingHijriModel hijriModel = new PrayerTimingHijriModel();
                hijriModel.setDate(jDate.getJSONObject("hijri").getString("date"));
                hijriModel.setDay(jDate.getJSONObject("hijri").getString("day"));
                hijriModel.setYear(jDate.getJSONObject("hijri").getString("year"));
                hijriModel.setWeekday_en(jDate.getJSONObject("hijri").getJSONObject("weekday").getString("en"));
                hijriModel.setMonth_en(jDate.getJSONObject("hijri").getJSONObject("month").getString("en"));
                hijriModel.setDesignation_abb(jDate.getJSONObject("hijri").getJSONObject("designation").getString("abbreviated"));
                hijriModel.setHolidays(jDate.getJSONObject("hijri").getString("holidays"));

                model.setHijriModel(hijriModel);

                data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }


    /*
        ResultComman resultObj = response.body();
        if (resultObj != null) {
            if (resultObj.getContent() instanceof String) {
                Toast.makeText(context, resultObj.getContent().toString(), Toast.LENGTH_LONG).show();
            } else if (resultObj.getContent() instanceof LinkedTreeMap) {
                JsonObject jsonObject = new Gson().toJsonTree(resultObj.getContent()).getAsJsonObject();
                String strJson = jsonObject.get("dailyQuoteText").toString();
            }
        } else {
            GlobalTask.showTostErrorResponse(context, response.errorBody());
        }*/
}

package com.krishiv.myislam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.dto.QuranDetailModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Shared {
    SharedPreferences pref;
    Editor edit;

    /*Constant*/
    public final String isLogin = "is_login";
    public final String token = "token";
    public static final String userProfile = "user_profile";
    public static final String lastDailyQuote = "lastDailyQuote";
    public static final String appConfig = "appConfig";
    public static final String upcomingAlarmData = "upcomingAlarmData";
    public static final String HadithJsonString  = "HadithJsonString";
    /*Constant*/
    public static final String LANGUAGE_PATH = "path";
    public static final String FAVORITES_QURAN = "FAVORITES_QURAN";


    public Shared(Context c) {
        // TODO Auto-generated constructor stub

        pref = c.getSharedPreferences("file_pref", Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    public void putBoolean(String key, boolean b) {
        edit.putBoolean(key, b);
        edit.commit();
    }

    public boolean getBoolean(String key, boolean b) {
        return pref.getBoolean(key, b);
    }

    public void putString(String key, String val) {
        edit.putString(key, val);
        edit.commit();
    }

    public String getString(String key, String def) {
        return pref.getString(key, def);
    }

    public void putLong(String key, Long val) {
        edit.putLong(key, val);
        edit.commit();
    }

    public Long getLong(String key, Long def) {
        return pref.getLong(key, def);
    }

    public void putInt(String key, int def) {
        edit.putInt(key, def);
        edit.commit();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }


    public boolean clearAll(){
        edit.clear();
        edit.commit();
        return true;
    }

    public void saveFavoritesQuran(ArrayList<QuranDetailModel> arrayList , Context context ){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(arrayList);

        editor.putString(FAVORITES_QURAN, json);
        editor.commit();
    }

    public ArrayList<QuranDetailModel>  getFavoritesQuran(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(FAVORITES_QURAN, "");
        Type type = new TypeToken<ArrayList<QuranDetailModel>>() {}.getType();
        ArrayList<QuranDetailModel> arrayList = gson.fromJson(json, type);
        return arrayList;
    }
}

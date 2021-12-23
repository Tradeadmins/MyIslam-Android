package com.krishiv.myislam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.krishiv.myislam.dto.HadithFavouriteModel;
import com.krishiv.myislam.dto.NameOfAllahModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedHadithFav {
    SharedPreferences pref;
    Editor edit;

    public static final String hadithName = "hadithName";

    public SharedHadithFav(Context c) {
        // TODO Auto-generated constructor stub

        pref = c.getSharedPreferences("file_pref_allah", Context.MODE_PRIVATE);
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

    public void putInt(String key, int def) {
        edit.putInt(key, def);
        edit.commit();
    }

    public int getInt(String key) {
        return pref.getInt(key, 0);
    }


    public ArrayList<HadithFavouriteModel> getDataArray(){
        ArrayList<HadithFavouriteModel> data = new ArrayList<>();
        Type listType = new TypeToken<List<HadithFavouriteModel>>() {}.getType();

        data = new Gson().fromJson(pref.getString(hadithName,""), listType);

        return data;
    }
}

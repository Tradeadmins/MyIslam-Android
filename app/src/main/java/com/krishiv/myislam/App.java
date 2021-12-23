package com.krishiv.myislam;

import android.app.Application;
import android.content.Context;

import com.krishiv.myislam.utils.LocaleHelper;

public class App extends Application {

    public static int currentLanuageId = 1; //lng_depended
    public static String PAGE_SIZE = "50";

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base,"en"));
    }
}

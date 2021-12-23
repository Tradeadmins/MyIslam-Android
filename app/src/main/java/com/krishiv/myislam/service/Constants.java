package com.krishiv.myislam.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.krishiv.myislam.R;

public class Constants {

    //

    public static final String HOST = "http://103.73.190.66:8080/api/account/";
    public static final String REGISTRATION_API = HOST + "Register";
    public static final int REGISTRATION_TOKEN = 101;

    public static  String MY_LANGUAGE_FILE = "myfile.json";


    public interface ACTION {
        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        public static String PAUSE_ACTION = "com.marothiatechs.customnotification.action.pause";
        public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.play, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }


}

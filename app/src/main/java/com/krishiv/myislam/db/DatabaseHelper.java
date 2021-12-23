package com.krishiv.myislam.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final Object[] LOCK = new Object[]{"SYNC LOCK"};
    private static final String DATABASE_NAME = "myislam.db";
    private static final int DATABASE_VERSION = 1;

    //TODO DATABASE
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PrayerTimingDAO.getCreateTableScript());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PrayerTimingDAO.getDropTableScript());
        //onCreate(db);
    }
}
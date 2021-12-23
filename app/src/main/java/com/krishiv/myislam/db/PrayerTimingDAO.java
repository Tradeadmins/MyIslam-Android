package com.krishiv.myislam.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.krishiv.myislam.dto.GraphDataModel;
import com.krishiv.myislam.dto.PrayerTimingHijriModel;
import com.krishiv.myislam.dto.PrayerTimingModel;
import com.krishiv.myislam.utils.TimingUtils;

import java.util.ArrayList;
import java.util.List;

public class PrayerTimingDAO {
    private Context context;

    public PrayerTimingDAO(Context context) {
        this.context = context;
    }

    private static final String TABLE_NAME = "tbl_timing";

    private static final String COL_ID = "id";
    private static final String COL_DATE = "date";
    private static final String COL_TIME_FAJR = "fajr";
    private static final String COL_TIME_DHUHR = "dhuhr";
    private static final String COL_TIME_ASAR = "asr";
    private static final String COL_TIME_MAGHRIB = "maghrib";
    private static final String COL_TIME_ISHA = "isha";

    private static final String COL_ISPRAYED_FAJR = "isprayed_fajr";
    private static final String COL_ISPRAYED_DHUHR = "isprayed_dhuhr";
    private static final String COL_ISPRAYED_ASAR = "isprayed_asr";
    private static final String COL_ISPRAYED_MAGHRIB = "isprayed_maghrib";
    private static final String COL_ISPRAYED_ISHA = "isprayed_isha";

    /*Hijri Data Column*/

    private static final String COL_HIJRI_DATE = "hijri_date";
    private static final String COL_HIJRI_DAY = "hijri_day";
    private static final String COL_HIJRI_WEEKDAY_EN = "hijri_weekday_en";
    private static final String COL_HIJRI_MONTH_EN = "hijri_en";
    private static final String COL_HIJRI_YEAR = "hijri_year";
    private static final String COL_HIJRI_DESIGNATION_ABB = "hijri_designation_abb";
    private static final String COL_HIJRI_HOLIDAYS = "hijri_holidays";



    public static String getCreateTableScript() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_DATE + " TEXT, "
                + COL_TIME_FAJR + " TEXT, "
                + COL_TIME_DHUHR + " TEXT, "
                + COL_TIME_ASAR + " TEXT, "
                + COL_TIME_MAGHRIB + " TEXT, "
                + COL_TIME_ISHA + " TEXT, "
                + COL_ISPRAYED_FAJR + " INTEGER DEFAULT 0, "
                + COL_ISPRAYED_DHUHR + " INTEGER DEFAULT 0, "
                + COL_ISPRAYED_ASAR + " INTEGER DEFAULT 0, "
                + COL_ISPRAYED_MAGHRIB + " INTEGER DEFAULT 0, "
                + COL_ISPRAYED_ISHA + " INTEGER DEFAULT 0, "
                + COL_HIJRI_DATE + " TEXT,"
                + COL_HIJRI_DAY + " TEXT,"
                + COL_HIJRI_WEEKDAY_EN + " TEXT,"
                + COL_HIJRI_MONTH_EN + " TEXT,"
                + COL_HIJRI_YEAR + " TEXT,"
                + COL_HIJRI_DESIGNATION_ABB + " TEXT,"
                + COL_HIJRI_HOLIDAYS + "  TEXT);";
    }

    public static String getDropTableScript() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static String getDeleteTableDataScript() {
        return "DELETE FROM " + TABLE_NAME;
    }

    public void deleteTableData() {
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            db.execSQL(getDeleteTableDataScript());
            db.close();
        }
    }

    public void TruncateTable() {
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            TruncateTable(db);
            db.close();
        }
    }

    private void TruncateTable(SQLiteDatabase db) {
        db.execSQL(getDropTableScript());
        db.execSQL(getCreateTableScript());
    }

    public boolean save(PrayerTimingModel obj) {
        boolean flag_status = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            try {
                if (isAvailable(obj.getDate() + "")) {
                    ContentValues v = setUpdateParameters(obj);
                    db.update(TABLE_NAME, v, COL_ID + "=" + obj.getId(), null);
                } else {
                    ContentValues v = setParameters(obj);
                    db.insert(TABLE_NAME, null, v);
                }
                flag_status = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return flag_status;
    }

    public boolean saveAll(List<PrayerTimingModel> objList) {
        boolean flag_status = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            try {
                PrayerTimingModel obj;
                for (int i = 0; i < objList.size(); i++) {
                    obj = objList.get(i);

                    obj.setDate(TimingUtils.getStringTimeBasedOnFormat(TimingUtils.PrayerTimeDateFormat, TimingUtils.SQLiteDateFormat, obj.getDate()));

                    if (isAvailable(obj.getDate() + "")) {
                        ContentValues v = setUpdateParameters(obj);
                        db.update(TABLE_NAME, v, COL_ID + "=" + obj.getId(), null);
                    } else {
                        ContentValues v = setParameters(obj);
                        db.insert(TABLE_NAME, null, v);
                    }
                }
                flag_status = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return flag_status;
    }

    public boolean update(PrayerTimingModel obj) {
        boolean flag_status = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            try {
                ContentValues v = setParameters(obj);
                db.update(TABLE_NAME, v, COL_ID + "=" + obj.getId(), null);
                flag_status = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return flag_status;
    }

    public PrayerTimingModel getByDate(String date) {
        PrayerTimingModel item = null;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                Cursor cur = db.query(TABLE_NAME, null, COL_DATE + "=?", new String[]{String.valueOf(date)}, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        item = readFromCursor(cur);
                    }
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return item;
    }

    public boolean isLastPopupOpened(String date, String col) {
        boolean item = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                Cursor  cur = db.rawQuery("SELECT date, "+col+", isprayed_"+col+" FROM tbl_timing where date = '"+date+"'", null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        item = cur.getInt(2) == 1;
                    }
                }
                cur.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return item;
    }

    public List<PrayerTimingModel> getAll() {
        List<PrayerTimingModel> dataList = new ArrayList<>();
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                Cursor cur = db.query(TABLE_NAME, null, null, null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        dataList.add(readFromCursor(cur));
                    }
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return dataList;
    }

    public List<GraphDataModel> getTrackerData() {
        List<GraphDataModel> dataList = new ArrayList<>();
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                String strStart = TimingUtils.getDayByAddSub(TimingUtils.SQLiteDateFormat,-7);
                //Cursor  cur = db.rawQuery("SELECT substr(date,length(date)-7,length(date)-7) as today, SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing GROUP BY substr(date,length(date)-7,length(date)-7)", null);
                String yyyy = TimingUtils.getToday(TimingUtils.Year);
                Cursor  cur = db.rawQuery("SELECT strftime('%m', date) as month, SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as y FROM tbl_timing where strftime('%Y', date)='"+yyyy+"' GROUP BY strftime('%m', date)", null);
                if (cur.getCount() > 0) {
                    //int counter = 1;
                    while (cur.moveToNext()) {
//                        dataList.add(new GraphDataModel(counter++, cur.getInt(1), cur.getInt(0)));
                        dataList.add(new GraphDataModel(cur.getInt(0), cur.getInt(1), cur.getInt(0)));
                    }
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return dataList;
    }

    public List<String> getTrackerWMYData() {
        List<String> dataList = new ArrayList<>();
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {

                String strStart = TimingUtils.getDayByAddSub(TimingUtils.SQLiteDateFormat,-6);
                String strEnd = TimingUtils.getToday(TimingUtils.SQLiteDateFormat);

                Cursor cur = db.rawQuery("SELECT SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing where date BETWEEN '"+strStart+"' AND '"+strEnd+"'", null);
                if(cur.moveToFirst())
                    dataList.add(cur.getInt(0)+"");
                cur.close();

                //cur = db.rawQuery("SELECT SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing where date like '%"+strStart+"%'", null);
                cur = db.rawQuery("SELECT SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing GROUP BY strftime('%m', "+strEnd+")", null);
                if(cur.moveToFirst())
                    dataList.add(cur.getInt(0)+"");
                cur.close();

                //SELECT SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing GROUP BY substr(date,length(date)-4,length(date))
                cur = db.rawQuery("SELECT SUM(isprayed_fajr) + SUM(isprayed_dhuhr) + SUM(isprayed_asr) + SUM(isprayed_maghrib) + SUM(isprayed_isha) as total FROM tbl_timing GROUP BY strftime('%Y', "+strEnd+")", null);
                if(cur.moveToFirst())
                    dataList.add(cur.getInt(0)+"");
                cur.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return dataList;
    }


    public int getCount() {
        int count = 0;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                Cursor cur = db.query(TABLE_NAME, null, null, null, null, null, null);
                count = cur.getCount();
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return count;
    }

    public boolean deleteById(String id) {
        boolean flag_status = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
            try {
                db.delete(TABLE_NAME, COL_ID + "='" + id + "'", null);
                flag_status = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return flag_status;
    }

    public boolean isAvailable(String date) {
        boolean flag_available = false;
        synchronized (DatabaseHelper.LOCK) {
            SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
            try {
                Cursor cur = db.query(TABLE_NAME, null, COL_DATE + "=?", new String[]{String.valueOf(date)}, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        flag_available = true;
                    }
                }
                cur.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return flag_available;
    }

    private ContentValues setUpdateParameters(PrayerTimingModel r) {
        ContentValues p = new ContentValues();
        p.put(COL_ISPRAYED_FAJR, r.isIsprayed_fajr() ? 1 : 0);
        p.put(COL_ISPRAYED_DHUHR, r.isIsprayed_dhuhr() ? 1 : 0);
        p.put(COL_ISPRAYED_ASAR, r.isIsprayed_asr() ? 1 : 0);
        p.put(COL_ISPRAYED_MAGHRIB, r.isIsprayed_maghrib() ? 1 : 0);
        p.put(COL_ISPRAYED_ISHA, r.isIsprayed_isha() ? 1 : 0);
        return p;
    }

    public ContentValues setParameters(PrayerTimingModel r) {
        ContentValues p = new ContentValues();

//        p.put(COL_ID, r.getId());
        p.put(COL_DATE, r.getDate());

        p.put(COL_TIME_FAJR, r.getFajr());
        p.put(COL_TIME_DHUHR, r.getDhuhr());
        p.put(COL_TIME_ASAR, r.getAsr());
        p.put(COL_TIME_MAGHRIB, r.getMaghrib());
        p.put(COL_TIME_ISHA, r.getIsha());

        p.put(COL_ISPRAYED_FAJR, r.isIsprayed_fajr() ? 1 : 0);
        p.put(COL_ISPRAYED_DHUHR, r.isIsprayed_dhuhr() ? 1 : 0);
        p.put(COL_ISPRAYED_ASAR, r.isIsprayed_asr() ? 1 : 0);
        p.put(COL_ISPRAYED_MAGHRIB, r.isIsprayed_maghrib() ? 1 : 0);
        p.put(COL_ISPRAYED_ISHA, r.isIsprayed_isha() ? 1 : 0);


        p.put(COL_HIJRI_DATE, r.getHijriModel().getDate());
        p.put(COL_HIJRI_DAY, r.getHijriModel().getDay());
        p.put(COL_HIJRI_YEAR, r.getHijriModel().getYear());
        p.put(COL_HIJRI_WEEKDAY_EN, r.getHijriModel().getWeekday_en());
        p.put(COL_HIJRI_MONTH_EN, r.getHijriModel().getMonth_en());
        p.put(COL_HIJRI_DESIGNATION_ABB, r.getHijriModel().getDesignation_abb());
        p.put(COL_HIJRI_HOLIDAYS, r.getHijriModel().getHolidays());

        return p;
    }

    private PrayerTimingModel readFromCursor(Cursor c) {
        PrayerTimingModel r = new PrayerTimingModel();

        r.setId(c.getInt(c.getColumnIndex(COL_ID)));
        r.setDate(c.getString(c.getColumnIndex(COL_DATE)));

        r.setFajr(c.getString(c.getColumnIndex(COL_TIME_FAJR)));
        r.setDhuhr(c.getString(c.getColumnIndex(COL_TIME_DHUHR)));
        r.setAsr(c.getString(c.getColumnIndex(COL_TIME_ASAR)));
        r.setMaghrib(c.getString(c.getColumnIndex(COL_TIME_MAGHRIB)));
        r.setIsha(c.getString(c.getColumnIndex(COL_TIME_ISHA)));

        r.setIsprayed_fajr(c.getInt(c.getColumnIndex(COL_ISPRAYED_FAJR)) == 1);
        r.setIsprayed_dhuhr(c.getInt(c.getColumnIndex(COL_ISPRAYED_DHUHR)) == 1);
        r.setIsprayed_asr(c.getInt(c.getColumnIndex(COL_ISPRAYED_ASAR)) == 1);
        r.setIsprayed_maghrib(c.getInt(c.getColumnIndex(COL_ISPRAYED_MAGHRIB)) == 1);
        r.setIsprayed_isha(c.getInt(c.getColumnIndex(COL_ISPRAYED_ISHA)) == 1);

        PrayerTimingHijriModel hijriModel = new PrayerTimingHijriModel();
        hijriModel.setDate(c.getString(c.getColumnIndex(COL_HIJRI_DATE)));
        hijriModel.setDay(c.getString(c.getColumnIndex(COL_HIJRI_DAY)));
        hijriModel.setYear(c.getString(c.getColumnIndex(COL_HIJRI_YEAR)));
        hijriModel.setWeekday_en(c.getString(c.getColumnIndex(COL_HIJRI_WEEKDAY_EN)));
        hijriModel.setMonth_en(c.getString(c.getColumnIndex(COL_HIJRI_MONTH_EN)));
        hijriModel.setDesignation_abb(c.getString(c.getColumnIndex(COL_HIJRI_DESIGNATION_ABB)));
        hijriModel.setHolidays(c.getString(c.getColumnIndex(COL_HIJRI_HOLIDAYS)));
        r.setHijriModel(hijriModel);

        return r;
    }
}
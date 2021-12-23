package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class RamdanResponseModel {

    @SerializedName("data")
    private List<Data> data;
    @SerializedName("status")
    private String status;
    @SerializedName("code")
    private int code;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class Data {
        @SerializedName("meta")
        private Meta meta;
        @SerializedName("date")
        private Date date;
        @SerializedName("timings")
        private Timings timings;

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Timings getTimings() {
            return timings;
        }

        public void setTimings(Timings timings) {
            this.timings = timings;
        }
    }

    public static class Meta {
        @SerializedName("offset")
        private Offset offset;
        @SerializedName("school")
        private String school;
        @SerializedName("midnightMode")
        private String midnightMode;
        @SerializedName("latitudeAdjustmentMethod")
        private String latitudeAdjustmentMethod;
        @SerializedName("method")
        private Method method;
        @SerializedName("timezone")
        private String timezone;
        @SerializedName("longitude")
        private double longitude;
        @SerializedName("latitude")
        private double latitude;

        public Offset getOffset() {
            return offset;
        }

        public void setOffset(Offset offset) {
            this.offset = offset;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getMidnightMode() {
            return midnightMode;
        }

        public void setMidnightMode(String midnightMode) {
            this.midnightMode = midnightMode;
        }

        public String getLatitudeAdjustmentMethod() {
            return latitudeAdjustmentMethod;
        }

        public void setLatitudeAdjustmentMethod(String latitudeAdjustmentMethod) {
            this.latitudeAdjustmentMethod = latitudeAdjustmentMethod;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }

    public static class Offset {
        @SerializedName("Midnight")
        private int Midnight;
        @SerializedName("Isha")
        private int Isha;
        @SerializedName("Sunset")
        private int Sunset;
        @SerializedName("Maghrib")
        private int Maghrib;
        @SerializedName("Asr")
        private int Asr;
        @SerializedName("Dhuhr")
        private int Dhuhr;
        @SerializedName("Sunrise")
        private int Sunrise;
        @SerializedName("Fajr")
        private int Fajr;
        @SerializedName("Imsak")
        private int Imsak;

        public int getMidnight() {
            return Midnight;
        }

        public void setMidnight(int Midnight) {
            this.Midnight = Midnight;
        }

        public int getIsha() {
            return Isha;
        }

        public void setIsha(int Isha) {
            this.Isha = Isha;
        }

        public int getSunset() {
            return Sunset;
        }

        public void setSunset(int Sunset) {
            this.Sunset = Sunset;
        }

        public int getMaghrib() {
            return Maghrib;
        }

        public void setMaghrib(int Maghrib) {
            this.Maghrib = Maghrib;
        }

        public int getAsr() {
            return Asr;
        }

        public void setAsr(int Asr) {
            this.Asr = Asr;
        }

        public int getDhuhr() {
            return Dhuhr;
        }

        public void setDhuhr(int Dhuhr) {
            this.Dhuhr = Dhuhr;
        }

        public int getSunrise() {
            return Sunrise;
        }

        public void setSunrise(int Sunrise) {
            this.Sunrise = Sunrise;
        }

        public int getFajr() {
            return Fajr;
        }

        public void setFajr(int Fajr) {
            this.Fajr = Fajr;
        }

        public int getImsak() {
            return Imsak;
        }

        public void setImsak(int Imsak) {
            this.Imsak = Imsak;
        }
    }

    public static class Method {
        @SerializedName("params")
        private Params params;
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private int id;

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Params {
        @SerializedName("Isha")
        private int Isha;
        @SerializedName("Fajr")
        private int Fajr;

        public int getIsha() {
            return Isha;
        }

        public void setIsha(int Isha) {
            this.Isha = Isha;
        }

        public int getFajr() {
            return Fajr;
        }

        public void setFajr(int Fajr) {
            this.Fajr = Fajr;
        }
    }

    public static class Date {
        @SerializedName("hijri")
        private Hijri hijri;
        @SerializedName("gregorian")
        private Gregorian gregorian;
        @SerializedName("timestamp")
        private String timestamp;
        @SerializedName("readable")
        private String readable;

        public Hijri getHijri() {
            return hijri;
        }

        public void setHijri(Hijri hijri) {
            this.hijri = hijri;
        }

        public Gregorian getGregorian() {
            return gregorian;
        }

        public void setGregorian(Gregorian gregorian) {
            this.gregorian = gregorian;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getReadable() {
            return readable;
        }

        public void setReadable(String readable) {
            this.readable = readable;
        }
    }

    public static class Hijri {
        @SerializedName("holidays")
        private List<String> holidays;

        @SerializedName("year")
        private String year;
        @SerializedName("month")
        private Month month;
        @SerializedName("weekday")
        private Weekday weekday;
        @SerializedName("day")
        private String day;
        @SerializedName("format")
        private String format;
        @SerializedName("date")
        private String date;

        public List<String> getHolidays() {
            return holidays;
        }

        public void setHolidays(List<String> holidays) {
            this.holidays = holidays;
        }



        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public Month getMonth() {
            return month;
        }

        public void setMonth(Month month) {
            this.month = month;
        }

        public Weekday getWeekday() {
            return weekday;
        }

        public void setWeekday(Weekday weekday) {
            this.weekday = weekday;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }



    public static class Month {
        @SerializedName("ar")
        private String ar;
        @SerializedName("en")
        private String en;
        @SerializedName("number")
        private int number;

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class Weekday {
        @SerializedName("ar")
        private String ar;
        @SerializedName("en")
        private String en;

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }

    public static class Gregorian {

        @SerializedName("year")
        private String year;
        @SerializedName("month")
        private MonthEnglish month;
        @SerializedName("weekday")
        private WeekdayEnglish weekday;
        @SerializedName("day")
        private String day;
        @SerializedName("format")
        private String format;
        @SerializedName("date")
        private String date;


        public String getYear() {
            return year;
        }

        public MonthEnglish getMonth() {
            return month;
        }

        public void setMonth(MonthEnglish month) {
            this.month = month;
        }

        public WeekdayEnglish getWeekday() {
            return weekday;
        }

        public void setWeekday(WeekdayEnglish weekday) {
            this.weekday = weekday;
        }

        public void setYear(String year) {
            this.year = year;
        }



        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }


    public static class MonthEnglish {
        @SerializedName("en")
        private String en;
        @SerializedName("number")
        private int number;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class WeekdayEnglish {
        @SerializedName("en")
        private String en;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }
    }

    public static class Timings {
        @SerializedName("Midnight")
        private String Midnight;
        @SerializedName("Imsak")
        private String Imsak;
        @SerializedName("Isha")
        private String Isha;
        @SerializedName("Maghrib")
        private String Maghrib;
        @SerializedName("Sunset")
        private String Sunset;
        @SerializedName("Asr")
        private String Asr;
        @SerializedName("Dhuhr")
        private String Dhuhr;
        @SerializedName("Sunrise")
        private String Sunrise;
        @SerializedName("Fajr")
        private String Fajr;

        public String getMidnight() {
            return Midnight;
        }

        public void setMidnight(String Midnight) {
            this.Midnight = Midnight;
        }

        public String getImsak() {
            return Imsak;
        }

        public void setImsak(String Imsak) {
            this.Imsak = Imsak;
        }

        public String getIsha() {
            return Isha;
        }

        public void setIsha(String Isha) {
            this.Isha = Isha;
        }

        public String getMaghrib() {
            return Maghrib;
        }

        public void setMaghrib(String Maghrib) {
            this.Maghrib = Maghrib;
        }

        public String getSunset() {
            return Sunset;
        }

        public void setSunset(String Sunset) {
            this.Sunset = Sunset;
        }

        public String getAsr() {
            return Asr;
        }

        public void setAsr(String Asr) {
            this.Asr = Asr;
        }

        public String getDhuhr() {
            return Dhuhr;
        }

        public void setDhuhr(String Dhuhr) {
            this.Dhuhr = Dhuhr;
        }

        public String getSunrise() {
            return Sunrise;
        }

        public void setSunrise(String Sunrise) {
            this.Sunrise = Sunrise;
        }

        public String getFajr() {
            return Fajr;
        }

        public void setFajr(String Fajr) {
            this.Fajr = Fajr;
        }
    }
}

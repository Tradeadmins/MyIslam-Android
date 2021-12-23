package com.krishiv.myislam.dto;

public class PrayerTimingHijriModel {
    String date, day, weekday_en, month_en, year, designation_abb, holidays;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeekday_en() {
        return weekday_en;
    }

    public void setWeekday_en(String weekday_en) {
        this.weekday_en = weekday_en;
    }

    public String getMonth_en() {
        return month_en;
    }

    public void setMonth_en(String month_en) {
        this.month_en = month_en;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDesignation_abb() {
        return designation_abb;
    }

    public void setDesignation_abb(String designation_abb) {
        this.designation_abb = designation_abb;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }
}

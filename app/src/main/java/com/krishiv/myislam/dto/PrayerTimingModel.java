package com.krishiv.myislam.dto;

public class PrayerTimingModel {

    int id;
    String date, fajr, dhuhr, asr, maghrib, isha;
    boolean isprayed_fajr, isprayed_dhuhr, isprayed_asr, isprayed_maghrib, isprayed_isha;
    PrayerTimingHijriModel hijriModel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFajr() {
        return fajr;
    }

    public void setFajr(String fajr) {
        this.fajr = fajr;
    }

    public String getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(String dhuhr) {
        this.dhuhr = dhuhr;
    }

    public String getAsr() {
        return asr;
    }

    public void setAsr(String asr) {
        this.asr = asr;
    }

    public String getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(String maghrib) {
        this.maghrib = maghrib;
    }

    public String getIsha() {
        return isha;
    }

    public void setIsha(String isha) {
        this.isha = isha;
    }

    public boolean isIsprayed_fajr() {
        return isprayed_fajr;
    }

    public void setIsprayed_fajr(boolean isprayed_fajr) {
        this.isprayed_fajr = isprayed_fajr;
    }

    public boolean isIsprayed_dhuhr() {
        return isprayed_dhuhr;
    }

    public void setIsprayed_dhuhr(boolean isprayed_dhuhr) {
        this.isprayed_dhuhr = isprayed_dhuhr;
    }

    public boolean isIsprayed_asr() {
        return isprayed_asr;
    }

    public void setIsprayed_asr(boolean isprayed_asr) {
        this.isprayed_asr = isprayed_asr;
    }

    public boolean isIsprayed_maghrib() {
        return isprayed_maghrib;
    }

    public void setIsprayed_maghrib(boolean isprayed_maghrib) {
        this.isprayed_maghrib = isprayed_maghrib;
    }

    public boolean isIsprayed_isha() {
        return isprayed_isha;
    }

    public void setIsprayed_isha(boolean isprayed_isha) {
        this.isprayed_isha = isprayed_isha;
    }

    public PrayerTimingHijriModel getHijriModel() {
        return hijriModel;
    }

    public void setHijriModel(PrayerTimingHijriModel hijriModel) {
        this.hijriModel = hijriModel;
    }
}

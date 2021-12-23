package com.krishiv.myislam.dto;

public class AppConfigModel {
    boolean fajrNotification = true, dhudrNotification  = true, asarNotification = true, magribNotification = true, ishaNotification = true,
            fajrAlarm = true, dhudrAlarm = true, asarAlarm = true, magribAlarm = true, ishaAlarm = true;

    public boolean isFajrNotification() {
        return fajrNotification;
    }

    public void setFajrNotification(boolean fajrNotification) {
        this.fajrNotification = fajrNotification;
    }

    public boolean isDhudrNotification() {
        return dhudrNotification;
    }

    public void setDhudrNotification(boolean dhudrNotification) {
        this.dhudrNotification = dhudrNotification;
    }

    public boolean isAsarNotification() {
        return asarNotification;
    }

    public void setAsarNotification(boolean asarNotification) {
        this.asarNotification = asarNotification;
    }

    public boolean isMagribNotification() {
        return magribNotification;
    }

    public void setMagribNotification(boolean magribNotification) {
        this.magribNotification = magribNotification;
    }

    public boolean isIshaNotification() {
        return ishaNotification;
    }

    public void setIshaNotification(boolean ishaNotification) {
        this.ishaNotification = ishaNotification;
    }

    public boolean isFajrAlarm() {
        return fajrAlarm;
    }

    public void setFajrAlarm(boolean fajrAlarm) {
        this.fajrAlarm = fajrAlarm;
    }

    public boolean isDhudrAlarm() {
        return dhudrAlarm;
    }

    public void setDhudrAlarm(boolean dhudrAlarm) {
        this.dhudrAlarm = dhudrAlarm;
    }

    public boolean isAsarAlarm() {
        return asarAlarm;
    }

    public void setAsarAlarm(boolean asarAlarm) {
        this.asarAlarm = asarAlarm;
    }

    public boolean isMagribAlarm() {
        return magribAlarm;
    }

    public void setMagribAlarm(boolean magribAlarm) {
        this.magribAlarm = magribAlarm;
    }

    public boolean isIshaAlarm() {
        return ishaAlarm;
    }

    public void setIshaAlarm(boolean ishaAlarm) {
        this.ishaAlarm = ishaAlarm;
    }
}

package com.krishiv.myislam.dto;

public class NotiHandler{
    public String time, date, msg, prayerType, inwokeTime;
    public int action; //1-Notif -- 2-Alarm

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPrayerType() {
        return prayerType;
    }

    public void setPrayerType(String prayerType) {
        this.prayerType = prayerType;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getInwokeTime() {
        return inwokeTime;
    }

    public void setInwokeTime(String inwokeTime) {
        this.inwokeTime = inwokeTime;
    }
}
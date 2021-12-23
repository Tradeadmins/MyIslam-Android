package com.krishiv.myislam.dto;

public class RequestPrayerModel {

    int prayerRequestId;
    String prayerRequestName, prayerRequestText, prayerRequestTotalDuaCount, prayerRequestCreatedByUserId;
    boolean prayerRequestIsLiked;

    public int getPrayerRequestId() {
        return prayerRequestId;
    }

    public void setPrayerRequestId(int prayerRequestId) {
        this.prayerRequestId = prayerRequestId;
    }

    public String getPrayerRequestName() {
        return prayerRequestName;
    }

    public void setPrayerRequestName(String prayerRequestName) {
        this.prayerRequestName = prayerRequestName;
    }

    public String getPrayerRequestText() {
        return prayerRequestText;
    }

    public void setPrayerRequestText(String prayerRequestText) {
        this.prayerRequestText = prayerRequestText;
    }

    public String getPrayerRequestTotalDuaCount() {
        return prayerRequestTotalDuaCount;
    }

    public void setPrayerRequestTotalDuaCount(String prayerRequestTotalDuaCount) {
        this.prayerRequestTotalDuaCount = prayerRequestTotalDuaCount;
    }

    public String getPrayerRequestCreatedByUserId() {
        return prayerRequestCreatedByUserId;
    }

    public void setPrayerRequestCreatedByUserId(String prayerRequestCreatedByUserId) {
        this.prayerRequestCreatedByUserId = prayerRequestCreatedByUserId;
    }

    public boolean isPrayerRequestIsLiked() {
        return prayerRequestIsLiked;
    }

    public void setPrayerRequestIsLiked(boolean prayerRequestIsLiked) {
        this.prayerRequestIsLiked = prayerRequestIsLiked;
    }
}

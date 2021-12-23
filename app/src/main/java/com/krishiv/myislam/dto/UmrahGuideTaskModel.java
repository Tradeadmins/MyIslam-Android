package com.krishiv.myislam.dto;

public class UmrahGuideTaskModel {
    int umrahGuideId;
    String umrahGuideName;
    boolean umrahGuideIsCompleted;

    public int getUmrahGuideId() {
        return umrahGuideId;
    }

    public void setUmrahGuideId(int umrahGuideId) {
        this.umrahGuideId = umrahGuideId;
    }

    public String getUmrahGuideName() {
        return umrahGuideName;
    }

    public void setUmrahGuideName(String umrahGuideName) {
        this.umrahGuideName = umrahGuideName;
    }

    public boolean isUmrahGuideIsCompleted() {
        return umrahGuideIsCompleted;
    }

    public void setUmrahGuideIsCompleted(boolean umrahGuideIsCompleted) {
        this.umrahGuideIsCompleted = umrahGuideIsCompleted;
    }
}

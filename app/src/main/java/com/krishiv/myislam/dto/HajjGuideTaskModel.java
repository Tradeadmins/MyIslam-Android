package com.krishiv.myislam.dto;

public class HajjGuideTaskModel {
    int hajjGuideId, hajjGuideCompleteId;
    String hajjGuideName;
    boolean hajjGuideIsCompleted;
    boolean isRecitation;

    public int getHajjGuideId() {
        return hajjGuideId;
    }

    public void setHajjGuideId(int hajjGuideId) {
        this.hajjGuideId = hajjGuideId;
    }

    public String getHajjGuideName() {
        return hajjGuideName;
    }

    public void setHajjGuideName(String hajjGuideName) {
        this.hajjGuideName = hajjGuideName;
    }

    public boolean isHajjGuideIsCompleted() {
        return hajjGuideIsCompleted;
    }

    public void setHajjGuideIsCompleted(boolean hajjGuideIsCompleted) {
        this.hajjGuideIsCompleted = hajjGuideIsCompleted;
    }

    public int getHajjGuideCompleteId() {
        return hajjGuideCompleteId;
    }

    public void setHajjGuideCompleteId(int hajjGuideCompleteId) {
        this.hajjGuideCompleteId = hajjGuideCompleteId;
    }

    public boolean isRecitation() {
        return isRecitation;
    }

    public void setRecitation(boolean recitation) {
        isRecitation = recitation;
    }
}

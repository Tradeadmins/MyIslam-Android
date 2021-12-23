package com.krishiv.myislam.dto;

public class HajjTaskModel {
    int hajjTaskId, hajjTaskCompleteId;
    String hajjTaskName;
    boolean hajjTaskIsCompleted;
    boolean isRecitation = false;


    public int getHajjTaskId() {
        return hajjTaskId;
    }

    public void setHajjTaskId(int hajjTaskId) {
        this.hajjTaskId = hajjTaskId;
    }

    public String getHajjTaskName() {
        return hajjTaskName;
    }

    public void setHajjTaskName(String hajjTaskName) {
        this.hajjTaskName = hajjTaskName;
    }

    public boolean isHajjTaskIsCompleted() {
        return hajjTaskIsCompleted;
    }

    public void setHajjTaskIsCompleted(boolean hajjTaskIsCompleted) {
        this.hajjTaskIsCompleted = hajjTaskIsCompleted;
    }

    public int getHajjTaskCompleteId() {
        return hajjTaskCompleteId;
    }

    public void setHajjTaskCompleteId(int hajjTaskCompleteId) {
        this.hajjTaskCompleteId = hajjTaskCompleteId;
    }

    public boolean isRecitation() {
        return isRecitation;
    }

    public void setRecitation(boolean recitation) {
        isRecitation = recitation;
    }
}

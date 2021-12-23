package com.krishiv.myislam.dto;

public class UmrahTaskModel {
    int umrahTaskId, umrahTaskCompleteId;
    String umrahTaskName;
    boolean umrahTaskIsCompleted;
    boolean isRecitation;

    public int getUmrahTaskId() {
        return umrahTaskId;
    }

    public void setUmrahTaskId(int umrahTaskId) {
        this.umrahTaskId = umrahTaskId;
    }

    public String getUmrahTaskName() {
        return umrahTaskName;
    }

    public void setUmrahTaskName(String umrahTaskName) {
        this.umrahTaskName = umrahTaskName;
    }

    public boolean isUmrahTaskIsCompleted() {
        return umrahTaskIsCompleted;
    }

    public void setUmrahTaskIsCompleted(boolean umrahTaskIsCompleted) {
        this.umrahTaskIsCompleted = umrahTaskIsCompleted;
    }

    public int getUmrahTaskCompleteId() {
        return umrahTaskCompleteId;
    }

    public void setUmrahTaskCompleteId(int umrahTaskCompleteId) {
        this.umrahTaskCompleteId = umrahTaskCompleteId;
    }

    public boolean isRecitation() {
        return isRecitation;
    }

    public void setRecitation(boolean recitation) {
        isRecitation = recitation;
    }
}

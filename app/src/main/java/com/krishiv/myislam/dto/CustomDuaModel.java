package com.krishiv.myislam.dto;

public class CustomDuaModel {
    int customDuaId;
    String customDuaName, customDuaText;
    boolean isLike, isExpanded;

    public int getCustomDuaId() {
        return customDuaId;
    }

    public void setCustomDuaId(int customDuaId) {
        this.customDuaId = customDuaId;
    }

    public String getCustomDuaName() {
        return customDuaName;
    }

    public void setCustomDuaName(String customDuaName) {
        this.customDuaName = customDuaName;
    }

    public String getCustomDuaText() {
        return customDuaText;
    }

    public void setCustomDuaText(String customDuaText) {
        this.customDuaText = customDuaText;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

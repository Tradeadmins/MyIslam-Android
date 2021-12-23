package com.krishiv.myislam.dto;

public class DuasModel {

    int duaId, duaCategoryId;
    String duaName, duaArabicText, duaEnglishText, duaTurkeyText, duaMalayText, duaPronunciationText;
    boolean isLike, isExpanded;

    public int getDuaId() {
        return duaId;
    }

    public void setDuaId(int duaId) {
        this.duaId = duaId;
    }

    public int getDuaCategoryId() {
        return duaCategoryId;
    }

    public void setDuaCategoryId(int duaCategoryId) {
        this.duaCategoryId = duaCategoryId;
    }

    public String getDuaName() {
        return duaName;
    }

    public void setDuaName(String duaName) {
        this.duaName = duaName;
    }

    public String getDuaArabicText() {
        return duaArabicText;
    }

    public void setDuaArabicText(String duaArabicText) {
        this.duaArabicText = duaArabicText;
    }

    public String getDuaEnglishText() {
        return duaEnglishText;
    }

    public void setDuaEnglishText(String duaEnglishText) {
        this.duaEnglishText = duaEnglishText;
    }

    public String getDuaTurkeyText() {
        return duaTurkeyText;
    }

    public void setDuaTurkeyText(String duaTurkeyText) {
        this.duaTurkeyText = duaTurkeyText;
    }

    public String getDuaMalayText() {
        return duaMalayText;
    }

    public void setDuaMalayText(String duaMalayText) {
        this.duaMalayText = duaMalayText;
    }

    public String getDuaPronunciationText() {
        return duaPronunciationText;
    }

    public void setDuaPronunciationText(String duaPronunciationText) {
        this.duaPronunciationText = duaPronunciationText;
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

package com.krishiv.myislam.dto;

public class HadithModel {
    int hadithId;
    String hadithArabicText, hadithEnglishText, hadithTurkeyText, hadithMalayText, hadithPronunciationText;
    boolean isLike;

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getHadithId() {
        return hadithId;
    }

    public void setHadithId(int hadithId) {
        this.hadithId = hadithId;
    }

    public String getHadithArabicText() {
        return hadithArabicText;
    }

    public void setHadithArabicText(String hadithArabicText) {
        this.hadithArabicText = hadithArabicText;
    }

    public String getHadithEnglishText() {
        return hadithEnglishText;
    }

    public void setHadithEnglishText(String hadithEnglishText) {
        this.hadithEnglishText = hadithEnglishText;
    }

    public String getHadithTurkeyText() {
        return hadithTurkeyText;
    }

    public void setHadithTurkeyText(String hadithTurkeyText) {
        this.hadithTurkeyText = hadithTurkeyText;
    }

    public String getHadithMalayText() {
        return hadithMalayText;
    }

    public void setHadithMalayText(String hadithMalayText) {
        this.hadithMalayText = hadithMalayText;
    }

    public String getHadithPronunciationText() {
        return hadithPronunciationText;
    }

    public void setHadithPronunciationText(String hadithPronunciationText) {
        this.hadithPronunciationText = hadithPronunciationText;
    }
}

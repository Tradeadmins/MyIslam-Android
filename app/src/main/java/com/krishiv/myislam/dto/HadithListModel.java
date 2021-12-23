package com.krishiv.myislam.dto;

public class HadithListModel {
    public HadithListModel(int hadithNumber) {
        this.hadithNumber = hadithNumber;
    }

    public int getHadithNumber() {
        return hadithNumber;
    }

    public void setHadithNumber(int hadithNumber) {
        this.hadithNumber = hadithNumber;
    }

    int hadithNumber;
}

package com.krishiv.myislam.dto;

import com.google.gson.annotations.SerializedName;

public  class HadithCountModel {

    @SerializedName("hadith")
    private int hadith;

    public int getHadith() {
        return hadith;
    }

    public void setHadith(int hadith) {
        this.hadith = hadith;
    }
}

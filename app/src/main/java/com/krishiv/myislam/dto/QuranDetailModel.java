package com.krishiv.myislam.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class QuranDetailModel implements Parcelable {

    String sura_index , main_index , sura_name , aya_text;
    String appLanugageName ;
    boolean favourites;
    String audio_url;
    boolean isPlaying;

    public QuranDetailModel(){

    }

    public QuranDetailModel(String sura_index, String main_index, String sura_name, String aya_text, String appLanugageName, boolean favourites, String audio_url, boolean isPlaying) {
        this.sura_index = sura_index;
        this.main_index = main_index;
        this.sura_name = sura_name;
        this.aya_text = aya_text;
        this.appLanugageName = appLanugageName;
        this.favourites = favourites;
        this.audio_url = audio_url;
        this.isPlaying = isPlaying;
    }

    protected QuranDetailModel(Parcel in) {
        sura_index = in.readString();
        main_index = in.readString();
        sura_name = in.readString();
        aya_text = in.readString();
        appLanugageName = in.readString();
        favourites = in.readByte() != 0;
        audio_url = in.readString();
        isPlaying = in.readByte() != 0;
    }

    public static final Creator<QuranDetailModel> CREATOR = new Creator<QuranDetailModel>() {
        @Override
        public QuranDetailModel createFromParcel(Parcel in) {
            return new QuranDetailModel(in);
        }

        @Override
        public QuranDetailModel[] newArray(int size) {
            return new QuranDetailModel[size];
        }
    };

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public boolean isFavourites() {
        return favourites;
    }

    public void setFavourites(boolean favourites) {
        this.favourites = favourites;
    }

    public String getAppLanugageName() {
        return appLanugageName;
    }

    public void setAppLanugageName(String appLanugageName) {
        this.appLanugageName = appLanugageName;
    }

    public String getSura_index() {
        return sura_index;
    }

    public void setSura_index(String sura_index) {
        this.sura_index = sura_index;
    }

    public String getMain_index() {
        return main_index;
    }

    public void setMain_index(String main_index) {
        this.main_index = main_index;
    }

    public String getSura_name() {
        return sura_name;
    }

    public void setSura_name(String sura_name) {
        this.sura_name = sura_name;
    }

    public String getAya_text() {
        return aya_text;
    }

    public void setAya_text(String aya_text) {
        this.aya_text = aya_text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sura_index);
        dest.writeString(main_index);
        dest.writeString(sura_name);
        dest.writeString(aya_text);
        dest.writeString(appLanugageName);
        dest.writeByte((byte) (favourites ? 1 : 0));
        dest.writeString(audio_url);
        dest.writeByte((byte) (isPlaying ? 1 : 0));
    }
}

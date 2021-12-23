package com.krishiv.myislam.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class SuraModel implements Parcelable {

    String _index , _ayas , _start , _name , _tname , _ename , _type , _order ,_rukus;
    String aya_index;


    protected SuraModel(Parcel in) {
        _index = in.readString();
        _ayas = in.readString();
        _start = in.readString();
        _name = in.readString();
        _tname = in.readString();
        _ename = in.readString();
        _type = in.readString();
        _order = in.readString();
        _rukus = in.readString();
        aya_index = in.readString();
    }

    public SuraModel() {

    }

    public static final Creator<SuraModel> CREATOR = new Creator<SuraModel>() {
        @Override
        public SuraModel createFromParcel(Parcel in) {
            return new SuraModel(in);
        }

        @Override
        public SuraModel[] newArray(int size) {
            return new SuraModel[size];
        }
    };

    public String get_index() {
        return _index;
    }

    public void set_index(String _index) {
        this._index = _index;
    }

    public String get_ayas() {
        return _ayas;
    }

    public void set_ayas(String _ayas) {
        this._ayas = _ayas;
    }

    public String get_start() {
        return _start;
    }

    public void set_start(String _start) {
        this._start = _start;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_tname() {
        return _tname;
    }

    public void set_tname(String _tname) {
        this._tname = _tname;
    }

    public String get_ename() {
        return _ename;
    }

    public void set_ename(String _ename) {
        this._ename = _ename;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_order() {
        return _order;
    }

    public void set_order(String _order) {
        this._order = _order;
    }

    public String get_rukus() {
        return _rukus;
    }

    public void set_rukus(String _rukus) {
        this._rukus = _rukus;
    }

    public String getAya_index() {
        return aya_index;
    }

    public void setAya_index(String aya_index) {
        this.aya_index = aya_index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_index);
        dest.writeString(_ayas);
        dest.writeString(_start);
        dest.writeString(_name);
        dest.writeString(_tname);
        dest.writeString(_ename);
        dest.writeString(_type);
        dest.writeString(_order);
        dest.writeString(_rukus);
        dest.writeString(aya_index);
    }


    //    protected SuraModel(Parcel in) {
//        _index = in.readString();
//        _ayas = in.readString();
//        _start = in.readString();
//        _name = in.readString();
//        _tname = in.readString();
//        _ename = in.readString();
//        _type = in.readString();
//        _order = in.readString();
//        _rukus = in.readString();
//    }
//
//    public static final Creator<SuraModel> CREATOR = new Creator<SuraModel>() {
//        @Override
//        public SuraModel createFromParcel(Parcel in) {
//            return new SuraModel(in);
//        }
//
//        @Override
//        public SuraModel[] newArray(int size) {
//            return new SuraModel[size];
//        }
//    };
//
//    public SuraModel() {
//
//    }
//
//    public String get_index() {
//        return _index;
//    }
//
//    public void set_index(String _index) {
//        this._index = _index;
//    }
//
//    public String get_ayas() {
//        return _ayas;
//    }
//
//    public void set_ayas(String _ayas) {
//        this._ayas = _ayas;
//    }
//
//    public String get_start() {
//        return _start;
//    }
//
//    public void set_start(String _start) {
//        this._start = _start;
//    }
//
//    public String get_name() {
//        return _name;
//    }
//
//    public void set_name(String _name) {
//        this._name = _name;
//    }
//
//    public String get_tname() {
//        return _tname;
//    }
//
//    public void set_tname(String _tname) {
//        this._tname = _tname;
//    }
//
//    public String get_ename() {
//        return _ename;
//    }
//
//    public void set_ename(String _ename) {
//        this._ename = _ename;
//    }
//
//    public String get_type() {
//        return _type;
//    }
//
//    public void set_type(String _type) {
//        this._type = _type;
//    }
//
//    public String get_order() {
//        return _order;
//    }
//
//    public void set_order(String _order) {
//        this._order = _order;
//    }
//
//    public String get_rukus() {
//        return _rukus;
//    }
//
//    public void set_rukus(String _rukus) {
//        this._rukus = _rukus;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this._ayas);
//        dest.writeString(this._ename);
//        dest.writeString(this._index);
//        dest.writeString(this._order);
//        dest.writeString(this._name);
//        dest.writeString(this._rukus);
//        dest.writeString(this._start);
//        dest.writeString(this._tname);
//        dest.writeString(this._type);
//    }
}

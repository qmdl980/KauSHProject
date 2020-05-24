package com.example.kaush;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
    String title;
    String date;
    String url;

    public MusicInfo(String title, String date, String url){
        this.title = title;
        this.date = date;
        this.url = url;
    }

    protected MusicInfo(Parcel in) {
        title = in.readString();
        date = in.readString();
        url = in.readString();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeString(url);
    }

}

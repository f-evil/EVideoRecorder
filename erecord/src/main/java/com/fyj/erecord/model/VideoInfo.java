package com.fyj.erecord.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 当前作者: Fyj<br>
 * 时间: 2017/5/2<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述:
 */

public class VideoInfo implements Parcelable {
    private String originVideoPath;
    private String videoPath;
    private String picPath;

    public VideoInfo() {
    }

    private VideoInfo(String originVideoPath, String videoPath, String picPath) {
        this.originVideoPath = originVideoPath;
        this.videoPath = videoPath;
        this.picPath = picPath;
    }

    public static VideoInfo getVideo(String originVideoPath, String videoPath, String picPath) {
        return new VideoInfo(originVideoPath, videoPath, picPath);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originVideoPath);
        dest.writeString(this.videoPath);
        dest.writeString(this.picPath);
    }

    protected VideoInfo(Parcel in) {
        this.originVideoPath = in.readString();
        this.videoPath = in.readString();
        this.picPath = in.readString();
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    public String getOriginVideoPath() {
        return originVideoPath;
    }

    public void setOriginVideoPath(String originVideoPath) {
        this.originVideoPath = originVideoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}

package com.fyj.videorecorder;

import android.app.Application;
import android.content.Context;

import com.fyj.videorecorder.global.CachePath;

import java.lang.ref.SoftReference;

import mabeijianxi.camera.VCamera;

/**
 * 当前作者: Fyj<br>
 * 时间: 2017/4/28<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述:
 */

public class VideoApp extends Application {

    private static SoftReference<Context> videoContext;

    @Override
    public void onCreate() {
        super.onCreate();
        videoContext = new SoftReference<>(getApplicationContext());
        CachePath.initDirName("videorecorder");
        String mediaCachePath = CachePath.getMediaCachePath(this);
        VCamera.setVideoCachePath(mediaCachePath);
        VCamera.setDebugMode(true);
        VCamera.initialize(this);
    }

    public static Context getApplication() {
//        if (videoContext == null) {
//            videoContext = new SoftReference<>(this);
//            return videoContext.get();
//        }
//        if (videoContext.get() == null) {
//            videoContext = new SoftReference<>(getApplicationContext());
//            return videoContext.get();
//        }
        return videoContext.get();
    }
}

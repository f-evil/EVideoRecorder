package com.fyj.videorecorder;

import android.app.Activity;
import android.media.CamcorderProfile;

import com.fyj.videorecorder.exception.NullAttachException;
import com.fyj.videorecorder.exception.NullProfileException;
import com.fyj.videorecorder.exception.NullRecordTimeException;
import com.fyj.videorecorder.util.XLog;

/**
 * 当前作者: Fyj<br>
 * 时间: 2017/4/28<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述:
 */

public class VideoConfig {

    /**
     * 请求拍视频请求码
     */
    public static final int REQUESR_RECORD_MEDIA = 112;

    public enum CompressMode {
        slow, medium, fast, faster, veryfast
    }

    /**
     * 录制时间
     */
    private int mRecordTime = -1;
    /**
     * 视频质量
     * 通过CamcorderProfile获得
     */
    private int mProfile = -1;

    /**
     * 是否压缩
     * 默认压缩
     */
    private boolean mIsCompress = true;

    /**
     * 压缩速度
     * 默认中等
     */
    private String mCompressMode;

    private VideoConfig() {

    }

    /**
     * 获得实例
     */
    public static VideoConfig get() {
        return new VideoConfig();
    }

    /**
     * 获得实例
     */
    public static VideoConfig getDefualt() {
        VideoConfig build = null;
        try {
            build = new VideoConfig()
                    .setTime(10 * 1000)
                    .setProfile(CamcorderProfile.QUALITY_480P)
                    .setCompress(true)
                    .setCompressMode(CompressMode.medium)
                    .build();
        } catch (NullRecordTimeException e) {
            e.printStackTrace();
        } catch (NullProfileException e) {
            e.printStackTrace();
        }
        return build;
    }

    /**
     * 设置录制时间
     *
     * @param time 录制时间
     * @return VideoConfig
     */
    public VideoConfig setTime(int time) {
        this.mRecordTime = time;
        return this;
    }

    /**
     * 视频质量
     * 通过CamcorderProfile.QUALITY_480P获得
     *
     * @param profile 视频质量
     * @return VideoConfig
     */
    public VideoConfig setProfile(int profile) {
        this.mProfile = profile;
        return this;
    }


    /**
     * 设置是否压缩文件
     *
     * @param compress 是否压缩
     * @return VideoConfig
     */
    public VideoConfig setCompress(boolean compress) {
        mIsCompress = compress;
        return this;
    }

    /**
     * 压缩模式
     *
     * @param compressMode 压缩模式
     * @return VideoConfig
     */
    public VideoConfig setCompressMode(CompressMode compressMode) {
        mCompressMode = compressMode.name();
        XLog.e("config", mCompressMode);
        XLog.e("config", compressMode.toString());
        return this;
    }

    /**
     * 请求开始录制视频
     *
     * @throws NullRecordTimeException 缺少录制时间异常
     * @throws NullAttachException     缺少回调Activity异常
     * @throws NullProfileException    缺少配置文件异常
     */
    public VideoConfig build() throws NullRecordTimeException, NullProfileException {
        return this;
    }

    /**
     * 请求开始录制视频
     */
    public VideoConfig start(Activity activity, int requestCode) {
        activity
                .startActivityForResult(
                        ERecorderActivityImpl
                                .getMediaIntent(
                                        activity,
                                        mRecordTime,
                                        mProfile,
                                        mIsCompress,
                                        mCompressMode
                                ),
                        requestCode
                );

        return this;

    }

    /**
     * 请求开始录制视频
     */
    public VideoConfig start(Activity activity) {
        start(activity, REQUESR_RECORD_MEDIA);
        return this;

    }


}

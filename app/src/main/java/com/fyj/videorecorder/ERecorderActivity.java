package com.fyj.videorecorder;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fyj.videorecorder.base.BaseAppCompatActivity;
import com.fyj.videorecorder.global.CachePath;
import com.fyj.videorecorder.util.XLog;
import com.fyj.videorecorder.view.TimeRoundProgressBar;

import butterknife.BindView;
import mabeijianxi.camera.model.OnlyCompressOverBean;

public class ERecorderActivity extends BaseAppCompatActivity implements SurfaceHolder.Callback {

    /**
     * surfaceholder
     */
    private SurfaceHolder mSurfaceHolder;
    /**
     * 录音
     */
    private MediaRecorder mRecorder;
    /**
     * 照相机
     */
    private Camera mCamera = null;
    /**
     * 原始文件路径
     */
    private String mOriginPath = "";
    /**
     * 录制时间
     */
    private int mRecordTime;
    /**
     * 视频质量
     * 通过CamcorderProfile.get()获得
     */
    private int mProfile;
    /**
     * 是否压缩
     * 默认压缩
     */
    private boolean mIsCompress;
    /**
     * 压缩速度
     * 默认中等
     */
    private String mCompressMode;
    /**
     * dialog
     */
    private MaterialDialog mDialog;

    @BindView(R.id.surfaceview)
    SurfaceView mSurfaceview;
    @BindView(R.id.iv_cancel)
    ImageView mIvCancel;
    @BindView(R.id.trpb_controller)
    TimeRoundProgressBar mTrpbController;
    @BindView(R.id.rl_take_vedio)
    RelativeLayout mRlTakeVedio;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.iv_confirm)
    ImageView mIvConfirm;
    @BindView(R.id.rl_confrm_vedio)
    RelativeLayout mRlConfrmVedio;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        releaseMediaRecorder();
        releaseCamera();
        super.onPause();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.view_bottom_enter, R.anim.view_bottom_exit);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_media_recorder;
    }

    @Override
    protected void destoryPre() {
        ERecorderActivityImpl.onDestory();
        mDialog = null;
    }

    @Override
    protected void initDate() {

        Intent intent = getIntent();

        if (intent == null) {
            XLog.e("ERecorder", "Illegal Operation!Please use ViewConfig.class.");
            finish();
            return;
        }

        mRecordTime = ERecorderActivityImpl.getRecordTime(intent);
        mProfile = ERecorderActivityImpl.getRecordProfile(intent);
        mIsCompress = ERecorderActivityImpl.getRecordIsCompress(intent);
        mCompressMode = ERecorderActivityImpl.getRecordCompressMode(intent);


    }

    @Override
    protected void initView() {

        mDialog = ERecorderActivityImpl.getCreateVedioDialog(getActivity());
        mTrpbController.setMax(mRecordTime);

        SurfaceHolder holder = mSurfaceview.getHolder();// 取得holder
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.setKeepScreenOn(true);
        holder.addCallback(this); // holder加入回调接口
    }

    @Override
    protected void getDate() {

    }

    @Override
    protected void initCustomFunction() {
        initCamera();
    }

    @Override
    protected void bindEvent() {

        mIvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIvDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ERecorderActivityImpl.scaleSmallAnim(mIvDelete);
                        break;

                    case MotionEvent.ACTION_UP:
                        ERecorderActivityImpl.scaleBigAnim(mIvDelete, new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                finish();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                }
                return true;
            }
        });

        mIvConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ERecorderActivityImpl.scaleSmallAnim(mIvConfirm);
                        break;

                    case MotionEvent.ACTION_UP:
                        ERecorderActivityImpl.scaleBigAnim(mIvConfirm, new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                judgeVideo();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        break;
                }
                return true;
            }
        });

        mTrpbController.setTimeListener(new TimeRoundProgressBar.OnTimeListener() {

            @Override
            public void onStart() {
                startRecord();
            }

            @Override
            public void onStop() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopRecord();
                        mRlTakeVedio.setVisibility(View.GONE);
                        mRlConfrmVedio.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    /**
     * 处理视频文件
     */
    private void judgeVideo() {
        mDialog.show();

        if (ERecorderActivityImpl.judgeFile(mOriginPath)) {
            mDialog.dismiss();
            Toast.makeText(getActivity(), "拍摄视频失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                String videoPath;
                String thumblePath;

                if (mIsCompress) {
                    OnlyCompressOverBean onlyCompressOverBean =
                            ERecorderActivityImpl
                                    .compressVideo(mOriginPath, mCompressMode);
                    videoPath = onlyCompressOverBean.getVideoPath();
                    thumblePath = onlyCompressOverBean.getPicPath();
                } else {
                    videoPath = mOriginPath;
                    thumblePath = ERecorderActivityImpl.getVideoThumble(mOriginPath);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                    }
                });

                ERecorderActivityImpl
                        .setResultAndFinish(
                                getActivity(),
                                videoPath,
                                thumblePath,
                                mOriginPath);

            }
        }).start();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            releaseCamera();
            finish();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaRecorder();
        releaseCamera();
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        mCamera = ERecorderActivityImpl.openCamera();

        if (mCamera == null) {
            Toast.makeText(getActivity(), "摄像头无法使用", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ERecorderActivityImpl.setCameraParam(mCamera);
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        releaseHolder();
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mCamera = null;
        }
    }

    /**
     * 释放holder
     */
    private void releaseHolder() {
        try {
            if (mSurfaceHolder != null) {
                mSurfaceHolder.removeCallback(this);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 释放MediaRecorder
     */
    private void releaseMediaRecorder() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {

        if (mRecorder == null) {
            mRecorder = new MediaRecorder(); // 创建MediaRecorder
        }
        if (mCamera != null) {
            mCamera.unlock();
            mRecorder.setCamera(mCamera);
        }

        try {
            // 设置音频采集方式
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            //设置视频的采集方式
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            mRecorder.setProfile(CamcorderProfile.get(mProfile));
//            mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));

            //设置记录会话的最大持续时间（毫秒）
            mRecorder.setMaxDuration(mRecordTime);
            mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mRecorder.setOrientationHint(90);
            String tpath = CachePath.getMediaCachePath(getActivity());
            tpath = tpath + System.currentTimeMillis() + ".mp4";
            //设置输出文件的路径
            mRecorder.setOutputFile(tpath);
            //准备录制
            mRecorder.prepare();
            //开始录制
            mRecorder.start();

            mOriginPath = tpath;

        } catch (Exception e) {
            finish();
        }
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        try {
            //停止录制
            mRecorder.stop();
            //重置
            mRecorder.reset();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mCamera != null) {
                mCamera.lock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

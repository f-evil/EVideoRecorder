package com.fyj.videorecorder;

import android.content.Intent;
import android.media.CamcorderProfile;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyj.videorecorder.base.BaseAppCompatActivity;
import com.fyj.videorecorder.exception.NullProfileException;
import com.fyj.videorecorder.exception.NullRecordTimeException;
import com.fyj.videorecorder.util.FileUtils;
import com.fyj.videorecorder.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.btn_start)
    Button mBtnStart;
    @BindView(R.id.tv_addr)
    TextView mTvAddr;
    @BindView(R.id.tv_pic_addr)
    TextView mTvPicAddr;
    @BindView(R.id.tv_ori_addr)
    TextView mTvOriAddr;
    @BindView(R.id.tv_ori_size)
    TextView mTvOriSize;
    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.tv_pic_title)
    TextView mTvPicTitle;
    @BindView(R.id.parent)
    ConstraintLayout mParent;

    private VideoConfig mVideoConfig;

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void destoryPre() {

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void getDate() {

    }

    @Override
    protected void initCustomFunction() {

    }

    @Override
    protected void bindEvent() {

    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        try {
            mVideoConfig = VideoConfig
                    .get()
                    .setTime(15 * 1000)
                    .setProfile(CamcorderProfile.QUALITY_480P)
                    .setCompress(true)
                    .setCompressMode(VideoConfig.CompressMode.fast)
                    .build();
        } catch (NullRecordTimeException e) {
            mVideoConfig.setTime(10 * 1000);
        } catch (NullProfileException e) {
            mVideoConfig.setProfile(CamcorderProfile.QUALITY_480P);
        } finally {
            mVideoConfig.start(this);
//            mVideoConfig.start(this,VideoConfig.REQUESR_RECORD_MEDIA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case VideoConfig.REQUESR_RECORD_MEDIA:
                String mVideoPath = ERecorderActivityImpl.getMediaPath(data);
                String mVideoThumblePath = ERecorderActivityImpl.getMediaThumblePath(data);
                String mVideoOriginPath = ERecorderActivityImpl.getMediaOriginPath(data);
                if (StringUtil.isEmpty(mVideoPath)
                        || StringUtil.isEmpty(mVideoThumblePath)) {
                    Toast.makeText(getActivity(), "视频拍摄失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                mTvOriAddr.setText(mVideoOriginPath);
                mTvOriSize.setText(FileUtils.getAutoFileOrFilesSize(mVideoOriginPath));
                mTvAddr.setText(mVideoPath);
                mTvSize.setText(FileUtils.getAutoFileOrFilesSize(mVideoPath));
                mTvPicAddr.setText(mVideoThumblePath);
                break;
        }
    }

}

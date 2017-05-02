package com.fyj.videorecorder;

import android.content.Intent;
import android.media.CamcorderProfile;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyj.erecord.ERecorderActivityImpl;
import com.fyj.erecord.VideoConfig;
import com.fyj.erecord.exception.NullProfileException;
import com.fyj.erecord.exception.NullRecordTimeException;
import com.fyj.erecord.model.VideoInfo;
import com.fyj.erecord.util.FileUtils;
import com.fyj.erecord.util.StringUtil;
import com.fyj.videorecorder.base.BaseAppCompatActivity;

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
                    .check();
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

                VideoInfo vedioInfo = ERecorderActivityImpl.getVedioInfo(data);

                if (vedioInfo == null) {
                    Toast.makeText(getActivity(), "视频拍摄失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                //压缩后视频地址
                String mVideoPath = vedioInfo.getVideoPath();
                //视频截图
                String mVideoThumblePath = vedioInfo.getPicPath();
                //视频原地址
                String mVideoOriginPath = vedioInfo.getOriginVideoPath();

                if (StringUtil.isEmpty(mVideoPath)
                        || StringUtil.isEmpty(mVideoOriginPath)) {
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

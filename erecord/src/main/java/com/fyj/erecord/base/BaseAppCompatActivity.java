package com.fyj.erecord.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fyj.erecord.util.XLog;

/**
 * 当前作者: Fyj<br>
 * 时间: 2016/8/24<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述:
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setLayout());
        getIntentData();
        initDate();
        initView();
        getDate();
        initCustomFunction();
        bindEvent();
        changeSystembar();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        XLog.e("Showing Activity Name:", TAG);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destoryPre();
    }

    @Override
    public void finish() {
        super.finish();
    }

    protected abstract int setLayout();

    protected abstract void destoryPre();

    protected abstract void initDate();

    protected abstract void getDate();

    protected abstract void initCustomFunction();

    protected abstract void bindEvent();

    protected void initView() {

    }

    protected void getIntentData() {

    }

    private void changeSystembar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleColor();
        }
    }

    protected void titleColor() {

    }

    protected Activity getActivity() {
        return this;
    }
}

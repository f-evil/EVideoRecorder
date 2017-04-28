package com.fyj.videorecorder.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fyj.videorecorder.R;

/**
 * 当前作者: Fyj<br>
 * 时间: 2017/4/13<br>
 * 邮箱: f279259625@gmail.com<br>
 * 修改次数: <br>
 * 描述:
 */

public class TimeRoundProgressBar extends View {

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 秒数
     */
    private String seconds = "0秒";

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    private OnTimeListener mTimeListener;

    private boolean isRunning = false;

    /**
     * 文字高度
     */
    private float mTextHeight;

    public TimeRoundProgressBar(Context context) {
        super(context);
        initView(context, null);
    }

    public TimeRoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TimeRoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundTimeProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundTimeProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundTimeProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundTimeProgressBar_timeTextColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundTimeProgressBar_timeTextSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundTimeProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundTimeProgressBar_maxTime, 100);
        style = mTypedArray.getInt(R.styleable.RoundTimeProgressBar_roundStyle, 0);

        mTypedArray.recycle();

        paint.setTextSize(textSize);

        Paint.FontMetrics fm = paint.getFontMetrics();
        mTextHeight = (float) Math.ceil(fm.descent - fm.ascent);
        Log.e("mTextHeight", mTextHeight + "");
    }

    RectF oval;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerWidth = getWidth() / 2;

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT);

        float textwidth = paint.measureText(seconds);

        canvas.drawText(seconds, centerWidth - textwidth / 2, mTextHeight / 3 + mTextHeight / 2, paint);

        int radiu = (int) (centerWidth - roundWidth / 2);
        int centerHeight = (int) (centerWidth + mTextHeight + mTextHeight / 3 * 2);

        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(centerWidth, centerHeight, radiu, paint);

        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(roundProgressColor);
        paint.setAntiAlias(true);
        canvas.drawCircle(centerWidth, centerHeight, (float) (radiu * 0.7), paint);

        paint.setStrokeWidth(roundWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(roundProgressColor);
        if (oval == null) {
            oval = new RectF(
                    centerWidth - radiu,
                    mTextHeight / 3 * 2 + mTextHeight + centerWidth - radiu,
                    centerWidth + radiu,
                    mTextHeight / 3 * 2 + mTextHeight + centerWidth + radiu);
//            oval = new RectF(textwidth - radiu, centerHeight - radiu, textwidth + radiu, centerHeight + radiu);
        }

        float percent = (float) progress / (float) max;
        Log.e("xxxxx", progress + "");
        Log.e("xxxxx", (int) (360 * percent) + "");

        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, -90, 0 + 360 * percent, false, paint);
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL);
                canvas.drawArc(oval, -90, 0 + 360 * percent, true, paint);
                break;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (width + mTextHeight / 3 * 2 + mTextHeight + 10);
        }

        setMeasuredDimension(width, height);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTimeListener != null) {
                    mTimeListener.onStart();
                    isRunning = true;
                    startTime();
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mTimeListener != null && isRunning) {
                    mTimeListener.onStop();
                    isRunning = false;
                }
                break;
        }

        return true;
    }

    public interface OnTimeListener {
        void onStart();

        void onStop();
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }


    private void startTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 1;
                while (i <= max && isRunning) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress(i);
                    seconds = i + "秒";
                    i++;
                    if (i == max + 1) {
                        isRunning = false;
                        if (mTimeListener != null) {
                            mTimeListener.onStop();
                        }
                    }
                }
            }
        }).start();
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }


    public OnTimeListener getTimeListener() {
        return mTimeListener;
    }

    public void setTimeListener(OnTimeListener timeListener) {
        mTimeListener = timeListener;
    }
}

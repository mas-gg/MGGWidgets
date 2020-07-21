package com.mgg.demo.mggwidgets.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 渐变控件
 */

public class RippleView extends View {

    //假背景
    private Bitmap mBackground;
    PaintFlagsDrawFilter pfd;
    private Paint mPaint;
    //最大半径长度
    private int mMaxRadius;
    //开始半径长度
    private int mStartRadius;
    //当前半径长度
    private int mCurrentRadius;
    //动画是否已经开始
    private boolean isStarted = false;
    //运动时长
    private long mDuration = 300;
    //扩散的起点
    private float mStartX;
    private float mStartY;
    //父控件
    private ViewGroup mViewGroup;

    private ValueAnimator mRippleAnimator;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        //设置为擦除模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }


    /**
     * 开始渐变
     */
    public void startRipple(ViewGroup viewGroup) {
        mViewGroup = viewGroup;

        mStartX = 0;
        mStartY = viewGroup.getHeight();
        mStartRadius = 0;
        //计算最大的扩展半径
        calculateMaxRadius();

        if (!isStarted) {
            isStarted = true;
            updateBackground();
            mViewGroup.addView(this, viewGroup.getWidth(), viewGroup.getHeight());
            startAnimation();
        }
    }

    public void stopRipple() {
        if (mRippleAnimator != null && mRippleAnimator.isRunning()) {
            mRippleAnimator.cancel();
        }
        if (mViewGroup != null) {
            try {
                mViewGroup.removeView(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //开始动画
    private void startAnimation() {
        mRippleAnimator = ValueAnimator.ofFloat(mStartRadius, mMaxRadius)
                .setDuration(mDuration);
        mRippleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isStarted = false;
                recycleBackground();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isStarted = false;
                //动画播放完毕
                recycleBackground();
                mViewGroup.removeView(RippleView.this);
            }
        });
        mRippleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新圆的半径
                mCurrentRadius = (int) (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mRippleAnimator.start();
    }

    //获取到最大的半径
    private void calculateMaxRadius() {
        mMaxRadius = (int) Math.hypot(mViewGroup.getWidth(), mViewGroup.getHeight());
    }

    public RippleView setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    /**
     * 更新屏幕截图
     */
    private void updateBackground() {
        recycleBackground();
        mViewGroup.setDrawingCacheEnabled(true);
        mBackground = mViewGroup.getDrawingCache();
        mBackground = Bitmap.createBitmap(mBackground);
        mViewGroup.setDrawingCacheEnabled(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在新的图层上面绘制
        int layer = canvas.save();
        canvas.setDrawFilter(pfd);
        canvas.drawBitmap(mBackground, 0, 0, null);
        canvas.drawCircle(mStartX, mStartY, mCurrentRadius, mPaint);
        canvas.restoreToCount(layer);
    }

    private void recycleBackground() {
        if (mBackground != null && !mBackground.isRecycled()) {
            mBackground.recycle();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mRippleAnimator != null && mRippleAnimator.isRunning()) {
            mRippleAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //消费掉事件
        return true;
    }
}

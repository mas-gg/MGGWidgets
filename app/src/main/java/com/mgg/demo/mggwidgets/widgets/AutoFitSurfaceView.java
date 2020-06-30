package com.mgg.demo.mggwidgets.widgets;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * created by mgg
 * 2020/6/28
 */
public class AutoFitSurfaceView extends SurfaceView {
    public static final int SCALE_TYPE_CENTER_CROP = 0;//充满布局，垂直或水平方向会有裁剪
    public static final int SCALE_TYPE_FIT_CENTER = 1;//不充满布局，垂直或水平方向会有黑边
    private int scaleType = SCALE_TYPE_FIT_CENTER;
    private float aspectRatio = 0f;

    public AutoFitSurfaceView(Context context) {
        super(context);
    }

    public AutoFitSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAspectRatio(int width, int height) {
        if (width > 0 && height > 0) {
            aspectRatio = ((float) height) / width;
            getHolder().setFixedSize(width, height);
            requestLayout();
        } else {
            throw new IllegalArgumentException("Size cannot be negative");
        }
    }

    public void setScaleType(@IntRange(from = SCALE_TYPE_CENTER_CROP, to = SCALE_TYPE_FIT_CENTER) int scaleType) {
        this.scaleType = scaleType;
        requestLayout();
    }

    public int getScaleType() {
        return scaleType;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aspectRatio != 0f) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            switch (scaleType) {
                case SCALE_TYPE_CENTER_CROP:
                    width = Math.round(Math.max(width, height * aspectRatio));
                    height = Math.round(Math.max(height, width / aspectRatio));
                    break;
                case SCALE_TYPE_FIT_CENTER:
                    width = Math.round(Math.min(width, height * aspectRatio));
                    height = Math.round(Math.min(height, width / aspectRatio));
                    break;
            }
            setMeasuredDimension(width, height);
        }
    }
}

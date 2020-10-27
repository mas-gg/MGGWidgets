package com.mgg.demo.mggwidgets.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.bean.DimpleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * created by mgg
 * 2020/10/20
 */
public class DimpleView extends View {
    private static final String TAG = "DimpleView";

    private List<DimpleItem> mItems;
    private Paint mPaint;
    private Path mPath;
    private Random random;
    private Bitmap mBitmap;
    private PathMeasure mPathMeasure;
    private float mLength;
    private float mDistance;
    private float[] pos;
    private float[] tan;

    private int centerX;
    private int centerY;
    private int radius;
    private int maxDistance;

    public DimpleView(Context context) {
        this(context, null);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DimpleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mItems = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        random = new Random();
        mPath = new Path();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_yellow_star);
        pos = new float[2];
        tan = new float[2];
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged: " + w + " " + h);
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 4;
        maxDistance = (int) (radius * 1.8f);

        for (int i = 0; i < 1000; i++) {
            mItems.add(initItem(new DimpleItem()));
        }

        mPath.addCircle(w / 2, h / 2, radius, Path.Direction.CW);

        mPathMeasure = new PathMeasure(mPath, false);
        mLength = mPathMeasure.getLength();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, mLength)
                .setDuration(1500);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDistance = (float) animation.getAnimatedValue();
                updateItems();
            }
        });
        animator.start();
    }

    void updateItems() {
        for (DimpleItem item : mItems) {
            if (changeItem(item)) {
                resetItem(item);
            }
        }
        invalidate();
    }

    DimpleItem initItem(DimpleItem item) {
        item.distance = random.nextInt(maxDistance - radius) + radius;
        item.degrees = random.nextDouble() * 360;
        calculateItem(item);
        item.speed = random.nextInt(2) + 2;
        item.alpha = 255;
        return item;
    }

    boolean changeItem(DimpleItem item) {
        item.distance += item.speed;
        calculateItem(item);
        item.alpha = (int) (255f / (maxDistance - radius) * (maxDistance - item.distance));
        return item.distance > maxDistance;
    }

    DimpleItem resetItem(DimpleItem item) {
        item.distance = random.nextInt(5) + radius;
        item.degrees = random.nextDouble() * 360;
        calculateItem(item);
        item.speed = random.nextInt(2) + 2;
        item.alpha = 255;
        return item;
    }

    /**
     * 根据角度和移动半径计算坐标
     *
     * @param item
     */
    void calculateItem(DimpleItem item) {
        item.x = centerX + (int) (item.distance * Math.cos(item.degrees));
        item.y = centerY - (int) (item.distance * Math.sin(item.degrees));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        for (DimpleItem item : mItems) {
            mPaint.setAlpha(item.alpha);
            canvas.drawCircle(item.x, item.y, item.radius, mPaint);
        }

        //图片沿着path移动
        mPaint.setAlpha(255);
        mPathMeasure.getPosTan(mDistance, pos, tan);
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        canvas.rotate(degrees, pos[0], pos[1]);
//        canvas.drawBitmap(mBitmap, pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight(), mPaint);
    }
}

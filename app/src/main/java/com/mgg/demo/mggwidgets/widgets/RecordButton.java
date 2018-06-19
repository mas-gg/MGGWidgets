package com.mgg.demo.mggwidgets.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.util.DensityUtils;

/**
 * Created by mgg on 2018/6/11.
 */

public class RecordButton extends View {
    private static String TAG = "RecordButton";
    private int DURATION;
    private int dp_ring_side;
    private int dp_between_ring_circle;
    private int dp_square_radius;
    private int dp_square_side;
    private int color_ring;
    private int color_circle;

    private Paint paintRing;
    private Paint paintCircle;
    private RectF ring;
    private RectF circle;
    int measuredWidth;

    float maxRadius;
    float minRadius;
    float maxLeft;
    float minLeft;
    float radiusRange;
    float leftRange;
    float radius;

    boolean isRecording;
    boolean isAnimating;

    public RecordButton(Context context) {
        super(context);
        init(context,null);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public RecordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(final Context context, final AttributeSet attrs) {
        if (attrs==null){
            dp_ring_side = DensityUtils.dp2px(context, 4);
            dp_between_ring_circle =DensityUtils.dp2px(context, 3);
            dp_square_radius = DensityUtils.dp2px(context, 4);
            dp_square_side = DensityUtils.dp2px(context, 22);
            color_ring = getResources().getColor(R.color.color_white);
            color_circle = getResources().getColor(R.color.color_00edd9);
            DURATION = 250;
        }else {
            TypedArray typedArray = null;
            try {
                typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButton);
                dp_ring_side = (int) typedArray.getDimension(R.styleable.RecordButton_ring_side, DensityUtils.dp2px(context, 4));
                dp_between_ring_circle =(int) typedArray.getDimension(R.styleable.RecordButton_between_ring_circle, DensityUtils.dp2px(context, 3));
                dp_square_radius = (int) typedArray.getDimension(R.styleable.RecordButton_square_radius, DensityUtils.dp2px(context, 4));
                dp_square_side = (int) typedArray.getDimension(R.styleable.RecordButton_square_side, DensityUtils.dp2px(context, 22));
                color_ring = typedArray.getColor(R.styleable.RecordButton_ring_color,getResources().getColor(R.color.color_white));
                color_circle = typedArray.getColor(R.styleable.RecordButton_circle_color,getResources().getColor(R.color.color_00edd9));
                DURATION = typedArray.getInt(R.styleable.RecordButton_duration,250);
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }
        }

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setColor(color_circle);

        paintRing = new Paint();
        paintRing.setAntiAlias(true);
        paintRing.setColor(color_ring);
        paintRing.setStrokeWidth(dp_ring_side);
        paintRing.setStyle(Paint.Style.STROKE);

        ring = new RectF();
        circle = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();

        ring.left = dp_ring_side / 2;
        ring.top = dp_ring_side / 2;
        ring.right = measuredWidth - dp_ring_side / 2;
        ring.bottom = measuredWidth - dp_ring_side / 2;

        maxRadius=measuredWidth/2-(dp_ring_side+ dp_between_ring_circle);
        minRadius=dp_square_radius;
        radiusRange=maxRadius-minRadius;

        maxLeft=(measuredWidth- dp_square_side)/2;
        minLeft=dp_ring_side+ dp_between_ring_circle;
        leftRange=maxLeft-minLeft;

        circle.left = minLeft;
        circle.top = circle.left;
        circle.right = measuredWidth - circle.left;
        circle.bottom = measuredWidth - circle.left;
        radius=maxRadius;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(ring, 0, 360, false, paintRing);
        canvas.drawRoundRect(circle, radius, radius, paintCircle);
    }

    public void setRecording(boolean begin){
        if (begin!=isRecording && !isAnimating){
            Animation animation=new ScaleAnimation(1,1,1,1);
            animation.setDuration(DURATION);
            if (isRecording) {
                animation.setInterpolator(new LinearInterpolator(){
                    @Override
                    public float getInterpolation(float input) {
                        Log.d(TAG, "toLarge: "+input);
                        toLarge(input);
                        return super.getInterpolation(input);
                    }
                });
            } else {
                animation.setInterpolator(new LinearInterpolator(){
                    @Override
                    public float getInterpolation(float input) {
                        Log.d(TAG, "toSmall: "+input);
                        toSmall(input);
                        return super.getInterpolation(input);
                    }
                });
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimating=true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimating=false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            isRecording = !isRecording;
            startAnimation(animation);
        }

    }

    public boolean getIsRecording(){
        return isRecording;
    }

    void toSmall(float input){
        circle.left = minLeft+input*leftRange;
        circle.top = circle.left;
        circle.right = measuredWidth - circle.left;
        circle.bottom = circle.right;

        radius = maxRadius-input*radiusRange;
        invalidate();
    }

    void toLarge(float input){
        circle.left = maxLeft-input*leftRange;
        circle.top = circle.left;
        circle.right = measuredWidth - circle.left;
        circle.bottom = circle.right;

        radius = minRadius+input*radiusRange;
        invalidate();
    }

}

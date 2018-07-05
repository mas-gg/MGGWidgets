package com.mgg.demo.mggwidgets.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.util.DensityUtils;

/**
 * Created by mgg on 2018/7/5.
 */

public class RoundImageView extends ImageView{
    private static final int MODE_ROUND=1;
    private static final int MODE_CIRCLE=2;
    private static final int MODE_DEFAULT=0;

    private Paint mPaint;
    private int currMode;
    private int currRound=8;


    public RoundImageView(Context context) {
        super(context);
        initViews(context,null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }
    private void initViews(final Context context, final AttributeSet attrs) {
        if(attrs==null)return;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        currMode=typedArray.getInt(R.styleable.RoundImageView_mode,MODE_DEFAULT);
        currRound= (int) typedArray.getDimension(R.styleable.RoundImageView_round,DensityUtils.dp2px(getContext(),currRound));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (currMode==MODE_CIRCLE){
            //强制使宽高一致，否则宽高差很多的时候，圆形会居中显示，很难看
            int newWidthMeasureSpec=Math.min(widthMeasureSpec, heightMeasureSpec);
            super.onMeasure(newWidthMeasureSpec, newWidthMeasureSpec);
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        if (getImageMatrix() == null && getPaddingTop() == 0 && getPaddingLeft() == 0) {
            getDrawable().draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            if (getCropToPadding()) {
                final int scrollX = getScrollX();
                final int scrollY = getScrollY();
                canvas.clipRect(scrollX + getPaddingLeft(), scrollY + getPaddingTop(),
                        scrollX + getRight() - getLeft() - getPaddingRight(),
                        scrollY + getBottom() - getTop() - getPaddingBottom());
            }
            canvas.translate(getPaddingLeft(), getPaddingTop());

            //判断绘制类型
            if (currMode==MODE_ROUND){
                //圆角
                Bitmap bitmap = drawable2Bitmap(getDrawable());
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect(new RectF(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom()),
                        currRound, currRound, mPaint);
            }else if (currMode==MODE_CIRCLE){
                //圆形
                Bitmap bitmap = drawable2Bitmap(getDrawable());
                mPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, (Math.min(getWidth(),getHeight())) / 2, mPaint);
            }else {
                //默认，即当作普通ImageView
                if (getImageMatrix() != null) {
                    canvas.concat(getImageMatrix());
                }
                getDrawable().draw(canvas);
            }

            canvas.restoreToCount(saveCount);
        }
    }

    /**
     * drawable转换成bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //根据传递的scaleType获取matrix对象，设置给bitmap
        Matrix matrix = getImageMatrix();
        if (matrix != null) {
            canvas.concat(matrix);
        }
        drawable.draw(canvas);
        return bitmap;
    }
}

package com.mgg.demo.mggwidgets.view.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class ThrowEggsView extends ImageView {
    // x轴坐标
    public static final int X_TYPE = 1;
    // y轴坐标
    public static final int Y_TYPE = 2;

    // 旋转动画
    private ObjectAnimator mRotateAnimator;
    // 移动动画
    private ObjectAnimator mTranslateAnimator;
    // 动画集合
    private AnimatorSet mAnimatorSet;

    private ThrowEggAnimatorListener mListener;

    public ThrowEggsView(Context context) {
        this(context, null, 0);
    }

    public ThrowEggsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThrowEggsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(ThrowEggAnimatorListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置坐标
     *
     * @param position
     */
    public void setPosition(PointF position) {
        if (position == null) {
            return;
        }

        setTranslationX(position.x);
        setTranslationY(position.y);
    }

    /**
     * @param startPoint 起始点 画布左上角坐标
     * @param endPoint
     * @param type       扔的类型，是鸡蛋还是花花
     */
    public void start(PointF startPoint, PointF endPoint, final int type) {
        setPosition(startPoint);
        mAnimatorSet = new AnimatorSet();

        //向右扔顺时针旋转360度，向左扔逆时针旋转360度
        mRotateAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, endPoint.x > startPoint.x ? 360 : -360);

        //通过调用setPosition 改变position属性
        mTranslateAnimator = ObjectAnimator.ofObject(this,
                "position",
                new BezierEvaluator(startPoint, endPoint),
                startPoint,
                endPoint);

        mAnimatorSet.playTogether(mRotateAnimator, mTranslateAnimator);
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.throwEggAnimEnd(type);
                    mListener = null;
                }
                ViewGroup parent = (ViewGroup) ThrowEggsView.this.getParent();
                if (parent != null && parent instanceof ViewGroup) {
                    parent.removeView(ThrowEggsView.this);
                }

            }
        });

        mAnimatorSet.start();
    }

    /**
     * 动画回调
     */
    public interface ThrowEggAnimatorListener {
        void throwEggAnimEnd(int type);
    }

    private static class BezierEvaluator implements TypeEvaluator<PointF> {

        private final List<PointF> pointList;

        public BezierEvaluator(PointF startPoint, PointF endPoint) {
            this.pointList = new ArrayList<>();

            //一个控制点，2阶贝塞尔曲线，保证上抛下抛时，曲线都向上弯
            PointF controlPointF;
            if (startPoint.y > endPoint.y) {
                controlPointF = new PointF(startPoint.x, endPoint.y);
            } else {
                controlPointF = new PointF(endPoint.x, startPoint.y);
            }

            pointList.add(startPoint);
            pointList.add(controlPointF);
            pointList.add(endPoint);

        }

        @Override
        public PointF evaluate(float fraction, PointF startPoint, PointF endPoint) {
            return new PointF(calculatePointCoordinate(X_TYPE, fraction, 2, 0, pointList),
                    calculatePointCoordinate(Y_TYPE, fraction, 2, 0, pointList));
        }
    }

    /**
     * 计算坐标 [贝塞尔曲线的核心关键]
     *
     * @param type             {@link #X_TYPE} 表示x轴的坐标， {@link #Y_TYPE} 表示y轴的坐标
     * @param u                当前的比例
     * @param k                阶数
     * @param p                当前坐标（具体为 x轴 或 y轴）
     * @param controlPointList 控制点的坐标
     * @return
     */
    private static float calculatePointCoordinate(@IntRange(from = X_TYPE, to = Y_TYPE) int type,
                                                  float u,
                                                  int k,
                                                  int p,
                                                  List<PointF> controlPointList) {

        if (k == 1) {

            float p1;
            float p2;

            // 根据是 x轴 还是 y轴 进行赋值
            if (type == X_TYPE) {
                p1 = controlPointList.get(p).x;
                p2 = controlPointList.get(p + 1).x;
            } else {
                p1 = controlPointList.get(p).y;
                p2 = controlPointList.get(p + 1).y;
            }

            return (1 - u) * p1 + u * p2;

        } else {
            return (1 - u) * calculatePointCoordinate(type, u, k - 1, p, controlPointList)
                    + u * calculatePointCoordinate(type, u, k - 1, p + 1, controlPointList);

        }

    }

}
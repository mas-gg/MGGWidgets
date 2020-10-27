package com.mgg.demo.mggwidgets.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * created by mgg
 * 2020/7/21
 */
class CommonNavigationItemView extends RelativeLayout {

    long toSmallDuration = 70;
    long toNormalDuration = 200;
    ImageView imageView;
    RippleView rippleView;
    int normalResId;
    int selectedResId;
    boolean tabSelected;
    boolean isAnimating;
    ViewPropertyAnimator propertyAnimator;

    public CommonNavigationItemView(Context context) {
        this(context, null);
    }

    public CommonNavigationItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonNavigationItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        imageView = new ImageView(context);
        rippleView = new RippleView(getContext());
        rippleView.setDuration(toNormalDuration);
        addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundColor(Color.WHITE);
    }

    public boolean isTabSelected() {
        return tabSelected;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setTabSelected(boolean selected, boolean animate) {
        if (this.tabSelected == selected) return;
        this.tabSelected = selected;
        if (isAnimating) {
            rippleView.stopRipple();
            propertyAnimator.cancel();
            setTabResId(normalResId, selectedResId);
            isAnimating = false;
            return;
        }
        if (animate) {
            if (selected) {
                select();
            } else {
                unSelect();
            }
        } else {
            setTabResId(normalResId, selectedResId);
        }

    }

    public void setTabResId(int normalResId, int selectedResId) {
        this.normalResId = normalResId;
        this.selectedResId = selectedResId;
        imageView.setImageResource(tabSelected ? selectedResId : normalResId);
    }

    private void select() {
        isAnimating = true;
        propertyAnimator = animate().scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(toSmallDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        CommonNavigationItemView.this.setScaleX(1f);
                        CommonNavigationItemView.this.setScaleY(1f);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rippleView.startRipple(CommonNavigationItemView.this);
                        imageView.setImageResource(selectedResId);
                        propertyAnimator = CommonNavigationItemView.this.animate().scaleX(1f)
                                .scaleY(1f)
                                .setDuration(toNormalDuration)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        CommonNavigationItemView.this.setScaleX(1f);
                                        CommonNavigationItemView.this.setScaleY(1f);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        isAnimating = false;
                                    }
                                });
                        propertyAnimator.start();
                    }
                });
        propertyAnimator.start();
    }

    private void unSelect() {
        imageView.setImageResource(normalResId);
    }
}

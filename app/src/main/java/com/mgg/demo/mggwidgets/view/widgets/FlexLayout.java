package com.mgg.demo.mggwidgets.view.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 仿flex box流式布局
 * created by mgg
 * 2020/10/29
 */
public class FlexLayout extends ViewGroup {
    private static final String TAG = "FlexLayout";

    public FlexLayout(Context context) {
        this(context, null);
    }

    public FlexLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (measuredWidth > 0 && MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {

            measuredHeight = 0;
            int leftWidth = measuredWidth - getPaddingLeft() - getPaddingRight();//当前行剩余宽度
            int currentLineHeight = 0;//当前行最大高度
            int currentItemWidth;//当前child宽度
            int currentItemHeight;//当前child高度
            int childCount = getChildCount();
            View child;
            ViewGroup.MarginLayoutParams layoutParams;
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                layoutParams = (MarginLayoutParams) child.getLayoutParams();
                currentItemWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                currentItemHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

                //此时需要新起一行，所以把当前行的相关数据重置
                if (currentItemWidth > leftWidth) {
                    measuredHeight += currentLineHeight;//先记录上一行的高度
                    leftWidth = measuredWidth - getPaddingLeft() - getPaddingRight();
                    currentLineHeight = 0;
                }

                leftWidth -= currentItemWidth;
                currentLineHeight = Math.max(currentLineHeight, currentItemHeight);
            }
            measuredHeight += currentLineHeight;//记录最后一行的高度
            measuredHeight += getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int parentWidth = r - l;
        int leftWidth = parentWidth - getPaddingLeft() - getPaddingRight();//当前行剩余宽度
        int currentLineHeight = 0;//当前行最大高度
        int totalHeight = getPaddingTop();
        int totalWidth = getPaddingLeft();
        int currentItemWidth, currentItemHeight;//当前child宽度、高度
        int left, top, right, bottom;

        int childCount = getChildCount();
        View child;
        ViewGroup.MarginLayoutParams layoutParams;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            layoutParams = (MarginLayoutParams) child.getLayoutParams();
            currentItemWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            currentItemHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            //此时需要新起一行，所以把当前行的相关数据重置
            if (currentItemWidth > leftWidth) {
                totalHeight += currentLineHeight;
                leftWidth = parentWidth - getPaddingLeft() - getPaddingRight();
                currentLineHeight = 0;
                totalWidth = getPaddingLeft();
            }

            leftWidth -= currentItemWidth;
            currentLineHeight = Math.max(currentLineHeight, currentItemHeight);

            left = totalWidth + layoutParams.leftMargin;
            top = totalHeight + layoutParams.topMargin;
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);

            totalWidth = right + layoutParams.rightMargin;
        }
    }

}

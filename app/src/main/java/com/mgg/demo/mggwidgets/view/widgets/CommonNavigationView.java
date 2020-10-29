package com.mgg.demo.mggwidgets.view.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mgg.demo.mggwidgets.util.DensityUtils;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.CENTER_HORIZONTAL;

/**
 * created by mgg
 * 2020/7/21
 */
public class CommonNavigationView extends LinearLayout {
    OnTabSelectedListener tabSelectedListener;
    int curSelected = -1;
    SparseArray<RelativeLayout> tabs = new SparseArray<>();

    public CommonNavigationView(Context context) {
        this(context, null);
    }

    public CommonNavigationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTab(int normalResId, int selectedResId, String name) {
        int index = tabs.size();
        tabs.put(index, addTabView(index, normalResId, selectedResId, name));
    }

    public void selectTab(int pos, boolean animate) {
        if (curSelected == pos) return;
        RelativeLayout relativeLayout = tabs.get(pos);
        if (relativeLayout != null) {
            RelativeLayout last = tabs.get(curSelected);
            if (last != null) {
                unSelect(last, animate);
            }
            select(relativeLayout, animate);
            curSelected = pos;
        }
    }

    public void setTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }

    private RelativeLayout addTabView(final int index, int normalResId, int selectedResId, String name) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        final CommonNavigationItemView itemView = new CommonNavigationItemView(getContext());
        itemView.setTabResId(normalResId, selectedResId);
        itemView.setTag("tab");
        relativeLayout.addView(itemView, DensityUtils.dp2px(getContext(), 20), DensityUtils.dp2px(getContext(), 20));
        RelativeLayout.LayoutParams itemLayoutParams = (RelativeLayout.LayoutParams) itemView.getLayoutParams();
        itemLayoutParams.topMargin = DensityUtils.dp2px(getContext(), 7);
        itemLayoutParams.addRule(CENTER_HORIZONTAL);

        TextView textView = new TextView(getContext());
        textView.setTag("text");
        textView.setText(name);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,9);
        textView.setTextColor(Color.parseColor("#333333"));
        relativeLayout.addView(textView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams textLayoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        textLayoutParams.bottomMargin = DensityUtils.dp2px(getContext(), 1f);
        textLayoutParams.addRule(CENTER_HORIZONTAL);
        textLayoutParams.addRule(ALIGN_PARENT_BOTTOM);

        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(index, true);
                if (tabSelectedListener != null) {
                    tabSelectedListener.onSelected(index);
                }
            }
        });

        addView(relativeLayout, layoutParams);
        return relativeLayout;
    }


    private void select(RelativeLayout relativeLayout, boolean animate) {
        CommonNavigationItemView itemView = relativeLayout.findViewWithTag("tab");
        TextView textView = relativeLayout.findViewWithTag("text");
        itemView.setTabSelected(true, animate);
        textView.setTextColor(Color.parseColor("#6d20ff"));
    }

    private void unSelect(RelativeLayout relativeLayout, boolean animate) {
        CommonNavigationItemView itemView = relativeLayout.findViewWithTag("tab");
        TextView textView = relativeLayout.findViewWithTag("text");
        itemView.setTabSelected(false, animate);
        textView.setTextColor(Color.parseColor("#333333"));
    }

    public void clearTabs() {
        removeAllViews();
    }

    public interface OnTabSelectedListener {
        void onSelected(int pos);
    }
}

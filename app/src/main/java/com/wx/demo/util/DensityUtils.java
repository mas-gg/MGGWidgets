package com.wx.demo.util;

import android.content.Context;
import android.util.TypedValue;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by wx on 2018/6/15.
 * 像素密度-dp、sp与px转换
 */

public class DensityUtils {
    private DensityUtils() {
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spVal) {
        return (int)TypedValue.applyDimension(COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float pxVal) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale;
    }

    public static float px2sp(Context context, float pxVal) {
        return pxVal / context.getResources().getDisplayMetrics().scaledDensity;
    }
}

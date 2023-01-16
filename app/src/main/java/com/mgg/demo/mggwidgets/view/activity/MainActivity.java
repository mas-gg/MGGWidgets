package com.mgg.demo.mggwidgets.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mgg.demo.mggwidgets.BaseActivity;
import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private static final String TAG = "MainActivity";

    public void goRecordButtonActivity(View view) {
        startActivity(new Intent(MainActivity.this, RecordButtonActivity.class));
    }

    public void goRoundImageViewActivity(View view) {
        startActivity(new Intent(MainActivity.this, RoundImageViewActivity.class));
    }

    public void goSvgActivity(View view) {
        startActivity(new Intent(MainActivity.this, SvgActivity.class));
    }

    public void goCalendarActivity(View view) {
        CalendarActivity.start(this);
    }

    public void goLiveActivity(View view) {
        LiveActivity.start(this);
    }

    public void goRecordVideoActivity(View view) {
        startActivity(new Intent(MainActivity.this, RecordVideoActivity.class));
    }

    public void goTabActivity(View view) {
        startActivity(new Intent(MainActivity.this, TabActivity.class));
    }

    public void goDimpleActivity(View view) {
        startActivity(new Intent(MainActivity.this, DimpleActivity.class));
    }

    public void goMotionActivity(View view) {
        startActivity(new Intent(MainActivity.this, MotionActivity.class));
    }

    public void goFlexBoxActivity(View view) {
        startActivity(new Intent(MainActivity.this, FlexActivity.class));
    }

    public void goViewPager2LoopActivity(View view) {
        startActivity(new Intent(MainActivity.this, ViewPager2LoopActivity.class));
    }
}

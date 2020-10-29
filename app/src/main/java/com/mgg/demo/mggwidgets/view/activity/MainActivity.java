package com.mgg.demo.mggwidgets.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mgg.demo.mggwidgets.BaseActivity;
import com.mgg.demo.mggwidgets.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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
}

package com.mgg.demo.mggwidgets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goRecordButtonActivity(View view) {
        startActivity(new Intent(MainActivity.this,RecordButtonActivity.class));
    }

    public void goRoundImageViewActivity(View view) {
        startActivity(new Intent(MainActivity.this,RoundImageViewActivity.class));
    }

    public void goSvgActivity(View view) {
        startActivity(new Intent(MainActivity.this,SvgActivity.class));
    }

    public void goCalendarActivity(View view) {
        CalendarActivity.start(this);
    }

    public void goLiveActivity(View view) {
        LiveActivity.start(this);
    }
}

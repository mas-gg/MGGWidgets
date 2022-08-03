package com.wx.demo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wx.demo.BaseActivity;
import com.wx.demo.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goFlexBoxActivity(View view) {
        startActivity(new Intent(MainActivity.this, FlexActivity.class));
    }

}

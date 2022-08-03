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

    public void goFlexBoxActivity(View view) {
        startActivity(new Intent(MainActivity.this, FlexActivity.class));
    }

}

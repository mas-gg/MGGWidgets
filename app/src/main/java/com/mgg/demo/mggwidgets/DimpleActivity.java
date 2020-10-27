package com.mgg.demo.mggwidgets;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;

public class DimpleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dimple);
        ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(R.id.iv_head), "rotation", 360);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.setRepeatCount(-1);
        animator.start();
    }
}
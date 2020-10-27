package com.mgg.demo.mggwidgets;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;


public class MotionActivity extends AppCompatActivity {
    MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_start);
        motionLayout = findViewById(R.id.motion_layout);
//        findViewById(R.id.v).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (motionLayout.getProgress() == 1) {
//                    motionLayout.transitionToStart();
//                } else {
//                    motionLayout.transitionToEnd();
//                }
//
//            }
//        });
    }
}
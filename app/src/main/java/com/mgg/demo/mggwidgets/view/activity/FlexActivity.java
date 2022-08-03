package com.mgg.demo.mggwidgets.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgg.demo.mggwidgets.BaseActivity;
import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.view.widgets.FlexLayout;

import java.util.Random;


public class FlexActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex);

        final Random random = new Random();
        final FlexLayout flexLayout = findViewById(R.id.fl);
        flexLayout.setOnClickListener(v -> flexLayout.addView(getRandomTv(random)));
        for (int i = 0; i < 10; i++) {
            flexLayout.addView(getRandomTv(random));
        }
    }

    @SuppressLint("SetTextI18n")
    TextView getRandomTv(Random random) {
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10 + random.nextInt(10));
        tv.setText("Hello World");
        int padding = random.nextInt(20);
        tv.setPadding(padding, padding, padding, padding);
        tv.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        tv.setLayoutParams(layoutParams);
        return tv;
    }
}
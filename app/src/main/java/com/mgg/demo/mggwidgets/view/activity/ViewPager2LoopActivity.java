package com.mgg.demo.mggwidgets.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.view.adapter.ViewPager2LoopAdapter;


public class ViewPager2LoopActivity extends AppCompatActivity {
    private static final String TAG = "ViewPager2LoopActivity";

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager2_loop);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(new ViewPager2LoopAdapter(viewPager2, new RecyclerView.Adapter<MyViewHolder>() {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView tv = new TextView(ViewPager2LoopActivity.this);
                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(Color.WHITE);
                return new MyViewHolder(tv);
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
                ((TextView) holder.itemView).setText(String.valueOf(position));
            }

            @Override
            public int getItemCount() {
                return 6;
            }
        }));
        viewPager2.setCurrentItem(1, false);
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
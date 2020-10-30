package com.mgg.demo.mggwidgets.view.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import static androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING;

/**
 * created by mgg
 * 2020/10/30
 */
public class ViewPager2LoopAdapter extends RecyclerView.Adapter {
    ViewPager2 viewPager2;
    RecyclerView.Adapter mAdapter;

    public ViewPager2LoopAdapter(@NonNull ViewPager2 viewPager2, @NonNull RecyclerView.Adapter mAdapter) {
        this.viewPager2 = viewPager2;
        this.mAdapter = mAdapter;
        viewPager2.registerOnPageChangeCallback(new LoopOnPageChangeCallback(viewPager2));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        mAdapter.onBindViewHolder(holder, toRealPosition(position));
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 2;
    }

    public int toRealPosition(int position) {
        if (position == 0) {
            position = mAdapter.getItemCount() - 1;
        } else if (position == mAdapter.getItemCount() + 1) {
            position = 0;
        } else {
            position--;
        }
        return position;
    }

    private static class LoopOnPageChangeCallback extends OnPageChangeCallback {
        ViewPager2 viewPager2;

        public LoopOnPageChangeCallback(@NonNull ViewPager2 viewPager2) {
            this.viewPager2 = viewPager2;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == SCROLL_STATE_DRAGGING) {
                int index = viewPager2.getCurrentItem();
                int count = viewPager2.getAdapter().getItemCount();
                if (index == 0) {
                    viewPager2.setCurrentItem(count - 2, false);
                } else if (index == count - 1) {
                    viewPager2.setCurrentItem(1, false);
                }

            }
        }

        @Override
        public void onPageSelected(int position) {
        }
    }
}

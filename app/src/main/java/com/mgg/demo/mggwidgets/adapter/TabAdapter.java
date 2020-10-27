package com.mgg.demo.mggwidgets.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.mgg.demo.mggwidgets.R;
import com.mgg.demo.mggwidgets.bean.TabBean;
import com.mgg.demo.mggwidgets.widgets.RippleView;

import java.util.List;

/**
 * created by mgg
 * 2020/7/20
 */
public class TabAdapter extends RecyclerView.Adapter<TabAdapter.MyHolder> {

    Context context;
    List<TabBean> list;
    int curSelected;

    public TabAdapter(Context context, List<TabBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false);
        return new MyHolder(view);
    }

    public void setCurSelected(int curSelected) {
        if (this.curSelected == curSelected) return;
        this.curSelected = curSelected;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final TabBean tab = list.get(position);
        holder.iv.setImageResource(position == curSelected ? tab.selectedResId : tab.normalResId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == curSelected) return;
                notifyItemChanged(curSelected);
                curSelected = position;
                holder.layout.animate().scaleX(0.8f)
                        .scaleY(0.8f)
                        .setDuration(100)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                RippleView rippleView = new RippleView(context);
                                rippleView.startRipple(holder.layout);
                                holder.iv.setImageResource(list.get(position).selectedResId);
                                holder.layout.animate().scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(300)
                                        .start();
                            }
                        })
                        .start();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout;
        public ImageView iv;

        public MyHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}

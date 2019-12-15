package com.kotlin.toutiao.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kotlin.toutiao.util.GlideApp;

/**
 * RecyclerView滑动时不加载图片，处于静止状态时加载图片
 */
public class GlideRecyclerView extends RecyclerView {
    public GlideRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public GlideRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlideRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(context).resumeRequests();
                }else{
                    GlideApp.with(context).pauseRequests();
                }


            }
        });
    }


}

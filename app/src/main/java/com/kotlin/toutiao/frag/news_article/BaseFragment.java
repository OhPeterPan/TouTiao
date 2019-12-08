package com.kotlin.toutiao.frag.news_article;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kotlin.toutiao.custom.State;
import com.kotlin.toutiao.custom.view.StateLayout;

public abstract class BaseFragment extends Fragment {

    private boolean isFirst = true;
    private boolean isInit = false;
    private boolean isVisible = false;
    protected StateLayout stateLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (stateLayout == null)
            stateLayout = new StateLayout(getContext()) {
                @Override
                protected void initView(View view) {
                    isFirst = false;
                    initFragView(view);
                }

                @Override
                protected int getLayoutId() {
                    return getFragLayoutId();
                }
            };

        isInit = true;
        return stateLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected abstract void initFragView(View view);

    protected abstract int getFragLayoutId();

    @Override
    public void onResume() {
        super.onResume();
        if (!isVisible) {
            isVisible = true;
            initData();
        }
    }

    private void initData() {
        if (isFirst && isInit && isVisible) {
            doWork();
        }
    }

    protected abstract void doWork();

}

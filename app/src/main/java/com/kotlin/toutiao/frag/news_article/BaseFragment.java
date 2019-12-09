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
import com.kotlin.toutiao.presenter.BasePresenter;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment {

    private boolean isFirst = true;
    private boolean isInit = false;
    private boolean isVisible = false;
    protected StateLayout stateLayout;

    protected MultiTypeAdapter adapter;
    protected Items oldItems = new Items();
    protected boolean canLoadMore = false;
    protected T presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (stateLayout == null) {
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

        }


        isInit = true;
        return stateLayout;
    }

    protected abstract void initPresenter();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (null != presenter)
            presenter.onDestroy();
        super.onDestroy();
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
            initPresenter();
            doWork();
        }
    }

    protected abstract void doWork();

    public void showLoading() {
    }

    public void hideLoading() {
    }

    public void onFail(Throwable throwable) {
        setState(State.ERROR);
    }

    protected void setState(State state) {
        if (null != stateLayout)
            stateLayout.setState(state);
    }

}

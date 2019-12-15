package com.kotlin.toutiao.custom.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kotlin.toutiao.R;
import com.kotlin.toutiao.custom.State;

public abstract class StateLayout extends FrameLayout implements View.OnClickListener {
    private Context context;
    private State state = State.LOADING;
    private View loadingView, errorView, netErrorView, emptyView, successView;

    public StateLayout(Context context) {
        super(context);
        init(context);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
      createStateLayout();
    }

    private void createStateLayout() {

        if (loadingView == null) {
            loadingView = LayoutInflater.from(context).inflate(R.layout.load_view, null);
            initStateView(loadingView);
        }

        if (netErrorView == null) {

            netErrorView = LayoutInflater.from(context).inflate(R.layout.net_error_view, null);
            initStateView(netErrorView);
        }

        if (emptyView == null) {

            emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null);
            initStateView(emptyView);
        }

        if (errorView == null) {
            errorView = LayoutInflater.from(context).inflate(R.layout.error_layout, null);
            initStateView(errorView);
        }
      showPage();
    }

    private void showPage() {
        if (state == State.LOADING) {
            loadingView.setVisibility(VISIBLE);
        } else {
            loadingView.setVisibility(INVISIBLE);
        }

        if (state == State.EMPTY) {
            emptyView.setVisibility(VISIBLE);
        } else {
            emptyView.setVisibility(INVISIBLE);
        }

        if (state == State.ERROR || state == State.UNKNOW) {
            errorView.setVisibility(VISIBLE);
        } else {
            errorView.setVisibility(INVISIBLE);
        }

        if (state == State.NET_ERROR) {
            netErrorView.setVisibility(VISIBLE);
        } else {
            netErrorView.setVisibility(INVISIBLE);
        }

        if (state == State.SUCCESS) {

            if (successView == null) {
                successView = getSuccessView();
                initView(successView);
            } else {
                successView.setVisibility(VISIBLE);
            }

        } else {
            if (null != successView) {
                successView.setVisibility(INVISIBLE);
            }
        }
    }

    public void setState(State state) {
        this.state = state;
        showPage();
    }

    protected abstract void initView(View view);

    protected abstract int getLayoutId();

    protected View getSuccessView() {
        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }


    private void initStateView(View view) {
        addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View stateRetry = view.findViewById(R.id.stateRetry);
        if (stateRetry != null) {
            stateRetry.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stateRetry:
                changeState();
                break;
        }
    }

    private void changeState() {
        state = State.LOADING;
        showPage();
        retryLoadData();
    }

    protected abstract void retryLoadData();
}

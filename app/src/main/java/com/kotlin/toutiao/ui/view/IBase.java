package com.kotlin.toutiao.ui.view;

import android.view.View;

public interface IBase {
    int getLayoutId();

    View getLayoutParentView();

    void initPresenter();
}

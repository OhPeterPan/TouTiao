package com.kotlin.toutiao.ui.view;

public interface IView {
    void showLoading();

    void hideLoading();

    void onFail(Throwable throwable);
}

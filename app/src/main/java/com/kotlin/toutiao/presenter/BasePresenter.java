package com.kotlin.toutiao.presenter;

import com.kotlin.toutiao.model.IModel;
import com.kotlin.toutiao.ui.view.IView;

import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T extends IView, U extends IModel> {
    protected T view;
    protected U model;

    private final ConcurrentHashMap<Object, Disposable> concurrentHashMap;//线程安全的HashMap

    public BasePresenter(T view, U model) {
        this.view = view;
        this.model = model;
        concurrentHashMap = new ConcurrentHashMap<>();
    }

    public void addDisposable(Object tag, Disposable disposable) {

        if (concurrentHashMap.containsKey(tag)) {
            if (!concurrentHashMap.get(tag).isDisposed())
                concurrentHashMap.get(tag).dispose();
        } else {
            concurrentHashMap.put(tag, disposable);
        }
    }

    public void onFail(Throwable e) {
        if (null != view)
            view.onFail(e);
    }

    protected void clearCompositeDisposable() {
        for (Object o : concurrentHashMap.keySet()) {
            if (null != concurrentHashMap.get(o) && !concurrentHashMap.get(o).isDisposed()) {
                concurrentHashMap.get(o).dispose();
            }
        }
        concurrentHashMap.clear();
    }

    public void onDestroy() {
        clearCompositeDisposable();
    }
}

package com.kotlin.toutiao.presenter;

import com.kotlin.toutiao.model.IModel;
import com.kotlin.toutiao.ui.view.IView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter<T extends IView, U extends IModel> {
    protected T view;
    protected U model;

    private CompositeDisposable compositeDisposable;

    public BasePresenter(T view, U model) {
        this.view = view;
        this.model = model;
        compositeDisposable = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable != null)
            compositeDisposable.add(disposable);
    }


    public void onFail(Throwable e) {
        if (null != view)
            view.onFail(e);
    }

    public void onDestroy() {
        if (null != compositeDisposable && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

}

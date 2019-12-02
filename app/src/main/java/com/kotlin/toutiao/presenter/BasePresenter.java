package com.kotlin.toutiao.presenter;

import com.kotlin.toutiao.ui.view.IView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BasePresenter<T extends IView> {
    T view;
    private CompositeDisposable compositeDisposable;

    public BasePresenter(T view) {
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable != null)
            compositeDisposable.add(disposable);
    }

    private void onError(Throwable e) {
        if (view != null)
            view.onFail(e);
    }

    public void onDestroy() {
        if (null != compositeDisposable && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }

}

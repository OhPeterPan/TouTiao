package com.kotlin.toutiao.frag.wenda.presenter;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.bean.wenda.WendaArticleDataBean;
import com.kotlin.toutiao.frag.news_article.INewsCallback;
import com.kotlin.toutiao.frag.wenda.IWenDaCallback;
import com.kotlin.toutiao.frag.wenda.model.WenDaModel;
import com.kotlin.toutiao.frag.wenda.view.IWenDaView;
import com.kotlin.toutiao.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class WenDaPresenter extends BasePresenter<IWenDaView, WenDaModel> implements IWenDaCallback {
    private static final String TAG = "问答";
    private List<WendaArticleDataBean> dataList = new ArrayList<>();

    public WenDaPresenter(IWenDaView view, WenDaModel model) {
        super(view, model);
    }

    public void doLoadData() {
        // 释放内存
        if (dataList.size() > 150) {
            dataList.clear();
        }
        Disposable disposable = model.doLoadData(this);

        addDisposable(TAG, disposable);
    }

    public void doRefresh() {
        if (dataList.size() != 0) {
            dataList.clear();
            addDisposable(TAG, model.doRefresh( this));
        }
    }

    public void doLoadMoreData() {
        doLoadData();
    }

    @Override
    public void doSetAdapter(List<WendaArticleDataBean> list) {
        dataList.addAll(list);
        view.onSetAdapter(dataList);
    }

    @Override
    public void doShowNoMore() {
        view.hideLoading();
        view.onShowNoMore();
    }
}

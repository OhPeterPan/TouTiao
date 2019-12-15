package com.kotlin.toutiao.frag.news_article.presenter;

import android.util.Log;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.frag.news_article.INewsCallback;
import com.kotlin.toutiao.frag.news_article.model.NewsModel;
import com.kotlin.toutiao.frag.news_article.view.INewArticleView;
import com.kotlin.toutiao.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.Disposable;

public class NewArticlePresenter extends BasePresenter<INewArticleView, NewsModel> implements INewsCallback {
    private static final String TAG = "wak";

    private String category;
    private List<MultiNewsArticleDataBean> dataList = new ArrayList<>();

    private Random random = new Random();

    public NewArticlePresenter(INewArticleView view, NewsModel model) {
        super(view, model);
    }

    public void doRefresh(String channelId) {
        if (dataList.size() != 0) {
            dataList.clear();
            addDisposable(channelId, model.doRefresh(channelId, this));
        }
    }

    public void doLoadMoreData(String channelId) {
        doLoadData(channelId);
    }

    public void doLoadData(String channelId) {
        // 释放内存
        if (dataList.size() > 150) {
            dataList.clear();
        }
        Disposable disposable = model.doLoadData(channelId, this);
        System.out.println("wak加载"+disposable);
        addDisposable(channelId, disposable);
    }

    @Override
    public void doSetAdapter(List<MultiNewsArticleDataBean> list) {
        dataList.addAll(list);
        view.onSetAdapter(dataList);
    }

    @Override
    public void doShowNoMore() {
        view.hideLoading();
        view.onShowNoMore();
    }


}

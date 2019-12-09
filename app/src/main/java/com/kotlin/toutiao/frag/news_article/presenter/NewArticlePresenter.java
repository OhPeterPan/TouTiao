package com.kotlin.toutiao.frag.news_article.presenter;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.frag.news_article.INewsCallback;
import com.kotlin.toutiao.frag.news_article.model.NewsModel;
import com.kotlin.toutiao.frag.news_article.view.INewArticleView;
import com.kotlin.toutiao.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NewArticlePresenter extends BasePresenter<INewArticleView, NewsModel> implements INewsCallback {


    private String category;
    private List<MultiNewsArticleDataBean> dataList = new ArrayList<>();

    private Random random = new Random();

    public NewArticlePresenter(INewArticleView view, NewsModel model) {
        super(view, model);
    }

    public void doLoadMoreData() {

    }

    public void doLoadData(String channelId) {
        // 释放内存
        if (dataList.size() > 150) {
            dataList.clear();
        }
        model.doLoadData(channelId,this);
    }

    @Override
    public void doSetAdapter(List<MultiNewsArticleDataBean> list) {
        dataList.addAll(list);
        view.onSetAdapter(dataList);
    }

    @Override
    public void doShowNoMore() {

    }
}

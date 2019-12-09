package com.kotlin.toutiao.frag.news_article;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.callback.ICallback;

import java.util.List;

public interface INewsCallback extends ICallback {
    void doSetAdapter(List<MultiNewsArticleDataBean> list);

    void doShowNoMore();
}

package com.kotlin.toutiao.frag.news_article.view;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.ui.view.IView;

import java.util.List;

public interface INewArticleView extends IView {

    void onSetAdapter(List<MultiNewsArticleDataBean> list);
}

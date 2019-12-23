package com.kotlin.toutiao.frag.wenda;

import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.bean.wenda.WendaArticleDataBean;
import com.kotlin.toutiao.callback.ICallback;

import java.util.List;

public interface IWenDaCallback extends ICallback {
    void doSetAdapter(List<WendaArticleDataBean> list);

    void doShowNoMore();
}

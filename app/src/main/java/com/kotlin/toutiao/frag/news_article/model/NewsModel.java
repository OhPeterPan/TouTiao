package com.kotlin.toutiao.frag.news_article.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kotlin.toutiao.ErrorAction;
import com.kotlin.toutiao.api.IMobileNewsApi;
import com.kotlin.toutiao.bean.news.MultiNewsArticleBean;
import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.frag.news_article.INewsCallback;
import com.kotlin.toutiao.model.IModel;
import com.kotlin.toutiao.net.RetrofitCreator;
import com.kotlin.toutiao.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class NewsModel extends IModel {
    private String time;
    private Gson gson = new Gson();
    private Random random = new Random();
    private List<MultiNewsArticleDataBean> dataList = new ArrayList<>();
    private String channelId;

    public NewsModel() {
        this.time = TimeUtil.getCurrentTimeStamp();
    }

    public Disposable doLoadData(String channelId, final INewsCallback callback) {
        this.channelId = channelId;
        // 释放内存
        if (dataList.size() > 150) {
            dataList.clear();
        }

        return getRandom()
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<MultiNewsArticleBean, Observable<MultiNewsArticleDataBean>>() {
                    @Override
                    public Observable<MultiNewsArticleDataBean> apply(@NonNull MultiNewsArticleBean multiNewsArticleBean) throws Exception {
                        List<MultiNewsArticleDataBean> dataList = new ArrayList<>();
                        for (MultiNewsArticleBean.DataBean dataBean : multiNewsArticleBean.getData()) {
                            dataList.add(gson.fromJson(dataBean.getContent(), MultiNewsArticleDataBean.class));
                        }
                        return Observable.fromIterable(dataList);
                    }
                })
                .filter(new Predicate<MultiNewsArticleDataBean>() {
                    @Override
                    public boolean test(@NonNull MultiNewsArticleDataBean dataBean) throws Exception {
                        time = dataBean.getBehot_time();
                        if (TextUtils.isEmpty(dataBean.getSource())) {
                            return false;
                        }
                        try {
                            // 过滤头条问答新闻
                            if (dataBean.getSource().contains("头条问答")
                                    || dataBean.getTag().contains("ad")
                                    || dataBean.getSource().contains("悟空问答")) {
                                return false;
                            }
                            // 过滤头条问答新闻
                            if (dataBean.getRead_count() == 0 || TextUtils.isEmpty(dataBean.getMedia_name())) {
                                String title = dataBean.getTitle();
                                if (title.lastIndexOf("？") == title.length() - 1) {
                                    return false;
                                }
                            }
                        } catch (NullPointerException e) {
                            ErrorAction.print(e);
                        }
                        // 过滤重复新闻(与上次刷新的数据比较)
                        for (MultiNewsArticleDataBean bean : dataList) {
                            if (bean.getTitle().equals(dataBean.getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .map(new Function<List<MultiNewsArticleDataBean>, List<MultiNewsArticleDataBean>>() {
                    @Override
                    public List<MultiNewsArticleDataBean> apply(@NonNull List<MultiNewsArticleDataBean> list) throws Exception {
                        // 过滤重复新闻(与本次刷新的数据比较,因为使用了2个请求,数据会有重复)
                        for (int i = 0; i < list.size() - 1; i++) {
                            for (int j = list.size() - 1; j > i; j--) {
                                if (list.get(j).getTitle().equals(list.get(i).getTitle())) {
                                    list.remove(j);
                                }
                            }
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<List<MultiNewsArticleDataBean>>() {
                    @Override
                    public void accept(@NonNull List<MultiNewsArticleDataBean> list) throws Exception {
                        if (null != list && list.size() > 0) {
                            dataList.addAll(list);
                            callback.doSetAdapter(list);
                        } else {
                            callback.doShowNoMore();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {

                        callback.onFail(e);
                        ErrorAction.print(e);
                    }
                });
    }

    private Observable<MultiNewsArticleBean> getRandom() {

        int i = random.nextInt(10);
        if (i % 2 == 0) {
            Observable<MultiNewsArticleBean> ob1 = RetrofitCreator.retrofitCreate().create(IMobileNewsApi.class)
                    .getNewsArticle(this.channelId, this.time);
            return ob1;
        } else {
            Observable<MultiNewsArticleBean> ob2 = RetrofitCreator.retrofitCreate().create(IMobileNewsApi.class)
                    .getNewsArticle2(this.channelId, this.time);
            return ob2;
        }
    }

}

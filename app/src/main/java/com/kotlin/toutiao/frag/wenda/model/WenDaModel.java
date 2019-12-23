package com.kotlin.toutiao.frag.wenda.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kotlin.toutiao.ErrorAction;
import com.kotlin.toutiao.api.IMobileWendaApi;
import com.kotlin.toutiao.bean.wenda.WendaArticleBean;
import com.kotlin.toutiao.bean.wenda.WendaArticleDataBean;
import com.kotlin.toutiao.frag.news_article.INewsCallback;
import com.kotlin.toutiao.frag.wenda.IWenDaCallback;
import com.kotlin.toutiao.frag.wenda.presenter.WenDaPresenter;
import com.kotlin.toutiao.model.IModel;
import com.kotlin.toutiao.net.RetrofitCreator;
import com.kotlin.toutiao.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class WenDaModel extends IModel {
    private String time;
    private Gson gson = new Gson();
    private List<WendaArticleDataBean> dataList = new ArrayList<>();


    public Disposable doLoadData(final IWenDaCallback callback) {

        // 释放内存
        if (dataList.size() > 100) {
            dataList.clear();
        }

        return RetrofitCreator.retrofitCreate().create(IMobileWendaApi.class)
                .getWendaArticle(time)
                .subscribeOn(Schedulers.io())
                .switchMap(new Function<WendaArticleBean, Observable<WendaArticleDataBean>>() {
                    @Override
                    public Observable<WendaArticleDataBean> apply(@NonNull WendaArticleBean wendaArticleBean) throws Exception {

                        List<WendaArticleDataBean> list = new ArrayList<>();
                        for (WendaArticleBean.DataBean bean : wendaArticleBean.getData()) {
                            WendaArticleDataBean contentBean = gson.fromJson(bean.getContent(), WendaArticleDataBean.class);
                            list.add(contentBean);
                        }
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<WendaArticleDataBean>() {
                    @Override
                    public boolean test(@NonNull WendaArticleDataBean wendaArticleDataBean) throws Exception {
                        return !TextUtils.isEmpty(wendaArticleDataBean.getQuestion());
                    }
                })
                .map(new Function<WendaArticleDataBean, WendaArticleDataBean>() {
                    @Override
                    public WendaArticleDataBean apply(@NonNull WendaArticleDataBean bean) throws Exception {

                        WendaArticleDataBean.ExtraBean extraBean = gson.fromJson(bean.getExtra(), WendaArticleDataBean.ExtraBean.class);
                        WendaArticleDataBean.QuestionBean questionBean = gson.fromJson(bean.getQuestion(), WendaArticleDataBean.QuestionBean.class);
                        WendaArticleDataBean.AnswerBean answerBean = gson.fromJson(bean.getAnswer(), WendaArticleDataBean.AnswerBean.class);
                        bean.setExtraBean(extraBean);
                        bean.setQuestionBean(questionBean);
                        bean.setAnswerBean(answerBean);

                        time = bean.getBehot_time();
                        return bean;
                    }
                })
                .filter(new Predicate<WendaArticleDataBean>() {
                    @Override
                    public boolean test(@NonNull WendaArticleDataBean wendaArticleDataBean) throws Exception {
                        for (WendaArticleDataBean bean : dataList) {
                            if (bean.getQuestionBean().getTitle().equals(wendaArticleDataBean.getQuestionBean().getTitle())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<List<WendaArticleDataBean>>() {
                    @Override
                    public void accept(@NonNull List<WendaArticleDataBean> wendaArticleDataBeen) throws Exception {
                        callback.doSetAdapter(wendaArticleDataBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        callback.onFail(throwable);
                        ErrorAction.print(throwable);
                    }
                });
    }

    public Disposable doRefresh(IWenDaCallback callback) {
        if (dataList.size() != 0) {
            dataList.clear();
            time = TimeUtil.getCurrentTimeStamp();
        }
        Disposable disposable = doLoadData(callback);
        return disposable;
    }
}

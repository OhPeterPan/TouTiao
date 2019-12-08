package com.kotlin.toutiao.ui.news;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.kotlin.toutiao.R;
import com.kotlin.toutiao.adapter.news.NewsChannelAdapter;
import com.kotlin.toutiao.bean.NewsChannelBean;
import com.kotlin.toutiao.database.dao.NewsChannelDao;
import com.kotlin.toutiao.frag.news.NewsFragment;
import com.kotlin.toutiao.ui.BaseActivity;
import com.kotlin.toutiao.util.Constant;
import com.kotlin.toutiao.util.RxBus;
import com.kotlin.toutiao.util.StatusBarUtil;
import com.kotlin.toutiao.widget.helper.ItemDragHelperCallback;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewsChannelActivity extends BaseActivity {

    private RecyclerView recycler_view;
    private NewsChannelDao dao = new NewsChannelDao();
    private NewsChannelAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    protected void initView() {
        super.initView();
        initToolBar(true, getString(R.string.title_item_drag));
        recycler_view = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        final List<NewsChannelBean> enableItems = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        final List<NewsChannelBean> disableItems = dao.query(Constant.NEWS_CHANNEL_DISABLE);
        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recycler_view);

        adapter = new NewsChannelAdapter(this, helper, enableItems, disableItems);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = adapter.getItemViewType(position);
                return itemViewType == NewsChannelAdapter.TYPE_MY_HEADER || itemViewType == NewsChannelAdapter.TYPE_OTHER_HEADER ? 4 : 1;
            }
        });
        recycler_view.setLayoutManager(manager);
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void innerClick(View v) {

    }


    @Override
    public View getLayoutParentView() {
        return null;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onStop() {
        super.onStop();

        saveData();
    }

    private void saveData() {

        Observable
                .create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                        List<NewsChannelBean> oldItems = dao.query(Constant.NEWS_CHANNEL_ENABLE);
                        e.onNext(!compare(oldItems, adapter.getmMyChannelItems()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            List<NewsChannelBean> enableItems = adapter.getmMyChannelItems();
                            List<NewsChannelBean> disableItems = adapter.getmOtherChannelItems();
                            dao.removeAll();
                            for (int i = 0; i < enableItems.size(); i++) {
                                NewsChannelBean bean = enableItems.get(i);
                                dao.add(bean.getChannelId(), bean.getChannelName(), Constant.NEWS_CHANNEL_ENABLE, i);
                            }
                            for (int i = 0; i < disableItems.size(); i++) {
                                NewsChannelBean bean = disableItems.get(i);
                                dao.add(bean.getChannelId(), bean.getChannelName(), Constant.NEWS_CHANNEL_DISABLE, i);
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isRefresh) throws Exception {
                        RxBus.getInstance().post(NewsFragment.TAG, isRefresh);
                    }
                });
    }

    public synchronized <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
//        Collections.sort(a);
//        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }
}

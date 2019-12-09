package com.kotlin.toutiao.frag.news_article;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kotlin.toutiao.R;
import com.kotlin.toutiao.bean.LoadingBean;
import com.kotlin.toutiao.bean.news.MultiNewsArticleDataBean;
import com.kotlin.toutiao.custom.State;
import com.kotlin.toutiao.frag.news_article.model.NewsModel;
import com.kotlin.toutiao.frag.news_article.presenter.NewArticlePresenter;
import com.kotlin.toutiao.frag.news_article.view.INewArticleView;
import com.kotlin.toutiao.util.DiffCallback;
import com.kotlin.toutiao.util.OnLoadMoreListener;
import com.kotlin.toutiao.widget.Register;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class NewsArticleFragment extends BaseFragment<NewArticlePresenter> implements INewArticleView {

    private String channelId;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    public static Fragment newInstance(String channelId) {
        Fragment fragment = new NewsArticleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("channelld", channelId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        channelId = bundle.getString("channelld");
    }

    @Override
    protected void initPresenter() {
        NewsModel model = new NewsModel();
        presenter = new NewArticlePresenter(this, model);
    }


    @Override
    protected void doWork() {
        presenter.doLoadData(channelId);
    }

    @Override
    protected int getFragLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initFragView(View view) {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MultiTypeAdapter(oldItems);
        Register.registerNewsArticleItem(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    canLoadMore = false;
                    presenter.doLoadMoreData();
                }
            }
        });
    }

    @Override
    public void onSetAdapter(List<MultiNewsArticleDataBean> list) {
        setState(State.SUCCESS);
     //   System.out.println("呵呵呵呵:" + list.size());
        Items newItems = new Items(list);
        newItems.add(new LoadingBean());
        DiffCallback.create(oldItems, newItems, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);

        canLoadMore = true;
        recyclerView.stopScroll();
    }
}

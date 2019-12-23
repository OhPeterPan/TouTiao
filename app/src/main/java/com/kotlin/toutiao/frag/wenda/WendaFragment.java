package com.kotlin.toutiao.frag.wenda;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kotlin.toutiao.R;
import com.kotlin.toutiao.bean.LoadingBean;
import com.kotlin.toutiao.custom.State;
import com.kotlin.toutiao.frag.news_article.BaseFragment;
import com.kotlin.toutiao.frag.wenda.model.WenDaModel;
import com.kotlin.toutiao.frag.wenda.presenter.WenDaPresenter;
import com.kotlin.toutiao.frag.wenda.view.IWenDaView;
import com.kotlin.toutiao.util.DiffCallback;
import com.kotlin.toutiao.util.OnLoadMoreListener;
import com.kotlin.toutiao.widget.Register;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class WendaFragment extends BaseFragment<WenDaPresenter>implements IWenDaView {

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected int getFragLayoutId() {
        return R.layout.fragment_list;
    }

    public static Fragment newInstance() {
        Fragment fragment = new WendaFragment();
        return fragment;
    }

    @Override
    protected void initPresenter() {
        WenDaModel wenDaModel = new WenDaModel();
        presenter = new WenDaPresenter(this,wenDaModel);
    }

    @Override
    protected void initFragView(View view) {
        refreshLayout = view.findViewById(R.id.refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new MultiTypeAdapter(oldItems);
        Register.registerWendaArticleItem(adapter);
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstLoad = true;
                int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0) {
                     presenter.doRefresh();
                    return;
                }
                recyclerView.scrollToPosition(5);
                recyclerView.smoothScrollToPosition(0);

            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        if (isFirstLoad)
            setState(State.EMPTY);
    }

    @Override
    protected void doWork() {
        isFirstLoad = true;
        presenter.doLoadData();
    }

    @Override
    public void onSetAdapter(List<?> list) {

        if (isFirstLoad) {
            if (null != refreshLayout && refreshLayout.isRefreshing()) {
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
            isFirstLoad = false;
            if (null == list || list.size() == 0) {
                setState(State.EMPTY);
                return;
            } else {
                setState(State.SUCCESS);
            }
        }

        Items newItems = new Items(list);
        newItems.add(new LoadingBean());
        DiffCallback.create(oldItems, newItems, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
        recyclerView.stopScroll();

    }
}

package com.kotlin.toutiao.frag.news_article;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kotlin.toutiao.R;

public class NewsArticleFragment extends BaseFragment {

    private String channelId;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //System.out.println("channelId:" + channelId + ":onCreateView:" + stateLayout);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  System.out.println("channelId:" + channelId + ":onDestroyView");
    }

    @Override
    protected void doWork() {


    }

    @Override
    protected int getFragLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initFragView(View view) {

    }


}

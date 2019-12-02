package com.kotlin.toutiao.frag.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kotlin.toutiao.R;
import com.kotlin.toutiao.adapter.BasePagerAdapter;
import com.kotlin.toutiao.bean.NewsChannelBean;
import com.kotlin.toutiao.database.dao.NewsChannelDao;
import com.kotlin.toutiao.frag.joke.JokeFragment;
import com.kotlin.toutiao.frag.news_article.NewsArticleFragment;
import com.kotlin.toutiao.frag.wenda.WendaFragment;
import com.kotlin.toutiao.util.Constant;
import com.kotlin.toutiao.util.SettingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFragment extends Fragment {

    private LinearLayout linearLayout;
    private List<Fragment> fragmentList;
    private NewsChannelDao dao = new NewsChannelDao();
    private List<String> titleList;
    private Map<String, Fragment> map = new HashMap<>();
    private ViewPager viewPager;
    private BasePagerAdapter adapter;

    public static Fragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_tab, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        linearLayout = view.findViewById(R.id.header_layout);
        TabLayout tab_layout_news = view.findViewById(R.id.tab_layout_news);
        tab_layout_news.setTabMode(TabLayout.MODE_SCROLLABLE);
        ImageView add_channel_iv = view.findViewById(R.id.add_channel_iv);
        add_channel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewPager = view.findViewById(R.id.view_pager_news);
        tab_layout_news.setupWithViewPager(viewPager);
    }

    private void initData() {
        initTips();

        adapter = new BasePagerAdapter(getChildFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(15);

    }

    private void initTips() {
        List<NewsChannelBean> channelList = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        if (channelList.size() == 0) {
            dao.addInitData();
            channelList = dao.query(Constant.NEWS_CHANNEL_ENABLE);
        }

        for (NewsChannelBean bean : channelList) {

            Fragment fragment = null;
            String channelId = bean.getChannelId();

            switch (channelId) {
                case "essay_joke"://包含段子的话使用段子特有的Fragment
                    if (map.containsKey(channelId)) {
                        fragmentList.add(map.get(channelId));
                    } else {
                        fragment = JokeFragment.newInstance();
                        fragmentList.add(fragment);
                    }

                    break;
                case "question_and_answer"://问答特有的Fragment
                    if (map.containsKey(channelId)) {
                        fragmentList.add(map.get(channelId));
                    } else {
                        fragment = WendaFragment.newInstance();
                        fragmentList.add(fragment);
                    }

                    break;
                default://默认的Fragment
                    if (map.containsKey(channelId)) {
                        fragmentList.add(map.get(channelId));
                    } else {
                        fragment = NewsArticleFragment.newInstance(channelId);
                        fragmentList.add(fragment);
                    }
                    break;
            }

            titleList.add(bean.getChannelName());

            if (fragment != null) {
                map.put(channelId, fragment);//放到一个集合里面
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        linearLayout.setBackgroundColor(SettingUtil.getInstance().getColor());
    }
}

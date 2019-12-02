package com.kotlin.toutiao;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.kotlin.toutiao.frag.media.MediaFragment;
import com.kotlin.toutiao.frag.news.NewsFragment;
import com.kotlin.toutiao.frag.photo.PhotoFragment;
import com.kotlin.toutiao.frag.video.VideoFragment;
import com.kotlin.toutiao.ui.BaseActivity;
import com.kotlin.toutiao.util.SettingUtil;
import com.kotlin.toutiao.util.UIUtils;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private BottomNavigationView bottom_navigation;
    private NavigationView nav_view;
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private static final int FRAGMENT_NEWS = 0;
    private static final int FRAGMENT_PHOTO = 1;
    private static final int FRAGMENT_VIDEO = 2;
    private static final int FRAGMENT_MEDIA = 3;
    private int position;
    private Fragment lastFrg;
    private Fragment newsFrag, photoFrag, videoFrag, mediaFrag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public View getLayoutParentView() {
        return null;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            newsFrag = manager.findFragmentByTag(NewsFragment.class.getSimpleName());
            photoFrag = manager.findFragmentByTag(PhotoFragment.class.getSimpleName());
            videoFrag = manager.findFragmentByTag(VideoFragment.class.getSimpleName());
            mediaFrag = manager.findFragmentByTag(MediaFragment.class.getSimpleName());

            showFragment(savedInstanceState.getInt(POSITION));
            bottom_navigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_NEWS);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putInt(POSITION, position);
            outState.putInt(SELECT_ITEM, bottom_navigation.getSelectedItemId());
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mDrawerLayout.setClipToPadding(false);
            }
        }

        getToolbar().setTitle(R.string.toutiao);
        getToolbar().inflateMenu(R.menu.menu_activity_search);
        setSupportActionBar(getToolbar());

        //必须在setSupportActionBar的后边
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_news:
                        showFragment(FRAGMENT_NEWS);
                        doubleClick(FRAGMENT_NEWS);
                        break;
                    case R.id.action_photo:
                        showFragment(FRAGMENT_PHOTO);
                        doubleClick(FRAGMENT_PHOTO);
                        break;
                    case R.id.action_video:
                        showFragment(FRAGMENT_VIDEO);
                        doubleClick(FRAGMENT_VIDEO);
                        break;
                    case R.id.action_media:
                        showFragment(FRAGMENT_MEDIA);
                        break;
                }
                return true;
            }
        });

        if (SettingUtil.getInstance().getIsFirstTime()) {
            showTapTarget();
        }
    }

    private FragmentManager manager = getSupportFragmentManager();

    private void showFragment(int pos) {
        FragmentTransaction ft = manager.beginTransaction();
        hideFrag(ft);
        switch (pos) {
            case FRAGMENT_NEWS:
                getToolbar().setTitle(R.string.toutiao);
                if (newsFrag == null) {
                    newsFrag = NewsFragment.newInstance();
                    ft.add(R.id.container, newsFrag, newsFrag.getClass().getSimpleName());
                } else {
                    ft.show(newsFrag);
                }
                break;

            case FRAGMENT_PHOTO:
                getToolbar().setTitle(R.string.title_photo);
                if (photoFrag == null) {
                    photoFrag = PhotoFragment.newInstance();
                    ft.add(R.id.container, photoFrag, photoFrag.getClass().getSimpleName());
                } else {
                    ft.show(photoFrag);
                }
                break;

            case FRAGMENT_VIDEO:
                getToolbar().setTitle(R.string.title_video);
                if (videoFrag == null) {
                    videoFrag = VideoFragment.newInstance();
                    ft.add(R.id.container, videoFrag, videoFrag.getClass().getSimpleName());
                } else {
                    ft.show(videoFrag);
                }
                break;

            case FRAGMENT_MEDIA:
                getToolbar().setTitle(R.string.title_media);
                if (mediaFrag == null) {
                    mediaFrag = MediaFragment.newInstance();
                    ft.add(R.id.container, mediaFrag, mediaFrag.getClass().getSimpleName());
                } else {
                    ft.show(mediaFrag);
                }
                break;
        }
        ft.commit();
    }

    private void hideFrag(FragmentTransaction ts) {
        if (newsFrag != null) {
            ts.hide(newsFrag);
        }
        if (photoFrag != null) {
            ts.hide(photoFrag);
        }
        if (videoFrag != null) {
            ts.hide(videoFrag);
        }
        if (mediaFrag != null) {
            ts.hide(mediaFrag);
        }
    }

    private void doubleClick(int pos) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_switch_night_mode://切换主题
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) {
                    SettingUtil.getInstance().saveNight(false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    SettingUtil.getInstance().saveNight(true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }

                recreate();
                break;

            case R.id.nav_setting://设置

                break;

            case R.id.nav_share://分享

                break;
        }
        return false;
    }

    private void showTapTarget() {
        final Display display = getWindowManager().getDefaultDisplay();
        final Rect target = new Rect(
                0,
                display.getHeight(),
                0,
                display.getHeight());
        target.offset(display.getWidth() / 8, -56);
        // 引导用户使用
        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forToolbarMenuItem(getToolbar(), R.id.action_search, "点击这里进行搜索")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .drawShadow(true)
                                .id(1),
                        TapTarget.forToolbarNavigationIcon(getToolbar(), "点击这里展开侧栏")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .drawShadow(true)
                                .id(2),
                        TapTarget.forBounds(target, "点击这里切换新闻", "双击返回顶部\n再次双击刷新当前页面")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.colorPrimary)
                                .targetRadius(60)
                                .transparentTarget(true)
                                .drawShadow(true)
                                .id(3)
                ).listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        SettingUtil.getInstance().setIsFirstTime(false);
                    }
                });
        sequence.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            //   startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initClick() {

    }

    @Override
    protected void innerClick(View v) {

    }

    @Override
    protected void initSlide() {
        //关闭侧滑

    }

    private long firstTime = 2000;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - firstTime < 2000)
            super.onBackPressed();
        else {
            Toast.makeText(UIUtils.getContext(), getString(R.string.double_click_exit), Toast.LENGTH_SHORT).show();
            firstTime = currentTime;
        }
    }
}

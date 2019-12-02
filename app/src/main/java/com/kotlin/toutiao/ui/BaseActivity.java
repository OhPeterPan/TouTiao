package com.kotlin.toutiao.ui;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.View;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kotlin.toutiao.R;
import com.kotlin.toutiao.presenter.BasePresenter;
import com.kotlin.toutiao.ui.view.IBase;
import com.kotlin.toutiao.util.Constant;
import com.kotlin.toutiao.util.SettingUtil;
import com.kotlin.toutiao.util.StatusBarUtil;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements IBase, View.OnClickListener {
    private boolean isView = false;//加载的view还是layout
    T presenter;
    protected SlidrInterface slidrInterface;//侧滑关闭或者开启的类
    private int iconType = 0;//桌面图标的下标值，constant
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        if (!isView)
            setContentView(getLayoutId());
        else
            setContentView(getLayoutParentView());
        initView();
        initData(savedInstanceState);
        initClick();
        initSlide();
    }

    protected void initView() {
        View back = findViewById(R.id.back);
        toolbar = findViewById(R.id.toolbar);
        if (back != null)
            back.setOnClickListener(this);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void initClick();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back)
            finish();
        innerClick(v);
    }

    protected abstract void innerClick(View v);

    protected void initSlide() {
        int slide = SettingUtil.getInstance().getSlide();
        if (slide != Constant.SLIDABLE_DISABLE) {
            SlidrConfig slidrConfig = new SlidrConfig.Builder()
                    .edge(slide == Constant.SLIDABLE_EDGE)
                    .build();
            slidrInterface = Slidr.attach(this, slidrConfig);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int color = SettingUtil.getInstance().getColor();
        int drawable = Constant.ICONS_DRAWABLES[SettingUtil.getInstance().getCustomIconValue()];
        StatusBarUtil.setGradientColor(this, getToolbar());
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));

         //   toolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getWindow().setStatusBarColor(color);
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), drawable),
                    color
            );
            setTaskDescription(taskDescription);

            if (SettingUtil.getInstance().getNavBar()) {
                getWindow().setNavigationBarColor(color);
            } else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        new Thread() {//更换桌面图标，因为是耗时操作，所以放在子线程
            @Override
            public void run() {
                super.run();

                if (iconType != SettingUtil.getInstance().getCustomIconValue()) {
                    String alis = ".splash_";
                    for (String tag : Constant.ICONS_TYPE) {
                        getPackageManager().setComponentEnabledSetting(
                                new ComponentName(BaseActivity.this, getPackageName() + alis + tag),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP
                        );
                    }

                    alis += Constant.ICONS_TYPE[SettingUtil.getInstance().getCustomIconValue()];
                    getPackageManager().setComponentEnabledSetting(
                            new ComponentName(BaseActivity.this, getPackageName() + alis),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                    );

                }

            }
        }.start();

    }

    private void showLoading() {

    }

    private void hideLoading() {

    }

    private void onFail() {

    }

    public void setView(boolean view) {
        isView = view;
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0)
            super.onBackPressed();
        else
            getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.onDestroy();
    }
}

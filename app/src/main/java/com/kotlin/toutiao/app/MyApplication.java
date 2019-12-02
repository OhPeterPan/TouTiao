package com.kotlin.toutiao.app;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.kotlin.toutiao.util.SettingUtil;


public class MyApplication extends Application {
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        if (SettingUtil.getInstance().getIsNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static Application getInstance() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /**
         * 此方法在Android5.0以下需要遍历查找其它DEX包的操作，然后将找到的DEX包转换成压缩包(变成.zip文件)，导致该方法很耗时
         *  第一进来的时候需要解压和压缩的过程，第二次进来的时候只需要从sp文件中拿到File list就行
         *
         * 1. 为什么需要变成zip文件呢？
         *      牵扯到ClassLoader机制
         *
         */
        MultiDex.install(this);
    }
}

package com.kotlin.toutiao.util;

import android.content.Context;
import android.content.res.Resources;

import com.kotlin.toutiao.app.MyApplication;

public class UIUtils {
    public static Context getContext() {
        return MyApplication.getInstance().getApplicationContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static String[] getStringArray(int resId) {

        return getResources().getStringArray(resId);
    }
}

package com.kotlin.toutiao.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkUtil {

    /**
     * 判断是否有数据网络连接
     *
     * @return
     */
    public static boolean isMobileConnected() {

        //获取手机所有连接管理对象(包括wifi，net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return networkInfo.isAvailable();
        else
            return false;
    }

    public static boolean isWifiConnected() {
        //获取手机所有连接管理对象(包括wifi，net等连接的管理)
        ConnectivityManager manager = (ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return networkInfo.isAvailable();
        else

        return false;
    }
}

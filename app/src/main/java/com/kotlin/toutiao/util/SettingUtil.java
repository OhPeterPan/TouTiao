package com.kotlin.toutiao.util;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.kotlin.toutiao.R;

public class SettingUtil {
    private SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(UIUtils.getContext());


    public static SettingUtil getInstance() {
        return SettingUtilInstance.SETTING_UTIL;
    }

    //获取是否开启无图模式
    public boolean getIsNoPhotoMode() {
        return sp.getBoolean("switch_noPhotoMode", false) && NetWorkUtil.isMobileConnected();
    }

    //获取主题颜色
    public int getColor() {
        int defaultColor = UIUtils.getColor(R.color.colorPrimary_night);
        int color = sp.getInt("color", defaultColor);
        if ((color != 0) && Color.alpha(color) != 255)
            return defaultColor;
        else
            return color;
    }

    //保存主题颜色
    public void saveColor(int color) {
        sp.edit().putInt("color", color).apply();
    }

    //获取是否开启夜间模式
    public boolean getIsNightMode() {
        return sp.getBoolean("switch_night", false);
    }

    //保存夜间模式
    public void saveNight(boolean flag) {
        sp.edit().putBoolean("switch_night", flag).apply();
    }

    //获取是否开启自动切换夜间模式
    public boolean getIsAutoNightMode() {

        return sp.getBoolean("auto_nightMode", false);
    }

    /**
     * 获取图标值
     */
    public int getCustomIconValue() {
        String s = sp.getString("custom_icon", "0");
        return Integer.parseInt(s);
    }


    //保存是否切换到夜间模式的状态
    public void saveIsAautoNight(boolean flag) {
        sp.edit().putBoolean("auto_nightMode", flag).apply();
    }

    //夜间模式开始时间小时
    public String getNightStartHour() {

        return sp.getString("night_startHour", "22");
    }

    public void setNightStartHour(String hour) {
        sp.edit().putString("night_startHour", hour).apply();
    }

    //夜间模式开始时间中的分钟数
    public String getNightStartMinute() {
        return sp.getString("night_startMinute", "00");
    }

    public void setNightStartMinute(String minute) {
        sp.edit().putString("night_startMinute", minute).apply();
    }

    //日间模式开始时间小时
    public String getDayStartHour() {

        return sp.getString("day_startHour", "06");
    }

    public void setDayStartHour(String hour) {
        sp.edit().putString("day_startHour", hour).apply();
    }

    //夜间模式开始时间中的分钟数
    public String getDayStartMinute() {
        return sp.getString("day_startMinute", "00");
    }

    public void setDayStartMinute(String minute) {
        sp.edit().putString("day_startMinute", minute).apply();
    }

    //获取导航栏是否跟随系统变色
    public boolean getNavBar() {
        return sp.getBoolean("nav_bar", false);
    }

    /**
     * 获取是否开启视频强制横屏
     */
    public boolean getIsVideoForceLandscape() {
        return sp.getBoolean("video_force_landscape", false);
    }

    //获取图标值
    public int getIcon() {
        String icon = sp.getString("custom_icon", "0");
        return Integer.parseInt(icon);
    }

    //获取滑动关闭的开关以及方向 0为禁用
    public int getSlide() {
        String slide = sp.getString("slidable", "1");
        return Integer.parseInt(slide);
    }

    /**
     * 获取是否开启视频自动播放
     */
    public boolean getIsVideoAutoPlay() {
        return sp.getBoolean("video_auto_play", false) && NetWorkUtil.isWifiConnected();
    }

    /**
     * 获取字体大小
     */
    public int getTextSize() {
        return sp.getInt("textsize", 16);
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(int textSize) {
        sp.edit().putInt("textsize", textSize).apply();
    }


    public boolean getIsFirstTime() {
        return sp.getBoolean("first_time", true);
    }

    public void setIsFirstTime(boolean flag) {
        sp.edit().putBoolean("first_time", flag).apply();
    }

    private static final class SettingUtilInstance {

        private static final SettingUtil SETTING_UTIL = new SettingUtil();
    }
}

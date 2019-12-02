package com.kotlin.toutiao.trace;

/**
 * Systrace和函数插装查看耗时，需要安装Python
 * 命令
 *   python2 /Users/lanshifu/Library/Android/sdk/platform-tools/systrace/systrace.py gfx
 *   view wm am pm ss dalvik app sched -b 90960 -a com.sample.systrace  -o test.log.html
 *
 *   可以使用浏览器打开
 *
 */
public class Trace {
    public static void i(String tag) {
        android.os.Trace.beginSection(tag);
    }

    public static void o() {
        android.os.Trace.endSection();
    }
}

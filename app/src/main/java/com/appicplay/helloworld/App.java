package com.appicplay.helloworld;

import android.app.Application;

import com.didichuxing.doraemonkit.DoKit;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zhangjian on 2022/9/9
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(this, "3953ba3cd0", true);
        new DoKit.Builder(this).productId("28919e34e0d20c9d54f2046ea924e114").build();
    }
}

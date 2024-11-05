package com.appicplay.helloworld.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class DemoHelper {

    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}

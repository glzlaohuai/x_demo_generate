package com.appicplay.helloworld;

import android.app.Application;
import android.util.Log;

import com.adup.sdk.core.APSDK;
import com.adup.sdk.core.others.APAdError;
import com.adup.sdk.core.utils.APSDKListener;
import com.appicplay.helloworld.utils.ProcessUtils;

/**
 * Created by zhangjian on 2022/9/9
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (ProcessUtils.isMainProcess(this)) {
            APSDK.init(this, getString(R.string.app_id), new APSDKListener() {
                @Override
                public void onSDKInitializeSuccess() {
                    Log.d("App", "onSDKInitializeSuccess");
                }

                @Override
                public void onSDKInitializeFail(APAdError apAdError) {
                    Log.d("App", "onSDKInitializeFail, code : " + apAdError.getCode() + " , msg : " + apAdError.getMsg());
                }
            });
        }

    }
}

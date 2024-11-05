package com.appicplay.helloworld;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.ad.listener.APAdSplashListener;
import com.adup.sdk.ad.splash.APAdSplash;
import com.adup.sdk.core.others.APAdError;
import com.adup.sdk.core.utils.CoreUtils;
import com.appicplay.helloworld.databinding.ActivitySplashBinding;

/**
 * Created by zhangjian on 2022/9/7
 */
public class SplashAdActivity extends AppCompatActivity {

    private static final String TAG = "SplashAdActivity";
    private ActivitySplashBinding mBinding;
    private boolean isUseCustomSkipView;
    private boolean isUseBottomView;
    private View mBottomView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏，适配刘海屏
        CoreUtils.setFullScreenUseStatus(this);
        mBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBottomView = getLayoutInflater().inflate(R.layout.splash_bottom, null);

        registerButtonClick();
        registerCheckBoxCheckedChange();

    }

    private void registerCheckBoxCheckedChange() {
        mBinding.skipCb.setOnCheckedChangeListener((buttonView, isChecked) -> isUseCustomSkipView = isChecked);
        mBinding.bottomCb.setOnCheckedChangeListener((buttonView, isChecked) -> isUseBottomView = isChecked);
    }

    private void registerButtonClick() {
        mBinding.loadAndShowBtn.setOnClickListener(v -> loadAndShowSplashAd());
    }


    private void loadAndShowSplashAd() {
        APAdSplash splashAd = new APAdSplash(getString(R.string.splash_slot_id), new APAdSplashListener() {
            @Override
            public void onAPAdSplashLoadSuccess(APAdSplash apAdSplash) {
                Log.d(TAG, "load ad success.");
            }

            @Override
            public void onAPAdSplashLoadFail(APAdSplash apAdSplash, APAdError apAdError) {
                Log.e(TAG, String.format("load ad failed. code: %d , msg: %s ", apAdError.getCode(), apAdError.getMsg()));
            }

            @Override
            public void onAPAdSplashPresentSuccess(APAdSplash apAdSplash) {
                Log.d(TAG, "show ad success.");
                mBinding.customFunctionView.setVisibility(View.GONE);
            }

            @Override
            public void onAPAdSplashPresentFail(APAdSplash apAdSplash, APAdError apAdError) {
                Log.e(TAG, String.format("show ad failed. code: %d , msg: %s ", apAdError.getCode(), apAdError.getMsg()));

            }

            @Override
            public void onAPAdSplashClick(APAdSplash apAdSplash) {
                Log.d(TAG, "ad clicked.");

            }

            @Override
            public void onAPAdSplashDidPresentLanding(APAdSplash apAdSplash) {

            }

            @Override
            public void onAPAdSplashDidDismissLanding(APAdSplash apAdSplash) {

            }

            @Override
            public void onAPAdSplashApplicationWillEnterBackground(APAdSplash apAdSplash) {

            }

            @Override
            public void onAPAdSplashDismiss(APAdSplash apAdSplash) {
                Log.d(TAG, "ad dismiss.");
                mBinding.adContainer.removeAllViews();
                mBinding.customFunctionView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAPAdSplashPresentTimeLeft(long l) {
                if (isUseCustomSkipView) {
                    mBinding.countdownView.setText(String.format(getString(R.string.btn_skip), ((l - 200) / 1000 + 1)));
                }
            }

            @Override
            public void onAPAdSplashRenderSuccess(APAdSplash apAdSplash) {
                Log.d(TAG, "render ad success.");
            }

            @Override
            public void onAPAdSplashDidAssembleViewFail(APAdSplash apAdSplash, APAdError apAdError) {
                Log.e(TAG, String.format("assemble ad failed. code: %d , msg: %s ", apAdError.getCode(), apAdError.getMsg()));
            }
        });
        if (isUseBottomView) {
            splashAd.setSplashBottomLayoutView(mBottomView, true);
        }
        if (isUseCustomSkipView) {
            CoreUtils.removeSelfFromParent(mBinding.customSkipView);
            mBinding.customSkipView.setVisibility(View.VISIBLE);
            splashAd.setSplashCloseButtonView(mBinding.customSkipView);

            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.RIGHT;
            layoutParams.rightMargin = 30;
            layoutParams.topMargin = 30;
            splashAd.setSplashCloseButtonPosition(layoutParams);
        }
        // 优先清理一下广告容器的内容
        mBinding.adContainer.removeAllViews();
        splashAd.loadAndPresentWithViewContainer(mBinding.adContainer);

    }
}

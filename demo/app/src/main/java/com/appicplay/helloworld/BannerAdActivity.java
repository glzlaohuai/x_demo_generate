package com.appicplay.helloworld;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.ad.banner.APAdBannerSize;
import com.adup.sdk.ad.banner.APAdBannerView;
import com.adup.sdk.ad.listener.APAdBannerViewListener;
import com.adup.sdk.core.others.APAdError;
import com.appicplay.helloworld.databinding.ActivityBannerBinding;

/**
 * Created by zhangjian on 2022/9/7
 */
public class BannerAdActivity extends AppCompatActivity {

    private static final String TAG = "BannerAdActivity";
    private ActivityBannerBinding mBinding;
    private APAdBannerSize mBannerSize = APAdBannerSize.APAdBannerSize320x50;
    private APAdBannerView mBannerView;
    private int mWidth = 300;
    private int mHeight = 50;
    private int mAdRefreshTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBannerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        registerViewListener();
    }

    private void registerViewListener() {
        mBinding.bannerStyles.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.bannerStyle50:
                    mBannerSize = APAdBannerSize.APAdBannerSize320x50;
                    break;
                case R.id.bannerStyle60:
                    mBannerSize = APAdBannerSize.APAdBannerSize468x60;
                    break;
                case R.id.bannerStyle90:
                    mBannerSize = APAdBannerSize.APAdBannerSize728x90;
                    break;
                default:
                    break;
            }
        });

        mBinding.loadAndShowBtn.setOnClickListener(v -> loadBannerAd());
    }

    private void loadBannerAd() {
        if (mBannerView != null) {
            mBannerView.destroy();
            mBannerView = null;
            mBinding.adContainer.removeAllViews();
        }
        mBannerView = new APAdBannerView(getString(R.string.banner_slot_id), mBannerSize, new APAdBannerViewListener() {
            @Override
            public void onAPAdBannerViewLoadSuccess(APAdBannerView ad) {
                Log.d(TAG, "ad load success.");
            }

            @Override
            public void onAPAdBannerViewLoadFail(APAdBannerView ad, APAdError err) {
                Log.e(TAG, "横幅广告加载失败，错误代码：" + err.getCode() + "，错误描述：" + err.getMsg());

            }

            @Override
            public void onAPAdBannerViewPresentSuccess(APAdBannerView ad) {
                Log.d(TAG, "横幅广告展示成功");
            }

            @Override
            public void onAPAdBannerViewClick(APAdBannerView ad) {
                Log.d(TAG, "横幅广告被点击");
            }

        });
        mBinding.adContainer.addView(mBannerView);

        if (!TextUtils.isEmpty(mBinding.bannerWidth.getText().toString())) {
            try {
                mWidth = Integer.parseInt(mBinding.bannerWidth.getText().toString().trim());
            } catch (Exception e) {
            }
        }
        if (!TextUtils.isEmpty(mBinding.bannerHeight.getText().toString())) {
            try {
                mHeight = Integer.parseInt(mBinding.bannerHeight.getText().toString().trim());
            } catch (Exception e) {
            }
        }
        if (!TextUtils.isEmpty(mBinding.bannerRefresh.getText().toString())) {
            try {
                mAdRefreshTime = Integer.parseInt(mBinding.bannerRefresh.getText().toString().trim());
            } catch (Exception e) {
            }
        }

        mBannerView.setImageAcceptedSize(mWidth, mHeight);
        mBannerView.setRefreshTimer(mAdRefreshTime);
        mBannerView.load();
    }
}

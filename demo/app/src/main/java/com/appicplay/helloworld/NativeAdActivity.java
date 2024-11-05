package com.appicplay.helloworld;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.ad.base.nativ.APNativeViewBinder;
import com.adup.sdk.ad.listener.APAdNativeListener;
import com.adup.sdk.ad.listener.APAdNativeVideoViewListener;
import com.adup.sdk.ad.nativ.APAdNative;
import com.adup.sdk.ad.nativ.APAdNativeVideoState;
import com.adup.sdk.ad.nativ.APAdNativeVideoView;
import com.adup.sdk.core.others.APAdError;
import com.appicplay.helloworld.databinding.ActivityNativBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjian on 2022/9/7
 */
public class NativeAdActivity extends AppCompatActivity {

    private static final String TAG = "NativeAdActivity";
    private ActivityNativBinding mBinding;
    private APAdNative mNativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNativBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        registerButtonClick();
    }

    private void registerButtonClick() {
        mBinding.loadAndShowBtn.setOnClickListener(v -> {

            if (mNativeAd != null) {
                mNativeAd.destroy();
                mNativeAd = null;
            }
            releaseView();

            mNativeAd = new APAdNative(getString(R.string.native_slot_id), new APAdNativeListener() {
                @Override
                public void onApAdNativeDidLoadSuccess(APAdNative apAdNative) {
                    Log.d(TAG, "load ad success.");
                    showNativeAd(apAdNative);
                }

                @Override
                public void onApAdNativeDidLoadFail(APAdNative apAdNative, APAdError apAdError) {

                }

                @Override
                public void onAPAdNativePresentSuccess(APAdNative apAdNative) {

                }

                @Override
                public void onApAdNativeDidClick(APAdNative apAdNative) {

                }

                @Override
                public void onApAdNativeDidPresentLanding(APAdNative apAdNative) {

                }

                @Override
                public void onApAdNativeDidDismissLanding(APAdNative apAdNative) {

                }

                @Override
                public void onApAdNativeApplicationWillEnterBackground(APAdNative apAdNative) {

                }
            });
            mNativeAd.load();
        });
    }

    private boolean isVideoPause;
    private boolean isVideoMute;

    private void showNativeAd(APAdNative nativeAd) {
        List<View> clickViews = new ArrayList<>();
        mBinding.adContainer.setVisibility(View.VISIBLE);
        final APAdNativeVideoView nativeExpressVideoView = nativeAd.getAPAdVideo();
        if (nativeExpressVideoView != null) {
            mBinding.adLargeImageContainer.setVisibility(View.GONE);
            mBinding.adVideoContainerLayout.setVisibility(View.VISIBLE);
            nativeExpressVideoView.setMute(true);
            mBinding.playBtn.setOnClickListener(v -> {
                if (!isVideoPause) {
                    nativeExpressVideoView.pause();
                    isVideoPause = true;
                    mBinding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                    return;
                }
                mBinding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                nativeExpressVideoView.play();
                isVideoPause = false;
            });

            mBinding.voiceBtn.setOnClickListener(v -> {
                if (!isVideoMute) {
                    mBinding.voiceBtn.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    isVideoMute = true;
                } else {
                    mBinding.voiceBtn.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    isVideoMute = false;
                }
                nativeExpressVideoView.setMute(isVideoMute);
            });

            nativeExpressVideoView.setApAdNativeVideoViewListener(new APAdNativeVideoViewListener() {
                @Override
                public void onAPAdNativeVideoViewDidChangeState(APAdNativeVideoView apAdNativeVideoView, APAdNativeVideoState state) {
                    String stateMsg = state.getState() == 0 ? "播放失败" : state.getState() == 2 ? "播放中" : state.getState() == 3 ? "播放完成" : state.getState() == 4 ? "播放暂停" : "";

                    Log.d(TAG, "原生视频广告状态：" + stateMsg);
                }

                @Override
                public void onAPAdNativeVideoViewDidPlayFinish(APAdNativeVideoView ad) {
                    Log.d(TAG, "原生视频广告播放完成");

                }
            });
            mBinding.adVideoContainer.addView(nativeExpressVideoView);
            clickViews.add(mBinding.adVideoContainer);
        } else {
            mBinding.adLargeImageContainer.setVisibility(View.VISIBLE);
            mBinding.adVideoContainerLayout.setVisibility(View.GONE);
        }

        if (nativeAd.getAPAdIcon() != null) {
            mBinding.adIcon.setImageDrawable(nativeAd.getAPAdIcon());
        } else {
            String iconUrl = nativeAd.getAPAdIconUrl();
            Glide.with(this).load(TextUtils.isEmpty(iconUrl) ? "" : iconUrl).placeholder(R.mipmap.ic_launcher).into(mBinding.adIcon);
        }

        if (nativeAd.getAPAdScreenshot() != null) {
            mBinding.adLargeImageContainer.setImageDrawable(nativeAd.getAPAdScreenshot());
        } else {
            String imgUrl = nativeAd.getAPAdScreenshotUrl();
            Glide.with(this).load(TextUtils.isEmpty(imgUrl) ? "" : imgUrl).placeholder(R.mipmap.ic_launcher).into(mBinding.adLargeImageContainer);
        }
        String title = nativeAd.getAPAdTitle();
        mBinding.adTitle.setText(TextUtils.isEmpty(title) ? "defaule title" : title);
        String desc = nativeAd.getAPAdDescription();
        mBinding.adDesc.setText(TextUtils.isEmpty(desc) ? "default desc" : desc);
        clickViews.add(mBinding.adTitle);
        clickViews.add(mBinding.adDesc);
        clickViews.add(mBinding.adIcon);
        clickViews.add(mBinding.adLargeImageContainer);

        nativeAd.setViewBinder(APNativeViewBinder.Builder.create().setTitleViewID(mBinding.adTitle.getId()).setDescViewID(mBinding.adDesc.getId()).setIconViewID(mBinding.adIcon.getId()).setScreenshotViewID(mBinding.adLargeImageContainer.getId()).build());
        nativeAd.bindAdToView(mBinding.adContainer, clickViews);
    }


    private void releaseView() {
        mBinding.adContainer.setVisibility(View.GONE);
        mBinding.adVideoContainer.removeAllViews();
        mBinding.adTitle.setText("");
        mBinding.adDesc.setText("");
        mBinding.adIcon.setImageResource(R.mipmap.ic_launcher);
        mBinding.adLargeImageContainer.setImageResource(R.mipmap.ic_launcher);
        mBinding.voiceBtn.setImageResource(R.drawable.ic_baseline_volume_off_24);
        mBinding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

        isVideoMute = false;
        isVideoPause = false;

    }
}

package com.appicplay.helloworld;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.ad.listener.APAdRewardVideoListener;
import com.adup.sdk.ad.video.APAdRewardVideo;
import com.adup.sdk.core.others.APAdError;
import com.appicplay.helloworld.databinding.ActivityVideoBinding;

/**
 * Created by zhangjian on 2022/9/7
 */
public class RewardVideoAdActivity extends AppCompatActivity {


    private static final String TAG = "RewardVideoAdActivity";
    private ActivityVideoBinding mBinding;
    private APAdRewardVideo mAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        registerButtonClick();
    }

    private void registerButtonClick() {
        mBinding.loadBtn.setOnClickListener(v -> {
            loadAd();
        });
        mBinding.showBtn.setOnClickListener(v -> {
            if (mAd == null) {
                mBinding.showBtn.setEnabled(false);
                mBinding.loadBtn.setEnabled(true);
                Toast.makeText(this, "ad show failed.", Toast.LENGTH_SHORT).show();
                return;
            }
            mAd.presentWithViewContainer(this);
        });
    }

    private void loadAd() {
        if (mAd != null) {
            mAd = null;
        }
        mAd = new APAdRewardVideo(getString(R.string.reward_video_slot_id), new APAdRewardVideoListener() {
            @Override
            public void onAPAdRewardVideoLoadSuccess(APAdRewardVideo apAdRewardVideo) {
                mBinding.loadBtn.setEnabled(false);
                mBinding.showBtn.setEnabled(true);
                Log.d(TAG, "load ad success.");
            }

            @Override
            public void onAPAdRewardVideoLoadFail(APAdRewardVideo apAdRewardVideo, APAdError err) {
                Log.e(TAG, String.format("load ad failed. code : %d, msg: %s", err.getCode(), err.getMsg()));

            }

            @Override
            public void onAPAdRewardVideoPresentSuccess(APAdRewardVideo apAdRewardVideo) {
                Log.d(TAG, "show ad success.");

            }

            @Override
            public void onAPAdRewardVideoPresentFail(APAdRewardVideo apAdRewardVideo, APAdError err) {
                Log.e(TAG, String.format("show ad failed. code : %d, msg: %s", err.getCode(), err.getMsg()));

            }

            @Override
            public void onAPAdRewardVideoClick(APAdRewardVideo apAdRewardVideo) {
                Log.d(TAG, "ad clicked.");

            }

            @Override
            public void onAPAdRewardVideoDidPlayComplete(APAdRewardVideo apAdRewardVideo) {
                Log.d(TAG, "ad play complete.");

            }

            @Override
            public void onAPAdRewardVideoDismiss(APAdRewardVideo apAdRewardVideo) {
                Log.d(TAG, "ad closed.");
                mBinding.loadBtn.setEnabled(true);
                mBinding.showBtn.setEnabled(false);
            }

        });
        mAd.load();
    }


}

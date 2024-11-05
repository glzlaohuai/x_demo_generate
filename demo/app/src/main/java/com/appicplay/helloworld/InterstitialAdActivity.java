package com.appicplay.helloworld;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.ad.interstitial.APAdInterstitial;
import com.adup.sdk.ad.listener.APAdInterstitialListener;
import com.adup.sdk.core.others.APAdError;
import com.appicplay.helloworld.R;
import com.appicplay.helloworld.databinding.ActivityInterstitialBinding;

/**
 * Created by zhangjian on 2022/9/7
 */
public class InterstitialAdActivity extends AppCompatActivity {


    private static final String TAG = "InterstitialAdActivity";
    private ActivityInterstitialBinding mBinding;
    private APAdInterstitial mAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityInterstitialBinding.inflate(getLayoutInflater());
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
            mAd.presentWithActivity(InterstitialAdActivity.this);
        });
    }

    private void loadAd() {
        if (mAd != null) {
            mAd.destroy();
            mAd = null;
        }
        mAd = new APAdInterstitial(getString(R.string.interstitial_slot_id), new APAdInterstitialListener() {
            @Override
            public void onApAdInterstitialLoadSuccess(APAdInterstitial ad) {
                mBinding.loadBtn.setEnabled(false);
                mBinding.showBtn.setEnabled(true);
                Log.d(TAG, "load ad success.");
            }

            @Override
            public void onApAdInterstitialLoadFail(APAdInterstitial ad, APAdError err) {
                Log.e(TAG, String.format("load ad failed. code : %d, msg: %s", err.getCode(), err.getMsg()));
            }

            @Override
            public void onApAdInterstitialPresentSuccess(APAdInterstitial ad) {
                Log.d(TAG, "show ad success.");
            }

            @Override
            public void onApAdInterstitialPresentFail(APAdInterstitial ad, APAdError err) {
                Log.e(TAG, String.format("show ad failed. code : %d, msg: %s", err.getCode(), err.getMsg()));

            }

            @Override
            public void onApAdInterstitialClick(APAdInterstitial ad) {
                Log.d(TAG, "ad clicked.");
            }

            @Override
            public void onApAdInterstitialDidPresentLanding(APAdInterstitial ad) {
                Log.d(TAG, "onApAdInterstitialDidPresentLanding: ");
            }

            @Override
            public void onApAdInterstitialDidDismissLanding(APAdInterstitial ad) {
                Log.d(TAG, "onApAdInterstitialDidDismissLanding");
            }

            @Override
            public void onApAdInterstitialApplicationWillEnterBackground(APAdInterstitial ad) {
                Log.d(TAG, "onApAdInterstitialApplicationWillEnterBackground: ");
            }

            @Override
            public void onAPAdInterstitialDismiss(APAdInterstitial ad) {
                Log.d(TAG, "ad closed.");
                mBinding.loadBtn.setEnabled(true);
                mBinding.showBtn.setEnabled(false);
            }
        });
        mAd.load();
    }


}

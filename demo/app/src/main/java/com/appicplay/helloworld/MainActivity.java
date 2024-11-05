package com.appicplay.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.adup.sdk.core.APSDK;
import com.adup.sdk.core.utils.CoreUtils;
import com.adup.sdk.core.utils.pool.APThreadPool;
import com.appicplay.helloworld.databinding.ActivityMainBinding;
import com.appicplay.helloworld.utils.AdvertisingIdClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindButtonToClick(binding.btnNativeAd, NativeAdActivity.class);
        bindButtonToClick(binding.btnBannerAd, BannerAdActivity.class);
        bindButtonToClick(binding.btnInterstitialAd, InterstitialAdActivity.class);
        bindButtonToClick(binding.btnRewardVideoAd, RewardVideoAdActivity.class);
        bindButtonToClick(binding.btnSplashAd, SplashAdActivity.class);

        APThreadPool.getInstance().exec(() -> {
            try {
                AdvertisingIdClient.AdInfo info = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this);
                runOnUiThread(() -> binding.deviceInfoContent.setText(String.format(getString(R.string.info), APSDK.getSdkVersion(), CoreUtils.getOAID(MainActivity.this), info.getId())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }


    private void bindButtonToClick(View view, final Class<?> clz) {
        view.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, clz)));
    }


}
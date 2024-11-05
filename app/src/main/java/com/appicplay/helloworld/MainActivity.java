package com.appicplay.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ap.android.trunk.sdk.core.APSDK;
import com.ap.android.trunk.sdk.core.others.APAdError;
import com.ap.android.trunk.sdk.core.utils.APSDKListener;
import com.ap.android.trunk.sdk.core.utils.CoreUtils;
import com.ap.android.trunk.sdk.core.utils.pool.APThreadPool;
import com.ap.android.trunk.sdk.debug.activity.APADDebugActivity;
import com.appicplay.helloworld.databinding.ActivityMainBinding;
import com.appicplay.helloworld.utils.AdvertisingIdClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        APThreadPool.getInstance().exec(() -> {
            try {
                AdvertisingIdClient.AdInfo info = AdvertisingIdClient.getAdvertisingIdInfo(MainActivity.this);
                runOnUiThread(() -> binding.deviceInfoContent.setText(String.format(getString(R.string.info), APSDK.getSdkVersion(), CoreUtils.getOAID(MainActivity.this), info.getId())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APSDK.init(MainActivity.this, "__appid__", new APSDKListener() {
                    @Override
                    public void onSDKInitializeSuccess() {

                    }

                    @Override
                    public void onSDKInitializeFail(APAdError apAdError) {

                    }
                });
            }
        });

        binding.btnDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, APADDebugActivity.class);
                startActivity(intent);
            }
        });

    }




}
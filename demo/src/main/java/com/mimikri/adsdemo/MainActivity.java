package com.mimikri.adsdemo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mimikri.ads.format.AdNetwork;
import com.mimikri.ads.format.BannerAd;
import com.mimikri.ads.format.InterstitialAd;
import com.mimikri.ads.format.MediumRectangleAd;
import com.mimikri.ads.format.NativeAd;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    AdNetwork.Initialize adNetwork;
    BannerAd.Builder bannerAd;
    MediumRectangleAd.Builder mediumRectangleAd;
    InterstitialAd.Builder interstitialAd;
    NativeAd.Builder nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initAds();
        loadBannerAd();
        loadInterstitialAd();
        loadNativeAd();

        findViewById(R.id.btn_interstitial).setOnClickListener(v -> {
           // startActivity(new Intent(getApplicationContext(), SecondActivity.class));
            showInterstitialAd();
           // bannerAd.destroyAndDetachBanner();
        });

    }

    private void initAds() {
        adNetwork = new AdNetwork.Initialize(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setStartappAppId(Constant.STARTAPP_APP_ID)
                .setAppLovinSdkKey(getResources().getString(R.string.applovin_sdk_key))
                .setIronSourceAppKey(Constant.IRONSOURCE_APP_KEY)
                .setUnityGameId(Constant.UNITY_GAME_ID)
                .setPangleAppId(Constant.PANGLE_AD_APP_ID)
                .setDebug(true)
                .build();
    }

    private void loadBannerAd() {
        bannerAd = new BannerAd.Builder(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setFanBannerId(Constant.FAN_BANNER_ID)
                .setAppLovinBannerId(Constant.APPLOVIN_BANNER_ID)
                .setAppLovinBannerZoneId(Constant.APPLOVIN_BANNER_ZONE_ID)
                .setIronSourceBannerId(Constant.IRONSOURCE_BANNER_ID)
                .setPangleBannerId(Constant.PANGLE_RIT_BANNER_320X50)
                .setUnityBannerId(Constant.UNITY_BANNER_ID)
                .setDarkTheme(false)
                .build();
    }

    private void loadMediumRectangleAd() {
        mediumRectangleAd = new MediumRectangleAd.Builder(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setFanBannerId(Constant.FAN_BANNER_ID)
                .setAppLovinBannerId(Constant.APPLOVIN_BANNER_ID)
                .setAppLovinBannerZoneId(Constant.APPLOVIN_BANNER_ZONE_ID)
                .setIronSourceBannerId(Constant.IRONSOURCE_BANNER_ID)
                .setDarkTheme(false)
                .build();
    }

    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd.Builder(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setFanInterstitialId(Constant.FAN_INTERSTITIAL_ID)
                .setAppLovinInterstitialId(Constant.APPLOVIN_INTERSTITIAL_ID)
                .setAppLovinInterstitialZoneId(Constant.APPLOVIN_INTERSTITIAL_ZONE_ID)
                .setIronSourceInterstitialId(Constant.IRONSOURCE_INTERSTITIAL_ID)
                .setPangleInterstitialId(Constant.RIT_INTER_HORIZONTAL)
                .setUnityInterstitialId(Constant.UNITY_INTERSTITIAL_ID)
          //      .setPlacementStatus(1)
                .setInterval(1)
                .build();
    }

    private void showInterstitialAd() {
        interstitialAd.show();
    }

    private void loadNativeAd() {
        nativeAd = new NativeAd.Builder(this)
                .setAdStatus(Constant.AD_STATUS)
                .setAdNetwork(Constant.AD_NETWORK)
                .setBackupAdNetwork(Constant.BACKUP_AD_NETWORK)
                .setFanNativeId(Constant.FAN_NATIVE_ID)
                .setAppLovinNativeId(Constant.APPLOVIN_NATIVE_MANUAL_ID)
                .setNativeAdStyle(Constant.STYLE_DEFAULT)
                .setDarkTheme(false)
                .build();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bannerAd.destroyAndDetachBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBannerAd();
    }

}
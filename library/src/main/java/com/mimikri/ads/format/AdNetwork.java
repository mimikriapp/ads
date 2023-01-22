package com.mimikri.ads.format;

import android.app.Activity;
import android.text.Html;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.ironsource.mediationsdk.IronSource;
import com.mimikri.ads.R;
import com.mimikri.ads.helper.AudienceNetworkInitializeHelper;
import com.mimikri.ads.util.Constant;

import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class AdNetwork {

    public static class Initialize {

        private static final String TAG = "AdNetwork";
        Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String startappAppId = "0";
        private String appLovinSdkKey = "";
        private String mopubBannerId = "";
        private String ironSourceAppKey = "";
        private boolean debug = true;

        public Initialize(Activity activity) {
            this.activity = activity;
        }

        public Initialize build() {
            initAds();
            initBackupAds();
            return this;
        }

        public Initialize setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Initialize setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Initialize setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }


        public Initialize setStartappAppId(String startappAppId) {
            this.startappAppId = startappAppId;
            return this;
        }


        public Initialize setAppLovinSdkKey(String appLovinSdkKey) {
            this.appLovinSdkKey = appLovinSdkKey;
            return this;
        }

        public Initialize setMopubBannerId(String mopubBannerId) {
            this.mopubBannerId = mopubBannerId;
            return this;
        }

        public Initialize setIronSourceAppKey(String ironSourceAppKey) {
            this.ironSourceAppKey = ironSourceAppKey;
            return this;
        }

        public Initialize setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public void initAds() {
            if (adStatus.equals(Constant.AD_STATUS_ON)) {
                switch (adNetwork) {
                    case Constant.ADMOB:
                   // case GOOGLE_AD_MANAGER:
                    case Constant.FAN:
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case Constant.STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case Constant.UNITY:
                    case Constant.APPLOVIN:
                    case Constant.APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;

                    case Constant.APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;

                    case Constant.IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, ironSourceAppKey);
                        break;
                }
                Log.d(TAG, "[" + adNetwork + "] is selected as Primary Ads");
            }
        }

        public void initBackupAds() {
            if (adStatus.equals(Constant.AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case Constant.ADMOB:
                    //case GOOGLE_AD_MANAGER:
                   // case FAN_BIDDING_ADMOB:

                    case Constant.STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case Constant.UNITY:
                    case Constant.APPLOVIN:
                    case Constant.APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;

                    case Constant.APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;

                    case Constant.IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, ironSourceAppKey);
                        break;

                    case Constant.NONE:
                        //do nothing
                        break;
                }
                Log.d(TAG, "[" + backupAdNetwork + "] is selected as Backup Ads");
            }
        }

    }

}

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
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.mediation.IInitializationListener;
import com.unity3d.mediation.InitializationConfiguration;
import com.unity3d.mediation.UnityMediation;
import com.unity3d.mediation.errors.SdkInitializationError;

import static com.mimikri.ads.util.Constant.PANGLE;
import static com.mimikri.ads.util.Constant.UNITY;
import static com.unity3d.services.core.properties.ClientProperties.getApplicationContext;

public class AdNetwork {

    public static class Initialize {

        private static final String TAG = "AdNetwork";
        Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String startappAppId = "0";
        private String appLovinSdkKey = "";
        private String unityGameId = "";
        private String pangleAppId = "";
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
        public Initialize setPangleAppId(String pangleAppId) {
            this.pangleAppId = pangleAppId;
            return this;
        }

        public Initialize setUnityGameId(String unityGameId) {
            this.unityGameId = unityGameId;
            return this;
        }

        public Initialize setAppLovinSdkKey(String appLovinSdkKey) {
            this.appLovinSdkKey = appLovinSdkKey;
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
                    case UNITY:
                        InitializationConfiguration configuration = InitializationConfiguration.builder()
                                .setGameId(unityGameId)
                                .setInitializationListener(new IInitializationListener() {
                                    @Override
                                    public void onInitializationComplete() {
                                        Log.d(TAG, "Unity Mediation is successfully initialized. with ID : " + unityGameId);
                                    }

                                    @Override
                                    public void onInitializationFailed(SdkInitializationError errorCode, String msg) {
                                        Log.d(TAG, "Unity Mediation Failed to Initialize : " + msg);
                                    }
                                }).build();
                        UnityMediation.initialize(configuration);
                        break;

                   /* case PANGLE:
                        Log.d(TAG, PANGLE + "ADS  " + unityGameId);
                        PAGConfig pAGInitConfig = new PAGConfig.Builder()
                                .appId(pangleAppId)
                                .debugLog(debug)
                                .supportMultiProcess(false)
                                .build();
                        PAGSdk.init(getApplicationContext(), pAGInitConfig, new PAGSdk.PAGInitCallback() {
                            @Override
                            public void success() {
                                Log.i(TAG, "PANGLE ADS is successfully initialized");
                            }
                            @Override
                            public void fail(int code, String msg) {
                                Log.i(TAG, "PANGLE ADS Failed to Initialize :  " + code);
                            }
                        });

                        break;

                    */

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
                    case UNITY:
                        InitializationConfiguration configuration = InitializationConfiguration.builder()
                                .setGameId(unityGameId)
                                .setInitializationListener(new IInitializationListener() {
                                    @Override
                                    public void onInitializationComplete() {
                                        Log.d(TAG, "Unity Mediation is successfully initialized. with ID : " + unityGameId);
                                    }

                                    @Override
                                    public void onInitializationFailed(SdkInitializationError errorCode, String msg) {
                                        Log.d(TAG, "Unity Mediation Failed to Initialize : " + msg);
                                    }
                                }).build();
                        UnityMediation.initialize(configuration);
                        break;

               /*     case PANGLE:
                        PAGConfig pAGInitConfig = new PAGConfig.Builder()
                                .appId(pangleAppId)
                                .debugLog(debug)
                                .supportMultiProcess(false)
                                .build();
                        PAGSdk.init(getApplicationContext(), pAGInitConfig, new PAGSdk.PAGInitCallback() {
                            @Override
                            public void success() {
                                Log.i(TAG, "PANGLE ADS is successfully initialized");
                            }
                            @Override
                            public void fail(int code, String msg) {
                                Log.i(TAG, "PANGLE ADS Failed to Initialize :  " + code);
                            }
                        });

                        break;

                */
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

package com.mimikri.ads.format;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;

import com.facebook.ads.InterstitialAdListener;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

import com.mimikri.ads.util.Constant;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.mediation.IInterstitialAdLoadListener;
import com.unity3d.mediation.IInterstitialAdShowListener;
import com.unity3d.mediation.errors.LoadError;
import com.unity3d.mediation.errors.ShowError;

import java.util.concurrent.TimeUnit;

import static com.mimikri.ads.util.Constant.PANGLE;
import static com.mimikri.ads.util.Constant.UNITY;

public class InterstitialAd {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;

        private com.facebook.ads.InterstitialAd fanInterstitialAd;
        private StartAppAd startAppAd;
        private com.unity3d.mediation.InterstitialAd unityInterstitialAd;
        private MaxInterstitialAd maxInterstitialAd;
        //private PAGInterstitialAd interstitialAd;

        public AppLovinInterstitialAdDialog appLovinInterstitialAdDialog;
        public AppLovinAd appLovinAd;
        private int retryAttempt;
        private int counter = 1;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String fanInterstitialId = "";
        private String appLovinInterstitialId = "";
        private String appLovinInterstitialZoneId = "";
        private String unityInterstitialId = "";
        private String pangleInterstitialId = "";
        private String ironSourceInterstitialId = "";
        private int placementStatus = 1;
        private int interval = 3;

        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadInterstitialAd();
            return this;
        }

        public void show() {
            showInterstitialAd();
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }



        public Builder setFanInterstitialId(String fanInterstitialId) {
            this.fanInterstitialId = fanInterstitialId;
            return this;
        }


        public Builder setAppLovinInterstitialId(String appLovinInterstitialId) {
            this.appLovinInterstitialId = appLovinInterstitialId;
            return this;
        }

        public Builder setAppLovinInterstitialZoneId(String appLovinInterstitialZoneId) {
            this.appLovinInterstitialZoneId = appLovinInterstitialZoneId;
            return this;
        }
        public Builder setUnityInterstitialId(String unityInterstitialId) {
            this.unityInterstitialId = unityInterstitialId;
            return this;
        }
        public Builder setPangleInterstitialId(String pangleInterstitialId) {
            this.pangleInterstitialId = pangleInterstitialId;
            return this;
        }

        public Builder setIronSourceInterstitialId(String ironSourceInterstitialId) {
            this.ironSourceInterstitialId = ironSourceInterstitialId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadInterstitialAd() {
            Log.d(TAG, "loadInterstitialAd");
            if (adStatus.equals(Constant.AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "loadInterstitialAd OK");
                switch (adNetwork) {
                    case Constant.FAN:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case Constant.STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                                loadBackupInterstitialAd();
                            }
                        });
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                                loadBackupInterstitialAd();
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;
                   /* case PANGLE:
                        Log.d(TAG, "PANGLE interstitial ad choosed");
                      PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error "+ code + " " + message);
                                        loadBackupInterstitialAd();
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });



                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                new PAGInterstitialRequest(),
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                        loadBackupInterstitialAd();
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd pagInterstitialAd) {
                                        interstitialAd = pagInterstitialAd;
                                        Log.d(TAG, "Pangle Interstitial Ad loaded...");
                                    }
                                });





                        break;


                    */
                    case Constant.APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                loadBackupInterstitialAd();
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case Constant.APPLOVIN_DISCOVERY:
                      //  AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                       // builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadBackupInterstitialAd();
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case Constant.IRONSOURCE:
                        IronSource.setInterstitialListener(new InterstitialListener() {
                            @Override
                            public void onInterstitialAdReady() {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onInterstitialAdOpened() {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onInterstitialAdClosed() {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }

                            @Override
                            public void onInterstitialAdShowSucceeded() {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onInterstitialAdClicked() {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }
                        });
                        IronSource.loadInterstitial();
                        break;
                }
            }
        }

        public void loadBackupInterstitialAd() {
            if (adStatus.equals(Constant.AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case Constant.FAN:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case Constant.STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;

                    case UNITY:
                        unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, unityInterstitialId);
                        final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                            @Override
                            public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                Log.d(TAG, "unity interstitial ad loaded");
                            }

                            @Override
                            public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                                Log.e(TAG, "Unity Ads failed to load ad : " + unityInterstitialId + " : error : " + s);
                                loadBackupInterstitialAd();
                            }

                        };
                        unityInterstitialAd.load(unityAdLoadListener);
                        break;
                  /*  case PANGLE:
                        PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });
                        break;

                   */
                    case Constant.APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case Constant.APPLOVIN_DISCOVERY:
                      //  AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                       // builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;
                    case Constant.IRONSOURCE:
                        IronSource.setInterstitialListener(new InterstitialListener() {
                            @Override
                            public void onInterstitialAdReady() {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onInterstitialAdOpened() {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onInterstitialAdClosed() {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }

                            @Override
                            public void onInterstitialAdShowSucceeded() {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onInterstitialAdClicked() {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case Constant.NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd() {
            Log.d(TAG, "showInterstitialAd");
            if (adStatus.equals(Constant.AD_STATUS_ON) && placementStatus != 0) {
                if (counter == interval) {
                    switch (adNetwork) {
                        case Constant.FAN:
                            if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                                fanInterstitialAd.show();
                                Log.d(TAG, "fan interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "fan interstitial null");
                            }
                            break;

                        case Constant.STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd();
                                Log.d(TAG, "startapp interstitial not null [counter] : " + counter);
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "startapp interstitial null");
                            }
                            break;


                        case UNITY:
                            final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                                @Override
                                public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                                }

                                @Override
                                public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                    Log.d(TAG, "unity ads show failure");
                                    showBackupInterstitialAd();
                                }
                            };
                            unityInterstitialAd.show(showListener);
                            break;
                 /*       case PANGLE:
                            /*PAGInterstitialRequest request = new PAGInterstitialRequest();
                            PAGInterstitialAd.loadAd(pangleInterstitialId,
                                    request,
                                    new PAGInterstitialAdLoadListener() {
                                        @Override
                                        public void onError(int code, String message) {
                                            Log.d(TAG, "PANGLE interstitial ad error");

                                        }

                                        @Override
                                        public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                            Log.d(TAG, "PANGLE interstitial ad loaded");
                                        }
                                    });


                          //  interstitialAd.show(activity);
                            if (interstitialAd != null) {
                                interstitialAd.show(activity);
                            }
                            //else {loadInterstitialAd();}

                            break;

                  */

                        case Constant.APPLOVIN_MAX:
                            if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                                Log.d(TAG, "ready : " + counter);
                                maxInterstitialAd.showAd();
                                Log.d(TAG, "show ad");
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;

                        case Constant.APPLOVIN_DISCOVERY:
                            if (appLovinInterstitialAdDialog != null) {
                                appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                            }
                            break;

                        case Constant.IRONSOURCE:
                            if (IronSource.isInterstitialReady()) {
                                IronSource.showInterstitial(ironSourceInterstitialId);
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;
                    }
                    counter = 1;
                } else {
                    counter++;
                }
                Log.d(TAG, "Current counter : " + counter);
            }
        }

        public void showBackupInterstitialAd() {
            if (adStatus.equals(Constant.AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case Constant.FAN:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                        }
                        break;

                    case Constant.STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                        }
                        break;

                    case UNITY:
                        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                            @Override
                            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                Log.d(TAG, "unity ads show failure");
                            }
                        };
                        unityInterstitialAd.show(showListener);
                        break;
                  /*  case PANGLE:

                        if (interstitialAd != null) {
                            interstitialAd.show(activity);
                        }
                        break;

                   */
                    case Constant.APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            maxInterstitialAd.showAd();
                        }
                        break;

                    case Constant.APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;


                    case Constant.IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(ironSourceInterstitialId);
                        }
                        break;

                    case Constant.NONE:
                        //do nothing
                        break;
                }
            }
        }

    }

}

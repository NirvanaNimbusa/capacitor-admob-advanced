package com.devutex.admobadvanced;

import android.Manifest;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


@NativePlugin(
    permissions = {
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET
    }
)
public class AdmobAdvanced extends Plugin {

    private PluginCall call;
    private ViewGroup viewGroup;
    private RelativeLayout adViewLayout;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private RewardedVideoAd rewardedVideoAd;
    private String personalisedAds = "1";

    // Initialize Admob
    @PluginMethod()
    public void initialise(final PluginCall call) {
        this.call = call;
        String appId = call.getString("appIdAndroid", "ca-app-pub-3940256099942544~3347511713");
        try {
            MobileAds.initialize(this.getContext(), appId);
            viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    // Initialize AdMob with Consent SDK
    @PluginMethod()
    public void initialiseWithConsent(final PluginCall call) {
        this.call = call;
        String appId = call.getString("appIdAndroid", "ca-app-pub-3940256099942544~3347511713");
        try {
            MobileAds.initialize(this.getContext(), appId);
            viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
        final boolean tfua = call.getBoolean("tagUnderAgeOfConsent", false);
        ConsentInformation consentInformation = ConsentInformation.getInstance(getContext());
        consentInformation.setTagForUnderAgeOfConsent(tfua);
        String[] publisherId = {call.getString("publisherId", "pub-0123456789012345")};
        consentInformation.requestConsentInfoUpdate(publisherId, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.

                if(tfua) {
                    call.success(new JSObject().put("consentStatus", "NON-PERSONALIZED"));
                } else {
                    if (ConsentInformation.getInstance(getContext()).isRequestLocationInEeaOrUnknown()) {
                        call.success(new JSObject().put("consentStatus", consentStatus));
                    } else {
                        call.success(new JSObject().put("consentStatus", "PERSONALIZED"));
                    }
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                call.error(errorDescription);
            }
        });
    }

    @PluginMethod()
    public void showGoogleConsentForm(final PluginCall call){
        this.call = call;
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(call.getString("privacyPolicyURL", null));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(call.getBoolean("showAdFreeOption", false)) {
            final ConsentForm form = new ConsentForm.Builder(getContext(), privacyUrl)
                    .withListener(new ConsentFormListener() {
                        @Override
                        public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                            // Consent form was closed.
                            if (userPrefersAdFree) {
                                call.success(new JSObject().put("consentStatus", "ADFREE"));
                            } else {
                                call.success(new JSObject().put("consentStatus", consentStatus));
                            }
                        }

                        @Override
                        public void onConsentFormError(String errorDescription) {
                            // Consent form error.
                            call.error(errorDescription);
                        }
                    })
                    .withPersonalizedAdsOption()
                    .withNonPersonalizedAdsOption()
                    .withAdFreeOption()
                    .build();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    form.load();
                    form.show();
                }
            });

        } else {
            final ConsentForm form = new ConsentForm.Builder(getActivity().getApplicationContext(), privacyUrl)
                    .withListener(new ConsentFormListener() {
                        @Override
                        public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                            // Consent form was closed.
                            call.success(new JSObject().put("consentStatus", consentStatus));
                        }

                        @Override
                        public void onConsentFormError(String errorDescription) {
                            // Consent form error.
                            call.error(errorDescription);
                        }
                    })
                    .withPersonalizedAdsOption()
                    .withNonPersonalizedAdsOption()
                    .build();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    form.load();
                    form.show();
                }
            });
        }
    }

    @PluginMethod()
    public void updateAdExtras(final PluginCall call){
        this.call = call;
        if(call.getBoolean("personalizedAds", false)) {
            this.personalisedAds = "0";
        } else {
            this.personalisedAds = "1";
        }
    }

    // Show a banner Ad
    @PluginMethod()
    public void showBanner(final PluginCall call) {
        this.call = call;
        String adId;
        if (call.getBoolean("isTesting",false)) {
            adId= "ca-app-pub-3940256099942544/6300978111";
        } else {
            adId= call.getString("adIdAndroid", "ca-app-pub-3940256099942544/6300978111");
        }
        String adSize     = call.getString("adSize", "SMART_BANNER");
        String adPosition = call.getString("adPosition", "BOTTOM");
        try {
            if (adView == null) {
                adView = new AdView(getContext());
                adView.setAdUnitId(adId);

                switch (adSize) {
                    case "SMART_BANNER":
                        adView.setAdSize(AdSize.SMART_BANNER);
                        break;
                    case "BANNER":
                        adView.setAdSize(AdSize.BANNER);
                        break;
                    case "FLUID":
                        adView.setAdSize(AdSize.FLUID);
                        break;
                    case "FULL_BANNER":
                        adView.setAdSize(AdSize.FULL_BANNER);
                        break;
                    case "LARGE_BANNER":
                        adView.setAdSize(AdSize.LARGE_BANNER);
                        break;
                    case "LEADERBOARD":
                        adView.setAdSize(AdSize.LEADERBOARD);
                        break;
                    case "MEDIUM_RECTANGLE":
                        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                        break;
                }
            }

            // Setup AdView Layout
            adViewLayout = new RelativeLayout(getContext());
            adViewLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            final CoordinatorLayout.LayoutParams adViewLayoutParams = new CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.MATCH_PARENT
            );

            switch (adPosition) {
                case "TOP":
                    adViewLayout.setVerticalGravity(Gravity.TOP);
                    break;
                case "CENTER":
                    adViewLayout.setVerticalGravity(Gravity.CENTER);
                    break;
                default:
                    adViewLayout.setVerticalGravity(Gravity.BOTTOM);
                    break;
            }

            adViewLayout.setLayoutParams(adViewLayoutParams);

            // Set Bottom margin for TabBar
            float density = getContext().getResources().getDisplayMetrics().density;
            float bottomMargin = call.getInt("bottomMargin", 0);
            float topMargin = call.getInt("topMargin", 0);
            int bottomMarginParam = (int) (bottomMargin * density);
            int topMarginParam = (int) (topMargin * density);
            adViewLayoutParams.setMargins(0, topMarginParam, 0, bottomMarginParam);

            // Remove child from AdViewLayout
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adView.getParent() != null) {
                        ((ViewGroup)adView.getParent()).removeView(adView);
                    }
                    adViewLayout.setLayoutParams(adViewLayoutParams);
                    // Add AdView into AdViewLayout
                    adViewLayout.addView(adView);
                }
            });

            // Run AdMob In Main UI Thread
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle extras = new Bundle();
                    extras.putString("npa", personalisedAds);
                    adView.loadAd(new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build());
                    adView.setAdListener(new AdListener(){
                        @Override
                        public void onAdLoaded() {
                            notifyListeners("onAdLoaded", new JSObject().put("value", true));
                            super.onAdLoaded();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            notifyListeners("onAdFailedToLoad", new JSObject().put("errorCode", i));
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdOpened() {
                            notifyListeners("onAdOpened", new JSObject().put("value", true));
                            super.onAdOpened();
                        }

                        @Override
                        public void onAdClosed() {
                            notifyListeners("onAdClosed", new JSObject().put("value", true));
                            super.onAdClosed();
                        }
                    });

                    // Add AdViewLayout top of the WebView
                    viewGroup.addView(adViewLayout);
                }
            });

            call.success(new JSObject().put("value", true));

        } catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }


    // Hide the banner, remove it from screen, but can show it later
    @PluginMethod()
    public void hideBanner(PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adViewLayout != null) {
                        adViewLayout.setVisibility(View.GONE);
                        adView.pause();
                    }
                }
            });
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }


    // Resume the banner, show it after hide
    @PluginMethod()
    public void resumeBanner(PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adViewLayout != null && adView != null) {
                        adViewLayout.setVisibility(View.VISIBLE);
                        adView.resume();
                        //Log.d(getLogTag(), "Banner AD Resumed");
                    }
                }
            });

            call.success(new JSObject().put("value", true));

        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }


    // Destroy the banner, remove it from screen.
    @PluginMethod()
    public void removeBanner(PluginCall call) {
        try {
            if (adView != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adView != null) {
                            viewGroup.removeView(adViewLayout);
                            adViewLayout.removeView(adView);
                            adView.destroy();
                            adView = null;
                            //Log.d(getLogTag(), "Banner AD Removed");
                        }
                    }
                });
            }
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    // Prepare interstitial Ad
    @PluginMethod()
    public void loadInterstitial(final PluginCall call) {
        this.call = call;
        String adId;
        if (call.getBoolean("isTesting",false)) {
            adId= "ca-app-pub-3940256099942544/1033173712";
        } else {
            adId= call.getString("adIdAndroid", "ca-app-pub-3940256099942544/1033173712");
        }

        try {
            interstitialAd = new InterstitialAd(getContext());
            interstitialAd.setAdUnitId(adId);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle extras = new Bundle();
                    extras.putString("npa", personalisedAds);
                    interstitialAd.loadAd(new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build());
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                            notifyListeners("onAdLoaded", new JSObject().put("value", true));
                            call.success(new JSObject().put("value", true));
                            super.onAdLoaded();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            // Code to be executed when an ad request fails.
                            notifyListeners("onAdFailedToLoad", new JSObject().put("errorCode", errorCode));
                            super.onAdFailedToLoad(errorCode);
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when the ad is displayed.
                            notifyListeners("onAdOpened", new JSObject().put("value", true));
                            super.onAdOpened();
                        }

                        @Override
                        public void onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                            notifyListeners("onAdLeftApplication", new JSObject().put("value", true));
                            super.onAdLeftApplication();
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when when the interstitial ad is closed.
                            notifyListeners("onAdClosed", new JSObject().put("value", true));
                            super.onAdClosed();
                        }
                    });
                }
            });

        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    
    // Show interstitial Ad
    @PluginMethod()
    public void showInterstitial(final PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if (interstitialAd != null && interstitialAd.isLoaded()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            interstitialAd.show();
                        }
                    });
                    call.success(new JSObject().put("value", true));
                } else {
                    call.error("The interstitial isn't loaded.");
                }
                }
            });
        }catch (Exception ex){
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    // Prepare a RewardVideoAd
    @PluginMethod()
    public void loadRewarded(final PluginCall call) {
        this.call = call;
        final String adId;
        if (call.getBoolean("isTesting",false)) {
            adId= "ca-app-pub-3940256099942544/5224354917";
        } else {
            adId = call.getString("adIdAndroid", "ca-app-pub-3940256099942544/5224354917");
        }

        try {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle extras = new Bundle();
                    extras.putString("npa", personalisedAds);
                    rewardedVideoAd.loadAd(adId, new AdRequest
                            .Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                            .build());
                    rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                        @Override
                        public void onRewardedVideoAdLoaded() {
                            call.success(new JSObject().put("value", true));
                            notifyListeners("onRewardedVideoAdLoaded", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoAdOpened() {
                            notifyListeners("onRewardedVideoAdOpened", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoStarted() {
                            notifyListeners("onRewardedVideoStarted", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoAdClosed() {
                            notifyListeners("onRewardedVideoAdClosed", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewarded(RewardItem rewardItem) {
                            notifyListeners("onRewarded", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoAdLeftApplication() {
                            notifyListeners("onRewardedVideoAdLeftApplication", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoAdFailedToLoad(int i) {
                            notifyListeners("onRewardedVideoAdFailedToLoad", new JSObject().put("value", true));
                        }

                        @Override
                        public void onRewardedVideoCompleted() {
                            notifyListeners("onRewardedVideoCompleted", new JSObject().put("value", true));
                        }
                    });
                }
            });
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }

    }

   // Show a RewardVideoAd
   @PluginMethod()
   public void showRewarded(final PluginCall call) {
       try {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               rewardedVideoAd.show();
                           }
                       });
                       call.success(new JSObject().put("value", true));
                   }else {
                       call.error("The RewardedVideoAd wasn't loaded yet.");
                   }
               }
           });
       }catch (Exception ex) {
           call.error(ex.getLocalizedMessage(), ex);
       }
   }
   
    // Pause a RewardVideoAd
    @PluginMethod()
    public void pauseRewarded(PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd.pause(getContext());
                }
            });
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    // Resume a RewardVideoAd
    @PluginMethod()
    public void resumeRewarded(PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd.resume(getContext());
                }
            });
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }
    
    // Destroy a RewardVideoAd
    @PluginMethod()
    public void stopRewarded(PluginCall call) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd.destroy(getContext());
                }
            });
            call.success(new JSObject().put("value", true));
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }
}

package com.devutex.admobadvanced;

import android.Manifest;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


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

    // Initialize AdMob with appId
    @PluginMethod()
    public void initialize(PluginCall call) {
        /* Sample AdMob App ID: ca-app-pub-3940256099942544~3347511713 */
        String appId = call.getString("appIdAndroid", "ca-app-pub-3940256099942544~3347511713");
        try {
            MobileAds.initialize(this.getContext(), appId);
            viewGroup = (ViewGroup) ((ViewGroup) getActivity().findViewById(android.R.id.content)).getChildAt(0);
            call.success();
        }catch (Exception ex) {
            call.error(ex.getLocalizedMessage(), ex);
        }
    }


    // Show a banner Ad
    @PluginMethod()
    public void showBanner(PluginCall call) {
        /* Dedicated test ad unit ID for Android banners: ca-app-pub-3940256099942544/6300978111*/
        Boolean isTesting = call.getBoolean("isTesting",false);
        String adId;
        if (isTesting) {
            adId= "ca-app-pub-3940256099942544/6300978111";
        } else {
            adId= call.getString("adIdAndroid", "ca-app-pub-3940256099942544/6300978111");
        }

        String adSize     = call.getString("adSize", "SMART_BANNER");
        String adPosition = call.getString("position", "BOTTOM_CENTER");
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
            adViewLayout.setVerticalGravity(Gravity.BOTTOM);

            final CoordinatorLayout.LayoutParams adViewLayoutParams = new CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                    CoordinatorLayout.LayoutParams.WRAP_CONTENT
            );

            switch (adPosition) {
                case "TOP_CENTER":
                    adViewLayoutParams.gravity = Gravity.TOP;
                    adViewLayoutParams.anchorGravity = Gravity.CENTER_VERTICAL;
                    break;
                case "CENTER":
                    adViewLayoutParams.gravity = Gravity.CENTER;
                    adViewLayoutParams.anchorGravity = Gravity.CENTER_VERTICAL;
                    break;
                default:
                    adViewLayoutParams.gravity = Gravity.BOTTOM;
                    adViewLayoutParams.anchorGravity = Gravity.CENTER_VERTICAL;
                    break;
            }

            adViewLayout.setLayoutParams(adViewLayoutParams);

            // Set Bottom margin for TabBar
            boolean hasTabBar = call.getBoolean("hasTabBar", false);
            if (hasTabBar) {
                float density = getContext().getResources().getDisplayMetrics().density;
                float tabBarHeight = call.getInt("tabBarHeight", 56);
                int margin = (int) (tabBarHeight * density);
                adViewLayoutParams.setMargins(0, 0, 0, margin);
            }

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
                    adView.loadAd(new AdRequest.Builder().build());
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
    public void prepareInterstitial(final PluginCall call) {
        this.call = call;
        Boolean isTesting = call.getBoolean("isTesting",false);
        String adId;
        if (isTesting) {
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
                    interstitialAd.loadAd(new AdRequest.Builder().build());
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
                    call.error("The interstitial wasn't loaded yet.");
                }
                }
            });
        }catch (Exception ex){
            call.error(ex.getLocalizedMessage(), ex);
        }
    }

    // Prepare a RewardVideoAd
    @PluginMethod()
    public void prepareRewardVideoAd(final PluginCall call) {
        this.call = call;
        /* dedicated test ad unit ID for Android rewarded video:
            ca-app-pub-3940256099942544/5224354917
        */
        Boolean isTesting = call.getBoolean("isTesting",false);
        final String adId;
        if (isTesting) {
            adId= "ca-app-pub-3940256099942544/5224354917";
        } else {
            adId = call.getString("adIdAndroid", "ca-app-pub-3940256099942544/5224354917");
        }

        try {
            rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    rewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
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
   public void showRewardVideoAd(final PluginCall call) {
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
    public void pauseRewardedVideo(PluginCall call) {
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
    public void resumeRewardedVideo(PluginCall call) {
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
    public void stopRewardedVideo(PluginCall call) {
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

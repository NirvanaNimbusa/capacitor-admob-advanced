import { Injectable } from '@angular/core';
import { Plugins } from '@capacitor/core';
import { BannerAdOptions, InterstitialAdOptions, RewardedAdOptions, AdSize, AdPosition } from 'capacitor-admob-advanced';
const { AdMobAdvanced } = Plugins;

@Injectable({
    providedIn: 'root'
})
export class AdsService {

    public bannerOptions: BannerAdOptions = {
        adIdAndroid: 'ca-app-pub-your-android-banner-ad-id',
        adIdIos: 'ca-app-pub-your-ios-banner-ad-id',
        adSize: AdSize.SMART_BANNER,
        adPosition: AdPosition.BOTTOM,
        isTesting: true,
        topMargin: 0,
        bottomMargin: 0
    };

    public interstitialOptions: InterstitialAdOptions = {
        adIdAndroid: 'ca-app-pub-your-android-interstitial-ad-id',
        adIdIos: 'ca-app-pub-your-ios-interstitial-ad-id',
        isTesting: true
    };

    public rewardedOptions: RewardedAdOptions = {
        adIdAndroid: 'ca-app-pub-your-android-rewarded-video-ad-id',
        adIdIos: 'ca-app-pub-your-ios-rewarded-video-ad-id',
        isTesting: true
    };

    interstitialLoaded = false;
    rewardedLoaded = false;
    personalizedAds;

    constructor() { }

    public initialize() {
        AdMobAdvanced.initialize({
            appIdAndroid: 'ca-app-your-android-app-id', // replace with your actual Android app ID
            appIdIos: 'ca-app-your-ios-add-id',     // replace with your actual iOS app ID
        }).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public initializeWithConsent() {
        AdMobAdvanced.initializeWithConsent({
            appIdAndroid: 'ca-app-your-android-app-id', // replace with your actual Android app ID
            appIdIos: 'ca-app-your-ios-add-id',     // replace with your actual iOS app ID
            publisherId: 'pub-your-publisher-id',                     // replace with your actual publisher ID
        }).then(data => {
            if (data.consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else if (data.consentStatus === 'NON_PERSONALIZED') {
                this.personalizedAds = false;
            } else if (data.consentStatus === 'UNKNOWN') {
                this.showGoogleConsentForm();
            }
            console.log(data);
        }, error => {
            console.error(error);
        });
    }

    public showGoogleConsentForm() {
        AdMobAdvanced.showGoogleConsentForm({
            privacyPolicyURL: 'https://www.your.com/privacyurl', // replace with your actual privacy policy url
            showAdFreeOption: true
        }).then(data => {
            if (data.consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else if (data.consentStatus === 'NON_PERSONALIZED') {
                this.personalizedAds = false;
            } else if (data.consentStatus === 'ADFREE') {
                console.log('User wishes to pay for Ad free version');
                this.personalizedAds = false;
            } else {
                this.personalizedAds = false;
            }
        }, error => {
            console.error(error);
        });
    }

    public getAdProviders() {
        AdMobAdvanced.getAdProviders({
        }).then(data => {
            console.log(data.adProviders);
        }, error => {
            console.error(error);
        });
    }

    public updateAdExtras(conStat, chldDct, uAOC, mACR) {
        AdMobAdvanced.updateAdExtras({
            consentStatus: conStat,
            childDirected: chldDct,
            underAgeOfConsent: uAOC,
            maxAdContentRating: mACR
        }).then(data => {
            if (data.consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else {
                this.personalizedAds = false;
            }
            console.log(data);
        }, error => {
            console.error(error);
        });
    }

    public showBanner() {
        AdMobAdvanced.showBanner(this.bannerOptions).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public hideBanner() {
        AdMobAdvanced.hideBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public resumeBanner() {
        AdMobAdvanced.resumeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public removeBanner() {
        AdMobAdvanced.removeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public loadInterstitial() {
        AdMobAdvanced.loadInterstitial(this.interstitialOptions).then(value => {
            console.log(value);
            this.interstitialLoaded = true;
        }, error => {
            console.error(error);
        });
    }

    public showInterstitial() {
        AdMobAdvanced.showInterstitial().then(value => {
            console.log(value);
            this.interstitialLoaded = false;
        }, error => {
            console.error(error);
        });
    }

    public loadRewarded() {
        AdMobAdvanced.loadRewarded(this.rewardedOptions).then(value => {
            console.log(value);
            this.rewardedLoaded = true;
        }, error => {
            console.error(error);
        });
    }

    public showRewarded() {
        AdMobAdvanced.showRewarded().then(value => {
            console.log(value);
            this.rewardedLoaded = false;
        }, error => {
            console.error(error);
        });
    }

    public pauseRewarded() {
        AdMobAdvanced.pauseRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public resumeRewarded() {
        AdMobAdvanced.resumeRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public stopRewarded() {
        AdMobAdvanced.showRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

}

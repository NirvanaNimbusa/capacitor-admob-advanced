import { Injectable } from '@angular/core';
import { Plugins } from '@capacitor/core';
import {
    BannerAdOptions, InterstitialAdOptions, RewardedAdOptions, AdSize, AdPosition,
    AdConsentStatus, AdContentRating
} from 'capacitor-admob-advanced';
const { AdmobAdvanced } = Plugins;

@Injectable({
    providedIn: 'root'
})
export class AdsService {

    public bannerOptions: BannerAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        adSize: AdSize.SMART_BANNER,
        adPosition: AdPosition.BOTTOM,
        isTesting: true,
        topMargin: 0,
        bottomMargin: 0
    };

    public interstitialOptions: InterstitialAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        isTesting: true
    };

    public rewardedOptions: RewardedAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        isTesting: true
    };

    interstitialLoaded = false;
    rewardedLoaded = false;
    personalizedAds;

    constructor() { }

    public initialise() {
        AdmobAdvanced.initializeWithConsent({
            appIdAndroid: 'ca-app-pub-3572449953921317~8063185404', // replace with your actual Android app ID
            appIdIos: 'ca-app-pub-3940256099942544~3347511713',     // replace with your actual iOS app ID
            publisherId: 'pub-3572449953921317',                     // replace with your actual publisher ID
            tagUnderAgeOfConsent: false
        }).then(consentStatus => {
            if (consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else if (consentStatus === 'NON_PERSONALIZED') {
                this.personalizedAds = false;
            } else if (consentStatus === 'UNKNOWN') {
                this.showGoogleConsentForm();
            } else {
                console.log('Consent Status: ' + consentStatus);
            }
        }, error => {
            console.error(error);
        });
    }

    public showGoogleConsentForm() {
        AdmobAdvanced.showGoogleConsentForm({
            privacyPolicyURL: 'https://www.your.com/privacyurl', // replace with your actual privacy policy url
            showAdFreeOption: true
        }).then(consentStatus => {
            console.log(consentStatus);
            if (consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else if (consentStatus === 'NON_PERSONALIZED') {
                this.personalizedAds = false;
            } else if (consentStatus === 'ADFREE') {
                console.log('User wishes to pay for Ad free version');
            } else {
                this.personalizedAds = false;
            }
        }, error => {
            console.error(error);
        });
    }

    public getAdProviders() {
        AdmobAdvanced.getAdProviders({
        }).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public updateAdExtras(persAds, chldDct, uAOC, mACR) {
        AdmobAdvanced.updateAdExtras({
            personalizedAds: persAds,
            childDirected: chldDct,
            underAgeOfConsent: uAOC,
            maxAdContentRating: mACR
        }).then(consentStatus => {
            console.log(consentStatus);
        }, error => {
            console.error(error);
        });
    }

    public showBanner() {
        AdmobAdvanced.showBanner(this.bannerOptions).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public hideBanner() {
        AdmobAdvanced.hideBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public resumeBanner() {
        AdmobAdvanced.resumeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public removeBanner() {
        AdmobAdvanced.removeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public loadInterstitial() {
        AdmobAdvanced.loadInterstitial(this.interstitialOptions).then(value => {
            console.log(value);
            this.interstitialLoaded = true;
        }, error => {
            console.error(error);
        });
    }

    public showInterstitial() {
        AdmobAdvanced.showInterstitial().then(value => {
            console.log(value);
            this.interstitialLoaded = false;
        }, error => {
            console.error(error);
        });
    }

    public loadRewarded() {
        AdmobAdvanced.loadRewarded(this.rewardedOptions).then(value => {
            console.log(value);
            this.rewardedLoaded = true;
        }, error => {
            console.error(error);
        });
    }

    public showRewarded() {
        AdmobAdvanced.showRewarded().then(value => {
            console.log(value);
            this.rewardedLoaded = false;
        }, error => {
            console.error(error);
        });
    }

    public pauseRewarded() {
        AdmobAdvanced.pauseRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public resumeRewarded() {
        AdmobAdvanced.resumeRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public stopRewarded() {
        AdmobAdvanced.showRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

}

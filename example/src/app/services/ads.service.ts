import { Injectable } from '@angular/core';
import { Plugins } from '@capacitor/core';
import { BannerAdOptions, InterstitialAdOptions, RewardedAdOptions, AdSize, AdPosition } from 'capacitor-admob-advanced';
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

    public initialize() {
        AdmobAdvanced.initializeWithConsent({
            appIdAndroid: 'ca-app-pub-3572449953921317~8063185404', // replace with your actual Android app ID
            appIdIos: 'ca-app-pub-3572449953921317~7887408669',     // replace with your actual iOS app ID
            publisherId: 'pub-3572449953921317',                     // replace with your actual publisher ID
            tagUnderAgeOfConsent: false
        }).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    public initializeWithConsent() {
        AdmobAdvanced.initializeWithConsent({
            appIdAndroid: 'ca-app-pub-3572449953921317~8063185404', // replace with your actual Android app ID
            appIdIos: 'ca-app-pub-3572449953921317~7887408669',     // replace with your actual iOS app ID
            publisherId: 'pub-3572449953921317',                     // replace with your actual publisher ID
            tagUnderAgeOfConsent: false
        }).then(data => {
            if (data.consentStatus === 'PERSONALIZED') {
                this.personalizedAds = true;
            } else if (data.consentStatus === 'NON_PERSONALIZED') {
                this.personalizedAds = false;
            } else if (data.consentStatus === 'UNKNOWN') {
                this.showGoogleConsentForm();
            } else {
                console.log('Error: ' + data);
            }
        }, error => {
            console.error(error);
        });
    }

    public showGoogleConsentForm() {
        AdmobAdvanced.showGoogleConsentForm({
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
        AdmobAdvanced.getAdProviders({
        }).then(data => {
            console.log(data.adProviders);
        }, error => {
            console.error(error);
        });
    }

    public updateAdExtras(conStat, chldDct, uAOC, mACR) {
        AdmobAdvanced.updateAdExtras({
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

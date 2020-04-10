import { Component } from '@angular/core';
import { Plugins } from '@capacitor/core';
import { BannerAdOptions, InterstitialAdOptions, RewardedAdOptions, AdSize, AdPosition } from 'capacitor-admob-advanced';
const { AdmobAdvanced } = Plugins;

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
})
export class HomePage {
    bannerOptions: BannerAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        adSize: AdSize.BANNER,
        adPosition: AdPosition.BOTTOM,
        isTesting: true,
        topMargin: 0,
        bottomMargin: 0
    };

    interstitialOptions: InterstitialAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        isTesting: true
    };

    rewardedOptions: RewardedAdOptions = {
        adIdAndroid: 'ca-app-pub-3940256099942544/6300978111',
        adIdIos: 'ca-app-pub-3940256099942544/6300978111',
        isTesting: true
    };

    constructor() { }

    showBanner() {
        AdmobAdvanced.showBanner(this.bannerOptions).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    hideBanner() {
        AdmobAdvanced.hideBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    resumeBanner() {
        AdmobAdvanced.resumeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    removeBanner() {
        AdmobAdvanced.removeBanner().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    loadInterstitial() {
        AdmobAdvanced.loadInterstitial(this.interstitialOptions).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    showInterstitial() {
        AdmobAdvanced.showInterstitial().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    loadRewarded() {
        AdmobAdvanced.loadRewarded(this.rewardedOptions).then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    showRewarded() {
        AdmobAdvanced.showRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    pauseRewarded() {
        AdmobAdvanced.pauseRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    resumeRewarded() {
        AdmobAdvanced.resumeRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

    stopRewarded() {
        AdmobAdvanced.showRewarded().then(value => {
            console.log(value);
        }, error => {
            console.error(error);
        });
    }

}

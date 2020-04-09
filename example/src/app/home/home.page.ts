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
        adposition: AdPosition.BOTTOM_CENTER,
        hasTabBar: false,
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

}

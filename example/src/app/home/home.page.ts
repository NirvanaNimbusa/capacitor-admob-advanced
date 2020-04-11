import { Component } from '@angular/core';
import { AdsService } from '../services/ads.service';

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
})
export class HomePage {

    userConsent;

    constructor(public adsService: AdsService) { }

    public showBanner() {
        this.adsService.showBanner();
    }

    public hideBanner() {
        this.adsService.hideBanner();
    }

    public resumeBanner() {
        this.adsService.resumeBanner();
    }

    public removeBanner() {
        this.adsService.removeBanner();
    }

    public loadInterstitial() {
        this.adsService.loadInterstitial();
    }

    public showInterstitial() {
        this.adsService.showInterstitial();
    }

    public loadRewarded() {
        this.adsService.loadRewarded();
    }

    public showRewarded() {
        this.adsService.showRewarded();
    }

    public showGoogleConsentForm() {
        this.adsService.showGoogleConsentForm();
    }

    public getAdProviders() {
        this.adsService.getAdProviders();
    }
}

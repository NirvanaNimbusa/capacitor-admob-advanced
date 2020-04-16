import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { AdsService } from './services/ads.service';

@Component({
    selector: 'app-root',
    templateUrl: 'app.component.html',
    styleUrls: ['app.component.scss']
})
export class AppComponent {
    constructor(
        private platform: Platform,
        private splashScreen: SplashScreen,
        private statusBar: StatusBar,
        private adsService: AdsService
    ) {

        this.initialiseApp();
    }

    initialiseApp() {
        this.platform.ready().then(() => {
            this.adsService.initializeWithConsent();
            this.statusBar.styleDefault();
            this.splashScreen.hide();
        });
    }
}

import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { Plugins } from '@capacitor/core';
import { AdsService } from './services/ads.service';
const { AdmobAdvanced } = Plugins;

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
            this.adsService.initialise();
            this.statusBar.styleDefault();
            this.splashScreen.hide();
        });
    }
}

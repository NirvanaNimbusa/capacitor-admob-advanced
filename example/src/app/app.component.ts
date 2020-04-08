import { Component } from '@angular/core';
import { Platform } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';
import { Plugins } from '@capacitor/core';
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
        private statusBar: StatusBar
    ) {

        this.initializeApp();
    }

    initializeApp() {
        this.platform.ready().then(() => {
            AdmobAdvanced.initialize({
                appIdAndroid: 'ca-app-pub-3572449953921317~8063185404',
                appIdIos: 'ca-app-pub-3940256099942544~3347511713'
            });
            this.statusBar.styleDefault();
            this.splashScreen.hide();
        });
    }
}

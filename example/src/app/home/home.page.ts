import { Component } from '@angular/core';
import { Plugins } from '@capacitor/core';
import { AdOptions, AdSize, AdPosition } from 'capacitor-admob-advanced';
const { AdmobAdvanced } = Plugins;

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
})
export class HomePage {

    constructor() { }

}

<p align="center">
  <img width="20%" height="auto" src="https://vignette.wikia.nocookie.net/logopedia/images/b/bf/Android_2019_%28stacked%29.svg/revision/latest/scale-to-width-down/1000?cb=20190823192905">
<img width="20%" height="auto" src="https://vignette.wikia.nocookie.net/logopedia/images/4/49/Admob2018.png/revision/latest?cb=20190109132426">
<img width="20%" height="auto" src="https://vignette.wikia.nocookie.net/logopedia/images/6/63/IOS_wordmark_%282017%29.svg/revision/latest/scale-to-width-down/512?cb=20170621150256">
</p>

# [Capacitor AdMob Advanced](https://github.com/DTX-Elliot/capacitor-admob-advanced) 

Capacitor AdMob Advanced is an [Ionic Capacitor](https://capacitor.ionicframework.com) plugin that allows you to use the Google AdMob and Google Consent SDK within your Ionic project. This plugin supports both iOS and Android.

## Release Notes:
### [v1.2.0](https://github.com/DTX-Elliot/capacitor-admob-advanced)
- Plugin released.

## Consider Donating:

I hope this plugin allows you to successfully monetize your mobile app while complying with various data protection laws. If so, please consider supporting this project by donating to my PayPal.


| Type    | Link                                                                                             |
| :------ | :----------------------------------------------------------------------------------------------- |
| Once    | [paypal.me](paypal.me/elliotarcher)                                                              |
| Monthly | [paypal.com](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=ZMVSGXUDY4CHG) |

## Example App

Download the example project **[here](https://github.com/DTX-Elliot/capacitor-admob-advanced/tree/master/example)**

## Installation


```console
npm install --save capacitor-admob-advanced@latest
```

## iOS

### Update **Info.plist**

Open your **App/App/Info.plist** file and add these lines. Make sure you replace the string with your iOS App ID.

```xml
<!-- this two line needs to be added -->

<key>GADIsAdManagerApp</key>
<true/>

<key>GADApplicationIdentifier</key>
<!-- replace this value with your App ID key-->
<string>ca-app-pub-your-ios-app-id</string>

```

## Android

### Update Manifest

Open your **android/app/src/Android/AndroidManifest.xml** file and add the code below. Again, replace the android:value with your actual Android App Id.

```xml
<application>
  <!-- this line needs to be added (replace the value!) -->
  <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="ca-app-pub-your-android-app-id" />
  <activity></activity>
</application>

```

### Register AdMobAdvanced to Capacitor Android

Open your **android/app/src/main/java/your/package/name/MainActivity.java** of your app and Register AdMobAdvanced as a Capacitor Plugin.

```java
// Other imports...
import com.devutex.admobadvanced.AdMobAdvanced;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{

      add(AdMobAdvanced.class);  // Add AdMobAdvanced as a Capacitor Plugin

    }});
  }
}
```

##  Import AdMobAdvanced

In Ionic, open your **app.component.ts** file (or wherever you want to initialize the plugin) and add the imports at the top.

```typescript
import { Plugins } from "@capacitor/core";
const { AdMobAdvanced } = Plugins;

@Component({
  selector: "app-root",
  templateUrl: "app.component.html",
  styleUrls: ["app.component.scss"]
})
export class AppComponent {
  constructor() {
    
  }
}
```
## Initializing Google Admob
### initialize(options: {appIdAndroid: string, appIdIos: string}): Promise<{ value: boolean }>
If you just want to use Google AdMob without utilising the Consent SDK then you can do so by calling **AdMobAdvanced.Initialize()**.
#### NOTE: The Google Admob SDK doesn't actually use the iOS App ID but I've added the option to put both to avoid confusion and also if Google decide to use it in the future then I can easily integrate that into the plugin. 

```typescript
public initialize() {
    AdmobAdvanced.initialize({
        appIdAndroid: 'ca-app-your-android-app-id',    // replace with your actual Android app ID
        appIdIos: 'ca-app-your-ios-add-id',            // replace with your actual iOS app ID
    }).then(value => {
        console.log(value);                            // this will return true once complete
    }, error => {
        console.error(error);
    });
}
```
## Initializing Google AdMob with the Consent SDK
### initializeWithConsent(options: {appIdAndroid: string, appIdIos: string, publisherId: string}): Promise<{consentStatus: string, childDirected: any, underAgeOfConsent: any, maxAdContentRating: string}>
If you want to take advantage of the Google Consent SDK, then you can initialize Google AdMob with the Consent functionality built in, see below. The plugin will return 4 values from the Consent SDK that may have been stored by you or from the user's preference. These values are: consentStatus, childDirected, underAgeOfConsent and maxAdContentRating. Again, Google doesn't need the iOS App Id value but I've put it in anyway.
```typescript
public initializeWithConsent() {
    AdmobAdvanced.initializeWithConsent({
        appIdAndroid: 'ca-app-your-android-app-id',    // replace with your actual Android app ID
        appIdIos: 'ca-app-your-ios-add-id',            // replace with your actual iOS app ID
        publisherId: 'pub-your-publisher-id'           // replace with your actual publisher ID
    }).then(data => {
        console.log(data.consentStatus);
        console.log(data.childDirected);
        console.log(data.underAgeOfConsent);
        console.log(data.maxAdContentRating);
    }, error => {
        console.error(error);
    });
}
```
## Using the Google Consent Form
### showGoogleConsentForm(options: {privacyPolicyURL: string, showAdFreeOption: boolean}): Promise<{ consentStatus: string }>
You can either choose to collect consent information yourself and pass this information to the plugin or you can use the default Google Consent form for you. If your app has an Ad-free version (for example you allow users to pay to remove ads) then you can pass **showAdFreeOption: true** to the plugin and this option will be displayed on the form. If you don't have an Ad-free version then you don't need to pass this value, the default is false and the option won't be displayed to the user. You should also pass in the URL for your app's privacy policy so that it can be displayed on the google consent form. Once the form is complete, the plugin will return the consent status, or "ADFREE" if that is the users choice. The plugin will also save this value so that the user's preference is remembered when the app is relaunched. There is no need to forward this information to Google AdMob, this plugin does that automatically for you. 
```typescript
public showGoogleConsentForm() {
    AdmobAdvanced.showGoogleConsentForm({
        privacyPolicyURL: 'https://www.your.com/privacyurl', // replace with your actual privacy policy url
        showAdFreeOption: true
    }).then(data => {
        if (data.consentStatus === 'PERSONALIZED') {
            // User is happy with personalized ads OR they aren't located in an area where we need their permission.
        } else if (data.consentStatus === 'NON_PERSONALIZED') {
            // User is not happy with personalized ads
        } else if (data.consentStatus === 'ADFREE') {
            // User wishes to pay for Ad-free version.
        }
    }, error => {
        console.error(error);
    });
}
```
## Custom consent form
### getAdProviders(): Promise<{ adProviders: any[] }>
### updateAdExtras(options: {consentStatus: AdConsentStatus, childDirected: AdChildDirected, underAgeOfConsent: AdUnderAge, maxAdContentRating: AdContentRating}): Promise<{consentStatus: string, childDirected: any, underAgeOfConsent: any, maxAdContentRating: string}>
If you choose to collect consent information yourself then the plugin has 2 functions to help you set and save these preferences, as well as passing in extra ad parameters. Firstly, you can use **getAdProviders()** to return a list of Ad Providers enabled in your Google AdMob account. This is for you to display on your custom form so that the user can agree to have their data shared to all parties. Once you have collected consent yourself, or you wish to pass extra ad parameters, you can use **updateAdExtras()** to pass this data to the plugin to be set and stored. See below for an example.
```typescript
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
        consentStatus: AdConsentStatus.PERSONALIZED,
        childDirected: AdChildDirected.FALSE,
        underAgeOfConsent: AdUnderAge.FALSE,
        maxAdContentRating: AdContentRating.MATURE_AUDIENCE
    }).then(data => {
        console.log(data);
    }, error => {
        console.error(error);
    });
}

```
Once you call **updateAdExtras()**, the plugin will return the new values set in the Consent SDK for you to check that they were set correctly.
#### NOTE1: as of V1.0.0, the iOS native code will return **"UNKNOWN"** for "Child Directed". This is because the Google Consent SDK doesn't provide a way to call the stored value whereas the Android side of the SDK does. The values should still be set with the SDK but they won't be returned. Check back on this note for a future fix.
#### NOTE2: on the native iOS side, there is currently not way to set "childDirected" or "underAgeOfConsent" as "UNSPECIFIED" whereas the Android side does provide functionality for this. Please be aware of this when updating these values in your app, you might have to force "FALSE" on either one of these instead of "UNSPECIFIED". 

##  Banner Ads
### showBanner(options: BannerAdOptions): Promise<{ value: boolean }>
To show a banner ad, you must first set up your **BannerAdOptions** to be passed. Firstly, pass your Banner Ad Id's for both Android and iOS and then choose a banner size and a position to be displayed. If you pass **isTesting = true** then the plugin will override your Ad Id's for the test Ads, allowing you to keep your actual Ad Id's in the code for when you are ready for production. If you're app has a Tab bar layout, or your wish to pass in a top or bottom margin, then you can pass that in to the plugin as below. 

```typescript
public bannerOptions: BannerAdOptions = {
    adIdAndroid: 'ca-app-pub-your-android-banner-ad-id',
    adIdIos: 'ca-app-pub-your-ios-banner-ad-id',
    adSize: AdSize.SMART_BANNER,
    adPosition: AdPosition.BOTTOM,
    isTesting: true,
    topMargin: 0,
    bottomMargin: 0
};

public showBanner() {
    AdmobAdvanced.showBanner(this.bannerOptions).then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```
#### For more information on Banner Ad Sizes, click [here](https://developers.google.com/android/reference/com/google/android/gms/ads/AdSize)

### hideBanner(): Promise<{ value: boolean }>
This will hide the banner from view, but will not remove its instance. 

```typescript
public hideBanner() {
    AdmobAdvanced.hideBanner().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### resumeBanner(): Promise<{ value: boolean }>
This will un-hide a previously hidden banner.

```typescript
public resumeBanner() {
    AdmobAdvanced.resumeBanner().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### removeBanner(): Promise<{ value: boolean }>
This will remove the banner completely.

```typescript
public removeBanner() {
    AdmobAdvanced.removeBanner().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### Banner Event Listeners

This following Event Listeners can be called for **Banner Ads**.

```typescript
addListener(eventName: 'onAdLoaded', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdFailedToLoad', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdOpened', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdClosed', listenerFunc: (info: any) => void): PluginListenerHandle;
```

## Interstitial Ads
### loadInterstitial(options: InterstitialAdOptions): Promise<{ value: boolean }>

```typescript
public interstitialOptions: InterstitialAdOptions = {
    adIdAndroid: 'ca-app-pub-your-android-interstitial-ad-id',
    adIdIos: 'ca-app-pub-your-ios-interstitial-ad-id',
    isTesting: true
};

public loadInterstitial() {
    AdmobAdvanced.loadInterstitial(this.interstitialOptions).then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### showInterstitial(): Promise<{ value: boolean }>

```typescript
public showInterstitial() {
    AdmobAdvanced.showInterstitial().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### Interstitial Event Listeners

This following Event Listeners can be called for **Interstitial Ads**

```typescript
addListener(eventName: 'onAdLoaded', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdFailedToLoad', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdOpened', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdClosed', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onAdLeftApplication', listenerFunc: (info: any) => void): PluginListenerHandle;
```

## RewardVideo
### loadRewarded(options: RewardedAdOptions): Promise<{ value: boolean }>

```typescript
public rewardedOptions: RewardedAdOptions = {
    adIdAndroid: 'ca-app-pub-your-android-rewarded-video-ad-id',
    adIdIos: 'ca-app-pub-your-ios-rewarded-video-ad-id',
    isTesting: true
};

public loadRewarded() {
    AdmobAdvanced.loadRewarded(this.rewardedOptions).then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### showRewardVideoAd(): Promise<{ value: boolean }>

```typescript
public showRewarded() {
    AdmobAdvanced.showRewarded().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### pauseRewardedVideo(): Promise<{ value: boolean }>

```typescript
public pauseRewarded() {
    AdmobAdvanced.pauseRewarded().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### resumeRewardedVideo(): Promise<{ value: boolean }>

```typescript
public resumeRewarded() {
    AdmobAdvanced.resumeRewarded().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### stopRewardedVideo(): Promise<{ value: boolean }>

```typescript
public stopRewarded() {
    AdmobAdvanced.showRewarded().then(value => {
        console.log(value);
    }, error => {
        console.error(error);
    });
}
```

### Rewarded Video Event Listeners

This following Event Listeners can be called for **Rewarded Video Ads**

```typescript
addListener(eventName: 'onRewardedVideoAdLoaded', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoAdOpened', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoStarted', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoAdClosed', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewarded', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoAdLeftApplication', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoAdFailedToLoad', listenerFunc: (info: any) => void): PluginListenerHandle;
addListener(eventName: 'onRewardedVideoCompleted', listenerFunc: (info: any) => void): PluginListenerHandle;
```

# API

### BannerAdOptions
```typescript
export interface BannerAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    adSize?: AdSize;            //Choose an Ad Size from the AdSize interface
    adPosition?: AdPosition; //Display the banner ad at "BOTTOM", "CENTER" or "TOP"
    bottomMargin?: number; //set a bottom margin (in pixels)
    topMargin?: number;    //set a top margin (in pixels)
    isTesting?: boolean; //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}
```

### InterstitialAdOptions
```typescript
export interface InterstitialAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    isTesting?: boolean;        //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}
```

### RewardedAdOptions
```typescript
export interface RewardedAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    isTesting?: boolean;        //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}
```

### AdSize
```typescript
export enum AdSize {
    BANNER = 'BANNER', // Standard banner ad size (320x50 density-independent pixels).
    FLUID = 'FLUID', // A dynamically sized banner that matches its parent's width and expands/contracts its height to match the ad's content after loading completes.
    FULL_BANNER = 'FULL_BANNER', // Full banner ad size (468x60 density-independent pixels).
    LARGE_BANNER = 'LARGE_BANNER', // Large banner ad size (320x100 density-independent pixels).
    LEADERBOARD = 'LEADERBOARD', // Leaderboard ad size (728x90 density-independent pixels).
    MEDIUM_RECTANGLE = 'MEDIUM_RECTANGLE', // Medium rectangle ad size (300x250 density-independent pixels).
    SMART_BANNER = 'SMART_BANNER' // A dynamically sized banner that is full-width and auto-height.
}
```

### AdPosition
```typescript
export enum AdPosition {
    TOP = 'TOP',
    CENTER = 'CENTER',
    BOTTOM = 'BOTTOM',
}
```

### AdContentRating
```typescript
export enum AdContentRating {
    GENERAL = 'G',
    PARENTAL_GUIDANCE = 'PG',
    TEENS = 'T',
    MATURE_AUDIENCE = 'MA'
}
```

### AdChildDirected
```typescript
export enum AdChildDirected {
    TRUE = 'TRUE',
    FALSE = 'FALSE',
    UNSPECIFIED = 'UNSPECIFIED'
}
```

### AdConsentStatus
```typescript
export enum AdConsentStatus {
    PERSONALIZED = 'PERSONALIZED',
    NON_PERSONALIZED = 'NON_PERSONALIZED',
    UNKNOWN = 'UNKNOWN'
}
```

### AdUnderAge
```typescript
export enum AdUnderAge {
    TRUE = 'TRUE',
    FALSE = 'FALSE',
    UNSPECIFIED = 'UNSPECIFIED'
}
```

# Contributing

#### Feel free to give feedback, raise issues or questions, bug reports or fixes by creating an issue [here](https://github.com/DTX-Elliot/capacitor-admob-advanced/issues)

# License

Capacitor AdMob Advanced is [MIT licensed](https://github.com/DTX-Elliot/capacitor-admob-advanced/blob/master/LICENSE).

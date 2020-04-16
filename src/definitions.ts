import { PluginListenerHandle } from '@capacitor/core';

declare global {
    interface PluginRegistry {
        AdmobAdvanced: AdmobAdvancedPlugin;
    }
}

export interface AdmobAdvancedPlugin {

    // Initialize Admob
    initialize(options: {
        appIdAndroid: string,
        appIdIos: string
    }): Promise<{ value: boolean }>

    // Initialize AdMob with the Consent SDK
    initializeWithConsent(options: {
        appIdAndroid: string,
        appIdIos: string,
        publisherId: string
    }): Promise<{
        consentStatus: string,
        childDirected: any,
        underAgeOfConsent: any,
        maxAdContentRating: string
    }>

    //Show the Google Consent Form
    showGoogleConsentForm(options: {
        privacyPolicyURL: string,
        showAdFreeOption: boolean
    }): Promise<{ consentStatus: string }>

    getAdProviders(): Promise<{ adProviders: any[] }>

    updateAdExtras(options: {
        consentStatus: AdConsentStatus,
        childDirected: AdChildDirected,
        underAgeOfConsent: AdUnderAge,
        maxAdContentRating: AdContentRating
    }): Promise<{
        consentStatus: string,
        childDirected: any,
        underAgeOfConsent: any,
        maxAdContentRating: string
    }>

    // Show a banner Ad
    showBanner(options: BannerAdOptions): Promise<{ value: boolean }>;

    // Hide the banner, remove it from screen, but can show it later
    hideBanner(): Promise<{ value: boolean }>;

    // Resume the banner, show it after hide
    resumeBanner(): Promise<{ value: boolean }>;

    // Destroy the banner, remove it from screen.
    removeBanner(): Promise<{ value: boolean }>;

    // Prepare interstitial banner
    loadInterstitial(options: InterstitialAdOptions): Promise<{ value: boolean }>;

    // Show interstitial ad when it’s ready
    showInterstitial(): Promise<{ value: boolean }>;

    // Prepare a reward video ad
    loadRewarded(options: RewardedAdOptions): Promise<{ value: boolean }>;

    // Show a reward video ad
    showRewarded(): Promise<{ value: boolean }>;

    // Pause RewardedVideo
    pauseRewarded(): Promise<{ value: boolean }>;

    // Resume RewardedVideo
    resumeRewarded(): Promise<{ value: boolean }>;

    // Close RewardedVideo
    stopRewarded(): Promise<{ value: boolean }>;


    // AdMob listeners
    addListener(eventName: 'onAdLoaded', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onAdFailedToLoad', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onAdOpened', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onAdClosed', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onAdLeftApplication', listenerFunc: (info: any) => void): PluginListenerHandle;

    // Admob Rewarded Video listeners
    addListener(eventName: 'onRewardedVideoAdLoaded', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoAdOpened', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoStarted', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoAdClosed', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewarded', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoAdLeftApplication', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoAdFailedToLoad', listenerFunc: (info: any) => void): PluginListenerHandle;
    addListener(eventName: 'onRewardedVideoCompleted', listenerFunc: (info: any) => void): PluginListenerHandle;

}

export enum AdUnderAge {
    TRUE = 'TRUE',
    FALSE = 'FALSE',
    UNSPECIFIED = 'UNSPECIFIED'
}

export enum AdChildDirected {
    TRUE = 'TRUE',
    FALSE = 'FALSE',
    UNSPECIFIED = 'UNSPECIFIED'
}

export enum AdConsentStatus {
    PERSONALIZED = 'PERSONALIZED',
    NON_PERSONALIZED = 'NON_PERSONALIZED',
    UNKNOWN = 'UNKNOWN'
}

export enum AdContentRating {
    GENERAL = 'G',
    PARENTAL_GUIDANCE = 'PG',
    TEENS = 'T',
    MATURE_AUDIENCE = 'MA'
}

export interface BannerAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    adSize?: AdSize;            //Choose an Ad Size from the AdSize interface
    adPosition?: AdPosition; //Display the banner ad at "BOTTOM", "CENTER" or "TOP"
    bottomMargin?: number; //set a bottom margin (in pixels)
    topMargin?: number;    //set a top margin (in pixels)
    isTesting?: boolean; //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}

export interface InterstitialAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    isTesting?: boolean;        //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}

export interface RewardedAdOptions {
    adIdAndroid: string;       // Banner ad ID Android (required)
    adIdIos: string;           // Banner ad ID iOS (required)
    isTesting?: boolean;        //set this to true to only display test ads (allows you to put your actual ad IDs in the options without displaying real ads)
}

export enum AdSize {
    BANNER = 'BANNER', // Standard banner ad size (320x50 density-independent pixels).
    FLUID = 'FLUID', // A dynamically sized banner that matches its parent's width and expands/contracts its height to match the ad's content after loading completes.
    FULL_BANNER = 'FULL_BANNER', // Full banner ad size (468x60 density-independent pixels).
    LARGE_BANNER = 'LARGE_BANNER', // Large banner ad size (320x100 density-independent pixels).
    LEADERBOARD = 'LEADERBOARD', // Leaderboard ad size (728x90 density-independent pixels).
    MEDIUM_RECTANGLE = 'MEDIUM_RECTANGLE', // Medium rectangle ad size (300x250 density-independent pixels).
    SMART_BANNER = 'SMART_BANNER' // A dynamically sized banner that is full-width and auto-height.
}

export enum AdPosition {
    TOP = 'TOP',
    CENTER = 'CENTER',
    BOTTOM = 'BOTTOM',
}



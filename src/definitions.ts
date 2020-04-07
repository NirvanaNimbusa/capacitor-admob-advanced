declare module "@capacitor/core" {
  interface PluginRegistry {
    AdMobAdvanced: AdMobAdvancedPlugin;
  }
}

export interface AdMobAdvancedPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}

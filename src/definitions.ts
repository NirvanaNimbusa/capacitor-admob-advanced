declare module "@capacitor/core" {
  interface PluginRegistry {
    AdmobAdvanced: AdmobAdvancedPlugin;
  }
}

export interface AdmobAdvancedPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}

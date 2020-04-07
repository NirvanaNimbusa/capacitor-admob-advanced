import { WebPlugin } from '@capacitor/core';
import { AdMobAdvancedPlugin } from './definitions';

export class AdMobAdvancedWeb extends WebPlugin implements AdMobAdvancedPlugin {
  constructor() {
    super({
      name: 'AdMobAdvanced',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const AdMobAdvanced = new AdMobAdvancedWeb();

export { AdMobAdvanced };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(AdMobAdvanced);

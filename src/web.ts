import { WebPlugin } from '@capacitor/core';
import { AdmobAdvancedPlugin } from './definitions';

export class AdmobAdvancedWeb extends WebPlugin implements AdmobAdvancedPlugin {
  constructor() {
    super({
      name: 'AdmobAdvanced',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const AdmobAdvanced = new AdmobAdvancedWeb();

export { AdmobAdvanced };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(AdmobAdvanced);

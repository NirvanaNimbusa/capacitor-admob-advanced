package com.devutex.example;

import android.os.Bundle;

import com.devutex.admobadvanced.AdMobAdvanced;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the Bridge
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      add(AdMobAdvanced.class);
      // Ex: add(TotallyAwesomePlugin.class);
    }});
  }
}
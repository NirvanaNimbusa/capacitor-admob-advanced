
  Pod::Spec.new do |s|
    s.name = 'CapacitorAdmobAdvanced'
    s.version = '1.4.0'
    s.summary = 'A capacitor plugin for iOS and Android to integrate Google Admob into your app with extra features.'
    s.license = 'MIT'
    s.homepage = 'https://github.com/DTX-Elliot/capacitor-admob-advanced'
    s.author = 'DTX-Elliot'
    s.source = { :git => 'https://github.com/DTX-Elliot/capacitor-admob-advanced', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.static_framework = true
    s.dependency 'Capacitor'
    s.dependency 'Google-Mobile-Ads-SDK'
    s.dependency 'PersonalizedAdConsent'
  end

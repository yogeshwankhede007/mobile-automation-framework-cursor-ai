package com.mobileautomation.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceConfig {
    private String device;
    private String osVersion;
    private String app;
    private String platform;

    public static DeviceConfig[] getLatestDevices() {
        return new DeviceConfig[]{
            // Android devices
            createDevice("Google Pixel 8", "14.0", "bs://sample-app", "android"),
            createDevice("Samsung Galaxy S24", "14.0", "bs://sample-app", "android"),
            // iOS devices
            createDevice("iPhone 15 Pro", "17.0", "bs://sample-app", "ios"),
            createDevice("iPhone 15 Pro Max", "17.0", "bs://sample-app", "ios")
        };
    }

    private static DeviceConfig createDevice(String device, String osVersion, String app, String platform) {
        DeviceConfig config = new DeviceConfig();
        config.setDevice(device);
        config.setOsVersion(osVersion);
        config.setApp(app);
        config.setPlatform(platform);
        return config;
    }
} 
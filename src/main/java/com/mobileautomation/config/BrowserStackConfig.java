package com.mobileautomation.config;

import com.mobileautomation.utils.DeviceConfig;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.util.HashMap;
import java.util.Map;

public class BrowserStackConfig {
    private static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String BROWSERSTACK_URL = "https://" + BROWSERSTACK_USERNAME + ":" + BROWSERSTACK_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    public static DesiredCapabilities getBrowserStackCapabilities(DeviceConfig device) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // BrowserStack specific capabilities
        capabilities.setCapability("browserstack.user", BROWSERSTACK_USERNAME);
        capabilities.setCapability("browserstack.key", BROWSERSTACK_ACCESS_KEY);
        capabilities.setCapability("browserstack.debug", "true");
        capabilities.setCapability("browserstack.networkLogs", "true");
        capabilities.setCapability("browserstack.console", "verbose");
        
        // Device specific capabilities
        capabilities.setCapability("device", device.getDevice());
        capabilities.setCapability("os_version", device.getOsVersion());
        capabilities.setCapability("project", "Mobile Automation Framework");
        capabilities.setCapability("build", "Build " + System.currentTimeMillis());
        capabilities.setCapability("name", "Test on " + device.getDevice());
        
        // App specific capabilities
        capabilities.setCapability("app", device.getApp());
        capabilities.setCapability("autoGrantPermissions", "true");
        
        // Additional capabilities for better debugging
        capabilities.setCapability("browserstack.appiumLogs", "true");
        capabilities.setCapability("browserstack.deviceLogs", "true");
        
        return capabilities;
    }

    public static String getBrowserStackUrl() {
        return BROWSERSTACK_URL;
    }
} 
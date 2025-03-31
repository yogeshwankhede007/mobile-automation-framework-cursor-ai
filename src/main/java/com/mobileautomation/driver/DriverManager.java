package com.mobileautomation.driver;

import com.mobileautomation.config.BrowserStackConfig;
import com.mobileautomation.config.ConfigManager;
import com.mobileautomation.utils.DeviceConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;

@Getter
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static DriverManager instance;
    private AppiumDriver driver;
    private final ConfigManager configManager;

    private DriverManager() {
        configManager = ConfigManager.getInstance();
    }

    public static DriverManager getInstance() {
        if (instance == null) {
            instance = new DriverManager();
        }
        return instance;
    }

    public void initializeDriver(String platform, String device, String osVersion) {
        try {
            DesiredCapabilities capabilities;
            
            if (platform.equalsIgnoreCase("android")) {
                capabilities = setupAndroidCapabilities(device, osVersion);
                driver = new AndroidDriver(new URL(BrowserStackConfig.getBrowserStackUrl()), capabilities);
            } else if (platform.equalsIgnoreCase("ios")) {
                capabilities = setupIOSCapabilities(device, osVersion);
                driver = new IOSDriver(new URL(BrowserStackConfig.getBrowserStackUrl()), capabilities);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            logger.info("Driver initialized successfully for {} on {}", platform, device);
        } catch (Exception e) {
            logger.error("Failed to initialize driver", e);
            throw new RuntimeException("Failed to initialize driver", e);
        }
    }

    private DesiredCapabilities setupAndroidCapabilities(String device, String osVersion) {
        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDevice(device);
        deviceConfig.setOsVersion(osVersion);
        deviceConfig.setPlatform("android");
        return BrowserStackConfig.getBrowserStackCapabilities(deviceConfig);
    }

    private DesiredCapabilities setupIOSCapabilities(String device, String osVersion) {
        DeviceConfig deviceConfig = new DeviceConfig();
        deviceConfig.setDevice(device);
        deviceConfig.setOsVersion(osVersion);
        deviceConfig.setPlatform("ios");
        return BrowserStackConfig.getBrowserStackCapabilities(deviceConfig);
    }

    public void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            } finally {
                driver = null;
            }
        }
    }

    public AppiumDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver has not been initialized");
        }
        return driver;
    }
} 
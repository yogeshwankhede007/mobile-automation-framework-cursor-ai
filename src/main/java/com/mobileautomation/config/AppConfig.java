package com.mobileautomation.config;

import com.mobileautomation.exceptions.MobileAutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Logger logger = LogManager.getLogger(AppConfig.class);
    private static final Properties properties = new Properties();
    private static final String APP_DIR = "apps";

    static {
        try {
            properties.load(new FileInputStream("src/test/resources/config.properties"));
            logger.info("Configuration loaded successfully");
            validateAppFiles();
        } catch (IOException e) {
            String error = "Failed to load configuration";
            String suggestion = "Check if config.properties exists in src/test/resources/";
            throw new MobileAutomationException("CONFIG_LOAD_FAILED", error, suggestion, e);
        }
    }

    public static String getAppPath(String platform) {
        String appName = platform.equalsIgnoreCase("android") 
            ? properties.getProperty("android.app.name") 
            : properties.getProperty("ios.app.name");
            
        String appPath = APP_DIR + File.separator + appName;
        
        if (isBrowserStackEnabled()) {
            return getBrowserStackAppUrl(platform);
        }
        
        return appPath;
    }

    public static boolean isBrowserStackEnabled() {
        return Boolean.parseBoolean(properties.getProperty("browserstack.enabled", "false"));
    }

    public static String getBrowserStackUsername() {
        return properties.getProperty("browserstack.username");
    }

    public static String getBrowserStackAccessKey() {
        return properties.getProperty("browserstack.accesskey");
    }

    public static String getBrowserStackServerUrl() {
        return "https://" + getBrowserStackUsername() + ":" + getBrowserStackAccessKey() + "@hub-cloud.browserstack.com/wd/hub";
    }

    public static String getBrowserStackAppUrl(String platform) {
        return platform.equalsIgnoreCase("android") 
            ? "bs://your_android_app_hash" 
            : "bs://your_ios_app_hash";
    }

    public static String getIOSBundleId() {
        return properties.getProperty("ios.bundle.id");
    }

    public static String getIOSDeviceName() {
        return properties.getProperty("ios.device.name");
    }

    public static String getIOSOSVersion() {
        return properties.getProperty("ios.os.version");
    }

    public static String getIOSSimulatorUDID() {
        return properties.getProperty("ios.simulator.udid");
    }

    public static int getIOSWDAPort() {
        return Integer.parseInt(properties.getProperty("ios.wda.port", "8100"));
    }

    public static String getIOSWDAUrl() {
        return properties.getProperty("ios.wda.url", "http://localhost:8100");
    }

    public static int getIOSWDAStartupTimeout() {
        return Integer.parseInt(properties.getProperty("ios.wda.startup.timeout", "30"));
    }

    public static int getIOSWDAStartupRetries() {
        return Integer.parseInt(properties.getProperty("ios.wda.startup.retries", "3"));
    }

    public static int getIOSWDAStartupRetryInterval() {
        return Integer.parseInt(properties.getProperty("ios.wda.startup.retry.interval", "10"));
    }

    public static int getIOSSimulatorStartupTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.startup.timeout", "60"));
    }

    public static int getIOSSimulatorTerminateTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.terminate.timeout", "30"));
    }

    public static int getIOSSimulatorLaunchTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.launch.timeout", "30"));
    }

    public static int getIOSSimulatorShutdownTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.shutdown.timeout", "30"));
    }

    public static int getIOSSimulatorInstallTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.install.timeout", "30"));
    }

    public static int getIOSSimulatorUninstallTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.uninstall.timeout", "30"));
    }

    public static int getIOSSimulatorResetTimeout() {
        return Integer.parseInt(properties.getProperty("ios.simulator.reset.timeout", "30"));
    }

    public static int getTestRetryCount() {
        return Integer.parseInt(properties.getProperty("test.retry.count", "3"));
    }

    private static void validateAppFiles() {
        String androidAppPath = APP_DIR + File.separator + properties.getProperty("android.app.name");
        String iosAppPath = APP_DIR + File.separator + properties.getProperty("ios.app.name");

        if (!new File(androidAppPath).exists()) {
            logger.warn("Android app file not found: {}", androidAppPath);
        }

        if (!new File(iosAppPath).exists()) {
            logger.warn("iOS app file not found: {}", iosAppPath);
        }
    }
} 
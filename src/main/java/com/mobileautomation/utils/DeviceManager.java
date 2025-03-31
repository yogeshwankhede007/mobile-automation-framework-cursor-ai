package com.mobileautomation.utils;

import com.mobileautomation.exceptions.MobileAutomationException;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.time.Duration;

public class DeviceManager {
    private static final Logger logger = LogManager.getLogger(DeviceManager.class);
    private static AppiumDriverLocalService appiumService;
    private static final String IOS_SIMULATOR_CMD = "xcrun simctl list devices";
    private static final String IOS_SIMULATOR_BOOT_CMD = "xcrun simctl boot %s";
    private static final String IOS_SIMULATOR_SHUTDOWN_CMD = "xcrun simctl shutdown %s";
    private static final String ANDROID_DEVICES_CMD = "adb devices";
    private static final Pattern DEVICE_PATTERN = Pattern.compile("\\((.*?)\\)\\s+\\((.*?)\\)");
    private static AppiumDriver driver;

    public static boolean isAppiumServerRunning() {
        try {
            if (appiumService != null) {
                return appiumService.isRunning();
            }
            return false;
        } catch (Exception e) {
            logger.error("Error checking Appium server status: {}", e.getMessage());
            return false;
        }
    }

    public static void startAppiumServer() {
        try {
            if (isAppiumServerRunning()) {
                logger.info("Appium server is already running");
                return;
            }

            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .withArgument(() -> "--base-path", "/wd/hub")
                .withArgument(() -> "--relaxed-security")
                .withArgument(() -> "--session-override")
                .withArgument(() -> "--log", "appium.log")
                .withArgument(() -> "--log-timestamp")
                .withArgument(() -> "--local-timezone");

            appiumService = AppiumDriverLocalService.buildService(builder);
            appiumService.start();
            logger.info("Appium server started successfully");
        } catch (Exception e) {
            String error = "Failed to start Appium server";
            String suggestion = "Check if port 4723 is available and Appium is installed";
            throw new MobileAutomationException("APPIUM_SERVER_START_FAILED", error, suggestion, e);
        }
    }

    public static void stopAppiumServer() {
        try {
            if (appiumService != null && appiumService.isRunning()) {
                appiumService.stop();
                logger.info("Appium server stopped successfully");
            }
        } catch (Exception e) {
            String error = "Failed to stop Appium server";
            String suggestion = "Check if Appium server is running";
            throw new MobileAutomationException("APPIUM_SERVER_STOP_FAILED", error, suggestion, e);
        }
    }

    public static List<String> getAvailableIOSSimulators() {
        List<String> availableDevices = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(IOS_SIMULATOR_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = DEVICE_PATTERN.matcher(line);
                if (matcher.find()) {
                    String deviceName = matcher.group(1).trim();
                    String deviceId = matcher.group(2).trim();
                    availableDevices.add(deviceName + " (" + deviceId + ")");
                }
            }
            process.waitFor();
            logger.info("Found {} available iOS simulators", availableDevices.size());
            return availableDevices;
        } catch (Exception e) {
            String error = "Failed to get available iOS simulators";
            String suggestion = "Check if Xcode and iOS Simulator are installed";
            throw new MobileAutomationException("SIMULATOR_LIST_FAILED", error, suggestion, e);
        }
    }

    public static List<String> getAvailableAndroidDevices() {
        List<String> availableDevices = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(ANDROID_DEVICES_CMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Skip the first line (List of devices attached)
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().endsWith("device")) {
                    String deviceId = line.split("\t")[0];
                    availableDevices.add(deviceId);
                }
            }
            process.waitFor();
            logger.info("Found {} available Android devices", availableDevices.size());
            return availableDevices;
        } catch (Exception e) {
            String error = "Failed to get available Android devices";
            String suggestion = "Check if ADB is installed and devices are connected";
            throw new MobileAutomationException("ANDROID_DEVICE_LIST_FAILED", error, suggestion, e);
        }
    }

    public static void bootIOSSimulator(String deviceId) {
        try {
            String command = String.format(IOS_SIMULATOR_BOOT_CMD, deviceId);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            logger.info("iOS simulator booted successfully: {}", deviceId);
        } catch (Exception e) {
            String error = "Failed to boot iOS simulator";
            String suggestion = "Check if simulator exists and is not already running";
            throw new MobileAutomationException("SIMULATOR_BOOT_FAILED", error, suggestion, e);
        }
    }

    public static void shutdownIOSSimulator(String deviceId) {
        try {
            String command = String.format(IOS_SIMULATOR_SHUTDOWN_CMD, deviceId);
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            logger.info("iOS simulator shutdown successfully: {}", deviceId);
        } catch (Exception e) {
            String error = "Failed to shutdown iOS simulator";
            String suggestion = "Check if simulator is running";
            throw new MobileAutomationException("SIMULATOR_SHUTDOWN_FAILED", error, suggestion, e);
        }
    }

    public static String getFirstAvailableIOSSimulator() {
        List<String> devices = getAvailableIOSSimulators();
        if (!devices.isEmpty()) {
            String device = devices.get(0);
            String deviceId = device.substring(device.indexOf("(") + 1, device.indexOf(")"));
            bootIOSSimulator(deviceId);
            return device;
        }
        throw new MobileAutomationException("NO_SIMULATOR_FOUND", 
            "No iOS simulators available", 
            "Check if Xcode and iOS Simulator are installed");
    }

    public static String getFirstAvailableAndroidDevice() {
        List<String> devices = getAvailableAndroidDevices();
        if (!devices.isEmpty()) {
            return devices.get(0);
        }
        throw new MobileAutomationException("NO_ANDROID_DEVICE_FOUND", 
            "No Android devices available", 
            "Check if devices are connected and ADB is working");
    }

    public static void cleanup() {
        stopAppiumServer();
    }

    public static AppiumDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("Driver has not been initialized. Call initializeDriver() first.");
        }
        return driver;
    }

    public static void initializeDriver(String platform, String deviceName, String platformVersion, String appPath) {
        try {
            if (!isAppiumServerRunning()) {
                startAppiumServer();
            }

            URL appiumServerURL = new URL("http://127.0.0.1:4723");
            
            if ("iOS".equalsIgnoreCase(platform)) {
                // Get and boot simulator if not specified
                if (deviceName == null || deviceName.isEmpty()) {
                    String simulator = getFirstAvailableIOSSimulator();
                    deviceName = simulator.substring(0, simulator.indexOf("(")).trim();
                }

                XCUITestOptions options = new XCUITestOptions()
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setApp(appPath)
                    .setAutomationName("XCUITest")
                    .setNewCommandTimeout(Duration.ofSeconds(60))
                    .setWdaLocalPort(8100)
                    .setWdaConnectionTimeout(Duration.ofSeconds(30));

                driver = new IOSDriver(appiumServerURL, options);
                logger.info("iOS driver initialized successfully with device: {}", deviceName);
            } else if ("Android".equalsIgnoreCase(platform)) {
                // Get first available device if not specified
                if (deviceName == null || deviceName.isEmpty()) {
                    deviceName = getFirstAvailableAndroidDevice();
                }

                UiAutomator2Options options = new UiAutomator2Options()
                    .setDeviceName(deviceName)
                    .setPlatformVersion(platformVersion)
                    .setApp(appPath)
                    .setAutomationName("UiAutomator2")
                    .setNewCommandTimeout(Duration.ofSeconds(60))
                    .setAutoGrantPermissions(true);

                driver = new AndroidDriver(appiumServerURL, options);
                logger.info("Android driver initialized successfully with device: {}", deviceName);
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize driver: {}", e.getMessage());
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                driver = null;
                logger.info("Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver: {}", e.getMessage());
            }
        }
    }
} 
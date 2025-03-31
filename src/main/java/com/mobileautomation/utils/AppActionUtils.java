package com.mobileautomation.utils;

import com.mobileautomation.exceptions.MobileAutomationException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class AppActionUtils {
    private static final Logger logger = LogManager.getLogger(AppActionUtils.class);
    private final AppiumDriver driver;
    private final MobileInteractionUtils mobileUtils;

    public AppActionUtils(AppiumDriver driver) {
        this.driver = driver;
        this.mobileUtils = new MobileInteractionUtils(driver);
    }

    public void installApp(String appPath) {
        try {
            logger.info("Installing app from: {}", appPath);
            driver.installApp(appPath);
            logger.info("App installed successfully");
        } catch (Exception e) {
            String error = String.format("Failed to install app from: %s", appPath);
            String suggestion = "Check if app file exists and is valid";
            throw new MobileAutomationException("APP_INSTALL_FAILED", error, suggestion, e);
        }
    }

    public void uninstallApp(String bundleId) {
        try {
            logger.info("Uninstalling app with bundle ID: {}", bundleId);
            driver.removeApp(bundleId);
            logger.info("App uninstalled successfully");
        } catch (Exception e) {
            String error = String.format("Failed to uninstall app with bundle ID: %s", bundleId);
            String suggestion = "Check if app is installed and bundle ID is correct";
            throw new MobileAutomationException("APP_UNINSTALL_FAILED", error, suggestion, e);
        }
    }

    public void launchApp(String bundleId) {
        try {
            logger.info("Launching app with bundle ID: {}", bundleId);
            driver.activateApp(bundleId);
            logger.info("App launched successfully");
        } catch (Exception e) {
            String error = String.format("Failed to launch app with bundle ID: %s", bundleId);
            String suggestion = "Check if app is installed and bundle ID is correct";
            throw new MobileAutomationException("APP_LAUNCH_FAILED", error, suggestion, e);
        }
    }

    public void terminateApp(String bundleId) {
        try {
            logger.info("Terminating app with bundle ID: {}", bundleId);
            driver.terminateApp(bundleId);
            logger.info("App terminated successfully");
        } catch (Exception e) {
            String error = String.format("Failed to terminate app with bundle ID: %s", bundleId);
            String suggestion = "Check if app is running and bundle ID is correct";
            throw new MobileAutomationException("APP_TERMINATE_FAILED", error, suggestion, e);
        }
    }

    public boolean isAppInstalled(String bundleId) {
        try {
            return driver.isAppInstalled(bundleId);
        } catch (Exception e) {
            logger.warn("Error checking app installation: {}", bundleId);
            return false;
        }
    }

    public void clearAppData(String bundleId) {
        try {
            logger.info("Clearing app data for bundle ID: {}", bundleId);
            driver.resetApp();
            logger.info("App data cleared successfully");
        } catch (Exception e) {
            String error = String.format("Failed to clear app data for bundle ID: %s", bundleId);
            String suggestion = "Check if app is installed and has necessary permissions";
            throw new MobileAutomationException("APP_CLEAR_DATA_FAILED", error, suggestion, e);
        }
    }

    public void hideKeyboard() {
        try {
            logger.debug("Hiding keyboard");
            driver.hideKeyboard();
        } catch (Exception e) {
            logger.warn("Failed to hide keyboard: {}", e.getMessage());
        }
    }

    public void pressBack() {
        try {
            logger.debug("Pressing back button");
            driver.navigate().back();
        } catch (Exception e) {
            logger.warn("Failed to press back button: {}", e.getMessage());
        }
    }

    public void switchToWebView(String webViewName) {
        try {
            logger.info("Switching to web view: {}", webViewName);
            driver.context(webViewName);
            logger.info("Successfully switched to web view");
        } catch (Exception e) {
            String error = String.format("Failed to switch to web view: %s", webViewName);
            String suggestion = "Check if web view exists and is available";
            throw new MobileAutomationException("WEBVIEW_SWITCH_FAILED", error, suggestion, e);
        }
    }

    public void switchToNativeView() {
        try {
            logger.info("Switching to native view");
            driver.context("NATIVE_APP");
            logger.info("Successfully switched to native view");
        } catch (Exception e) {
            String error = "Failed to switch to native view";
            String suggestion = "Check if native view is available";
            throw new MobileAutomationException("NATIVE_VIEW_SWITCH_FAILED", error, suggestion, e);
        }
    }

    public List<String> getContextHandles() {
        try {
            return driver.getContextHandles();
        } catch (Exception e) {
            String error = "Failed to get context handles";
            String suggestion = "Check if app is running and has contexts available";
            throw new MobileAutomationException("CONTEXT_HANDLES_FAILED", error, suggestion, e);
        }
    }

    public void waitForAppToLoad(By loadingIndicator, int timeout) {
        try {
            logger.debug("Waiting for app to load");
            mobileUtils.waitForElement(loadingIndicator, timeout);
            logger.debug("App loaded successfully");
        } catch (Exception e) {
            String error = "App failed to load within timeout";
            String suggestion = "Check app performance and increase timeout if needed";
            throw new MobileAutomationException("APP_LOAD_TIMEOUT", error, suggestion, e);
        }
    }

    public void waitForAppToBeReady(By readyIndicator, int timeout) {
        try {
            logger.debug("Waiting for app to be ready");
            mobileUtils.waitForElement(readyIndicator, timeout);
            logger.debug("App is ready");
        } catch (Exception e) {
            String error = "App failed to be ready within timeout";
            String suggestion = "Check app initialization and increase timeout if needed";
            throw new MobileAutomationException("APP_READY_TIMEOUT", error, suggestion, e);
        }
    }
} 
package com.mobileautomation.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class WaitUtils {
    private static final Logger logger = LogManager.getLogger(WaitUtils.class);
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    public static WebElement waitForElementVisible(AppiumDriver driver, WebElement element) {
        try {
            return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element not visible after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            throw e;
        }
    }

    public static WebElement waitForElementClickable(AppiumDriver driver, WebElement element) {
        try {
            return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element not clickable after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            throw e;
        }
    }

    public static boolean waitForElementPresent(AppiumDriver driver, WebElement element) {
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.presenceOfElementLocated(element));
            return true;
        } catch (Exception e) {
            logger.error("Element not present after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            return false;
        }
    }

    public static boolean waitForElementNotPresent(AppiumDriver driver, WebElement element) {
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.invisibilityOf(element));
            return true;
        } catch (Exception e) {
            logger.error("Element still present after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            return false;
        }
    }

    public static void waitForElementStaleness(AppiumDriver driver, WebElement element) {
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.stalenessOf(element));
        } catch (Exception e) {
            logger.error("Element not stale after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            throw e;
        }
    }

    public static boolean waitForTextToBe(AppiumDriver driver, WebElement element, String text) {
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.textToBe(element, text));
            return true;
        } catch (Exception e) {
            logger.error("Text not matched after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            return false;
        }
    }

    public static void waitForPageLoad(AppiumDriver driver) {
        try {
            new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(webDriver -> ((AppiumDriver) webDriver).executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.error("Page not loaded after {} seconds: {}", DEFAULT_TIMEOUT.getSeconds(), e.getMessage());
            throw e;
        }
    }
} 
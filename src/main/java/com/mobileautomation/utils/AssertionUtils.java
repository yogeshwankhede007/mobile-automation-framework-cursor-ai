package com.mobileautomation.utils;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class AssertionUtils {
    private static final Logger logger = LogManager.getLogger(AssertionUtils.class);
    private static final SoftAssert softAssert = new SoftAssert();

    public static void assertElementVisible(AppiumDriver driver, WebElement element, String message) {
        try {
            logger.debug("Asserting element visibility: {}", message);
            softAssert.assertTrue(element.isDisplayed(), message);
        } catch (Exception e) {
            logger.error("Failed to assert element visibility: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementEnabled(AppiumDriver driver, WebElement element, String message) {
        try {
            logger.debug("Asserting element enabled state: {}", message);
            softAssert.assertTrue(element.isEnabled(), message);
        } catch (Exception e) {
            logger.error("Failed to assert element enabled state: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementSelected(AppiumDriver driver, WebElement element, String message) {
        try {
            logger.debug("Asserting element selected state: {}", message);
            softAssert.assertTrue(element.isSelected(), message);
        } catch (Exception e) {
            logger.error("Failed to assert element selected state: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementText(AppiumDriver driver, WebElement element, String expectedText, String message) {
        try {
            logger.debug("Asserting element text: {}", message);
            softAssert.assertEquals(element.getText(), expectedText, message);
        } catch (Exception e) {
            logger.error("Failed to assert element text: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementAttribute(AppiumDriver driver, WebElement element, String attribute, String expectedValue, String message) {
        try {
            logger.debug("Asserting element attribute: {}", message);
            softAssert.assertEquals(element.getAttribute(attribute), expectedValue, message);
        } catch (Exception e) {
            logger.error("Failed to assert element attribute: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementCssValue(AppiumDriver driver, WebElement element, String property, String expectedValue, String message) {
        try {
            logger.debug("Asserting element CSS value: {}", message);
            softAssert.assertEquals(element.getCssValue(property), expectedValue, message);
        } catch (Exception e) {
            logger.error("Failed to assert element CSS value: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertElementLocation(AppiumDriver driver, WebElement element, int expectedX, int expectedY, String message) {
        try {
            logger.debug("Asserting element location: {}", message);
            softAssert.assertEquals(element.getLocation().x, expectedX, message + " (X coordinate)");
            softAssert.assertEquals(element.getLocation().y, expectedY, message + " (Y coordinate)");
        } catch (Exception e) {
            logger.error("Failed to assert element location: {}", e.getMessage());
            softAssert.fail(message + ": " + e.getMessage());
        }
    }

    public static void assertAll() {
        softAssert.assertAll();
    }
} 
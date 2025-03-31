package com.mobileautomation.utils;

import com.mobileautomation.exceptions.MobileAutomationException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class MobileInteractionUtils {
    private static final Logger logger = LogManager.getLogger(MobileInteractionUtils.class);
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLLING_INTERVAL = 500;

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public MobileInteractionUtils(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public WebElement waitForElement(By locator, int timeout) {
        try {
            logger.debug("Waiting for element: {}", locator);
            return wait.withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            String error = String.format("Element not found after %d seconds: %s", timeout, locator);
            String suggestion = "Check if element exists, increase timeout, or verify locator";
            throw new MobileAutomationException("ELEMENT_NOT_FOUND", error, suggestion, e);
        }
    }

    public List<WebElement> waitForElements(By locator, int timeout) {
        try {
            logger.debug("Waiting for elements: {}", locator);
            return wait.withTimeout(Duration.ofSeconds(timeout))
                    .pollingEvery(Duration.ofMillis(DEFAULT_POLLING_INTERVAL))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            String error = String.format("Elements not found after %d seconds: %s", timeout, locator);
            String suggestion = "Check if elements exist, increase timeout, or verify locator";
            throw new MobileAutomationException("ELEMENTS_NOT_FOUND", error, suggestion, e);
        }
    }

    public void tap(By locator, int timeout) {
        try {
            logger.debug("Tapping element: {}", locator);
            WebElement element = waitForElement(locator, timeout);
            element.click();
        } catch (ElementClickInterceptedException e) {
            String error = String.format("Element not clickable: %s", locator);
            String suggestion = "Try using JavaScript click or wait for element to be clickable";
            throw new MobileAutomationException("ELEMENT_NOT_CLICKABLE", error, suggestion, e);
        }
    }

    public void type(By locator, String text, int timeout) {
        try {
            logger.debug("Typing text '{}' into element: {}", text, locator);
            WebElement element = waitForElement(locator, timeout);
            element.clear();
            element.sendKeys(text);
        } catch (ElementNotInteractableException e) {
            String error = String.format("Element not interactable: %s", locator);
            String suggestion = "Check if element is enabled and visible";
            throw new MobileAutomationException("ELEMENT_NOT_INTERACTABLE", error, suggestion, e);
        }
    }

    public void swipe(Direction direction, int distance) {
        try {
            logger.debug("Swiping {} by {} pixels", direction, distance);
            Dimension size = driver.manage().window().getSize();
            int startX, startY, endX, endY;

            switch (direction) {
                case UP:
                    startX = size.width / 2;
                    startY = (int) (size.height * 0.8);
                    endX = startX;
                    endY = (int) (size.height * 0.2);
                    break;
                case DOWN:
                    startX = size.width / 2;
                    startY = (int) (size.height * 0.2);
                    endX = startX;
                    endY = (int) (size.height * 0.8);
                    break;
                case LEFT:
                    startX = (int) (size.width * 0.8);
                    startY = size.height / 2;
                    endX = (int) (size.width * 0.2);
                    endY = startY;
                    break;
                case RIGHT:
                    startX = (int) (size.width * 0.2);
                    startY = size.height / 2;
                    endX = (int) (size.width * 0.8);
                    endY = startY;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid swipe direction");
            }

            TouchAction action = new TouchAction(driver);
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(endX, endY))
                    .release()
                    .perform();
        } catch (Exception e) {
            String error = String.format("Failed to swipe %s: %s", direction, e.getMessage());
            String suggestion = "Check if device supports touch actions and screen dimensions";
            throw new MobileAutomationException("SWIPE_FAILED", error, suggestion, e);
        }
    }

    public void longPress(By locator, int timeout) {
        try {
            logger.debug("Long pressing element: {}", locator);
            WebElement element = waitForElement(locator, timeout);
            TouchAction action = new TouchAction(driver);
            action.longPress(PointOption.point(element.getLocation().x, element.getLocation().y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .release()
                    .perform();
        } catch (Exception e) {
            String error = String.format("Failed to long press element: %s", locator);
            String suggestion = "Check if element is visible and supports long press";
            throw new MobileAutomationException("LONG_PRESS_FAILED", error, suggestion, e);
        }
    }

    public boolean isElementVisible(By locator, int timeout) {
        try {
            logger.debug("Checking visibility of element: {}", locator);
            waitForElement(locator, timeout);
            return true;
        } catch (TimeoutException e) {
            logger.warn("Element not visible: {}", locator);
            return false;
        }
    }

    public String getElementText(By locator, int timeout) {
        try {
            logger.debug("Getting text from element: {}", locator);
            WebElement element = waitForElement(locator, timeout);
            return element.getText();
        } catch (NoSuchElementException e) {
            String error = String.format("Element not found: %s", locator);
            String suggestion = "Check if element exists and is visible";
            throw new MobileAutomationException("ELEMENT_NOT_FOUND", error, suggestion, e);
        }
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
} 
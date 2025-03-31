package com.mobileautomation.pages;

import com.mobileautomation.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getInstance().getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected WebElement findElementWithRetry(By locator, int maxAttempts) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            } catch (Exception e) {
                attempts++;
                if (attempts == maxAttempts) {
                    throw new ElementNotInteractableException("Element not found after " + maxAttempts + " attempts");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null;
    }

    protected List<WebElement> findElementsWithRetry(By locator, int maxAttempts) {
        int attempts = 0;
        while (attempts < maxAttempts) {
            try {
                return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            } catch (Exception e) {
                attempts++;
                if (attempts == maxAttempts) {
                    throw new ElementNotInteractableException("Elements not found after " + maxAttempts + " attempts");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null;
    }

    protected void clickWithRetry(By locator, int maxAttempts) {
        WebElement element = findElementWithRetry(locator, maxAttempts);
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void sendKeysWithRetry(By locator, String text, int maxAttempts) {
        WebElement element = findElementWithRetry(locator, maxAttempts);
        element.clear();
        element.sendKeys(text);
    }

    protected String getTextWithRetry(By locator, int maxAttempts) {
        WebElement element = findElementWithRetry(locator, maxAttempts);
        return element.getText();
    }

    protected boolean isElementDisplayed(By locator, int maxAttempts) {
        try {
            WebElement element = findElementWithRetry(locator, maxAttempts);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void waitForElementToDisappear(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
} 
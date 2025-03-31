package com.mobileautomation.pages;

import com.mobileautomation.base.BasePage;
import com.mobileautomation.utils.AssertionUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class HomePage extends BasePage {
    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private final SoftAssert softAssert = new SoftAssert();

    @AndroidFindBy(id = "com.example.app:id/welcomeMessage")
    @iOSXCUITFindBy(id = "welcomeMessage")
    private WebElement welcomeMessage;

    @AndroidFindBy(id = "com.example.app:id/logoutButton")
    @iOSXCUITFindBy(id = "logoutButton")
    private WebElement logoutButton;

    @AndroidFindBy(id = "com.example.app:id/profileButton")
    @iOSXCUITFindBy(id = "profileButton")
    private WebElement profileButton;

    @AndroidFindBy(id = "com.example.app:id/settingsButton")
    @iOSXCUITFindBy(id = "settingsButton")
    private WebElement settingsButton;

    @AndroidFindBy(id = "com.example.app:id/notificationsButton")
    @iOSXCUITFindBy(id = "notificationsButton")
    private WebElement notificationsButton;

    @AndroidFindBy(id = "com.example.app:id/searchBar")
    @iOSXCUITFindBy(id = "searchBar")
    private WebElement searchBar;

    @AndroidFindBy(id = "com.example.app:id/menuButton")
    @iOSXCUITFindBy(id = "menuButton")
    private WebElement menuButton;

    @AndroidFindBy(id = "com.example.app:id/refreshButton")
    @iOSXCUITFindBy(id = "refreshButton")
    private WebElement refreshButton;

    @AndroidFindBy(id = "com.example.app:id/helpButton")
    @iOSXCUITFindBy(id = "helpButton")
    private WebElement helpButton;

    @AndroidFindBy(id = "com.example.app:id/feedbackButton")
    @iOSXCUITFindBy(id = "feedbackButton")
    private WebElement feedbackButton;

    public HomePage(AppiumDriver driver) {
        super(driver);
    }

    public String getWelcomeMessage() {
        try {
            logger.debug("Getting welcome message");
            waitForElement(welcomeMessage);
            return welcomeMessage.getText();
        } catch (Exception e) {
            logger.error("Failed to get welcome message: {}", e.getMessage());
            throw e;
        }
    }

    public void clickLogout() {
        try {
            logger.debug("Clicking logout button");
            waitForElementToBeClickable(logoutButton);
            logoutButton.click();
        } catch (Exception e) {
            logger.error("Failed to click logout button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickProfile() {
        try {
            logger.debug("Clicking profile button");
            waitForElementToBeClickable(profileButton);
            profileButton.click();
        } catch (Exception e) {
            logger.error("Failed to click profile button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickSettings() {
        try {
            logger.debug("Clicking settings button");
            waitForElementToBeClickable(settingsButton);
            settingsButton.click();
        } catch (Exception e) {
            logger.error("Failed to click settings button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickNotifications() {
        try {
            logger.debug("Clicking notifications button");
            waitForElementToBeClickable(notificationsButton);
            notificationsButton.click();
        } catch (Exception e) {
            logger.error("Failed to click notifications button: {}", e.getMessage());
            throw e;
        }
    }

    public void enterSearchText(String text) {
        try {
            logger.debug("Entering search text: {}", text);
            waitForElement(searchBar);
            searchBar.clear();
            searchBar.sendKeys(text);
        } catch (Exception e) {
            logger.error("Failed to enter search text: {}", e.getMessage());
            throw e;
        }
    }

    public void clickMenu() {
        try {
            logger.debug("Clicking menu button");
            waitForElementToBeClickable(menuButton);
            menuButton.click();
        } catch (Exception e) {
            logger.error("Failed to click menu button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickRefresh() {
        try {
            logger.debug("Clicking refresh button");
            waitForElementToBeClickable(refreshButton);
            refreshButton.click();
        } catch (Exception e) {
            logger.error("Failed to click refresh button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickHelp() {
        try {
            logger.debug("Clicking help button");
            waitForElementToBeClickable(helpButton);
            helpButton.click();
        } catch (Exception e) {
            logger.error("Failed to click help button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickFeedback() {
        try {
            logger.debug("Clicking feedback button");
            waitForElementToBeClickable(feedbackButton);
            feedbackButton.click();
        } catch (Exception e) {
            logger.error("Failed to click feedback button: {}", e.getMessage());
            throw e;
        }
    }

    public void verifyHomePageElements() {
        logger.debug("Verifying home page elements");
        AssertionUtils.assertElementVisible(driver, welcomeMessage, "Welcome message should be visible");
        AssertionUtils.assertElementVisible(driver, logoutButton, "Logout button should be visible");
        AssertionUtils.assertElementVisible(driver, profileButton, "Profile button should be visible");
        AssertionUtils.assertElementVisible(driver, settingsButton, "Settings button should be visible");
        AssertionUtils.assertElementVisible(driver, notificationsButton, "Notifications button should be visible");
        AssertionUtils.assertElementVisible(driver, searchBar, "Search bar should be visible");
        AssertionUtils.assertElementVisible(driver, menuButton, "Menu button should be visible");
        AssertionUtils.assertElementVisible(driver, refreshButton, "Refresh button should be visible");
        AssertionUtils.assertElementVisible(driver, helpButton, "Help button should be visible");
        AssertionUtils.assertElementVisible(driver, feedbackButton, "Feedback button should be visible");
        AssertionUtils.assertAll();
    }

    public void verifyWelcomeMessage(String expectedMessage) {
        logger.debug("Verifying welcome message: {}", expectedMessage);
        AssertionUtils.assertElementText(driver, welcomeMessage, expectedMessage, "Welcome message should match expected text");
        AssertionUtils.assertAll();
    }
} 
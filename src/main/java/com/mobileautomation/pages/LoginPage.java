package com.mobileautomation.pages;

import com.mobileautomation.base.BasePage;
import com.mobileautomation.utils.AssertionUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class LoginPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private final SoftAssert softAssert = new SoftAssert();

    @AndroidFindBy(id = "com.example.app:id/username")
    @iOSXCUITFindBy(accessibility = "username")
    private WebElement usernameField;

    @AndroidFindBy(id = "com.example.app:id/password")
    @iOSXCUITFindBy(accessibility = "password")
    private WebElement passwordField;

    @AndroidFindBy(id = "com.example.app:id/loginButton")
    @iOSXCUITFindBy(accessibility = "loginButton")
    private WebElement loginButton;

    @AndroidFindBy(id = "com.example.app:id/forgotPassword")
    @iOSXCUITFindBy(accessibility = "forgotPassword")
    private WebElement forgotPasswordLink;

    @AndroidFindBy(id = "com.example.app:id/errorMessage")
    @iOSXCUITFindBy(accessibility = "errorMessage")
    private WebElement errorMessage;

    @AndroidFindBy(id = "com.example.app:id/rememberMe")
    @iOSXCUITFindBy(accessibility = "rememberMe")
    private WebElement rememberMeCheckbox;

    @AndroidFindBy(id = "com.example.app:id/termsAndConditions")
    @iOSXCUITFindBy(accessibility = "termsAndConditions")
    private WebElement termsAndConditionsLink;

    @AndroidFindBy(id = "com.example.app:id/privacyPolicy")
    @iOSXCUITFindBy(accessibility = "privacyPolicy")
    private WebElement privacyPolicyLink;

    @AndroidFindBy(id = "com.example.app:id/socialLogin")
    @iOSXCUITFindBy(accessibility = "socialLogin")
    private WebElement socialLoginButton;

    @AndroidFindBy(id = "com.example.app:id/registerLink")
    @iOSXCUITFindBy(accessibility = "registerLink")
    private WebElement registerLink;

    @AndroidFindBy(id = "com.example.app:id/helpLink")
    @iOSXCUITFindBy(accessibility = "helpLink")
    private WebElement helpLink;

    @AndroidFindBy(id = "com.example.app:id/contactSupport")
    @iOSXCUITFindBy(accessibility = "contactSupport")
    private WebElement contactSupportLink;

    public LoginPage(AppiumDriver<WebElement> driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        try {
            logger.debug("Entering username: {}", username);
            waitForElementToBeVisible(usernameField);
            usernameField.clear();
            usernameField.sendKeys(username);
        } catch (Exception e) {
            logger.error("Failed to enter username: {}", e.getMessage());
            throw e;
        }
    }

    public void enterPassword(String password) {
        try {
            logger.debug("Entering password");
            waitForElementToBeVisible(passwordField);
            passwordField.clear();
            passwordField.sendKeys(password);
        } catch (Exception e) {
            logger.error("Failed to enter password: {}", e.getMessage());
            throw e;
        }
    }

    public void clickLoginButton() {
        try {
            logger.debug("Clicking login button");
            waitForElementToBeClickable(loginButton);
            loginButton.click();
        } catch (Exception e) {
            logger.error("Failed to click login button: {}", e.getMessage());
            throw e;
        }
    }

    public void clickForgotPassword() {
        try {
            logger.debug("Clicking forgot password link");
            waitForElementToBeClickable(forgotPasswordLink);
            forgotPasswordLink.click();
        } catch (Exception e) {
            logger.error("Failed to click forgot password link: {}", e.getMessage());
            throw e;
        }
    }

    public String getErrorMessage() {
        try {
            logger.debug("Getting error message");
            waitForElementToBeVisible(errorMessage);
            return errorMessage.getText();
        } catch (Exception e) {
            logger.error("Failed to get error message: {}", e.getMessage());
            throw e;
        }
    }

    public void checkRememberMe() {
        waitForElementToBeClickable(rememberMeCheckbox);
        if (!rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    public void uncheckRememberMe() {
        waitForElementToBeClickable(rememberMeCheckbox);
        if (rememberMeCheckbox.isSelected()) {
            rememberMeCheckbox.click();
        }
    }

    public void clickTermsAndConditions() {
        waitForElementToBeClickable(termsAndConditionsLink);
        termsAndConditionsLink.click();
    }

    public void clickPrivacyPolicy() {
        waitForElementToBeClickable(privacyPolicyLink);
        privacyPolicyLink.click();
    }

    public void clickSocialLogin() {
        waitForElementToBeClickable(socialLoginButton);
        socialLoginButton.click();
    }

    public void clickRegister() {
        waitForElementToBeClickable(registerLink);
        registerLink.click();
    }

    public void clickHelp() {
        waitForElementToBeClickable(helpLink);
        helpLink.click();
    }

    public void clickContactSupport() {
        waitForElementToBeClickable(contactSupportLink);
        contactSupportLink.click();
    }

    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new HomePage(driver);
    }

    public void verifyLoginPageElements() {
        logger.debug("Verifying login page elements");
        AssertionUtils.assertElementVisible(driver, usernameField, "Username field should be visible");
        AssertionUtils.assertElementVisible(driver, passwordField, "Password field should be visible");
        AssertionUtils.assertElementVisible(driver, loginButton, "Login button should be visible");
        AssertionUtils.assertElementVisible(driver, forgotPasswordLink, "Forgot password link should be visible");
        AssertionUtils.assertElementVisible(driver, rememberMeCheckbox, "Remember me checkbox should be visible");
        AssertionUtils.assertElementVisible(driver, termsAndConditionsLink, "Terms and conditions link should be visible");
        AssertionUtils.assertElementVisible(driver, privacyPolicyLink, "Privacy policy link should be visible");
        AssertionUtils.assertElementVisible(driver, socialLoginButton, "Social login button should be visible");
        AssertionUtils.assertElementVisible(driver, registerLink, "Register link should be visible");
        AssertionUtils.assertElementVisible(driver, helpLink, "Help link should be visible");
        AssertionUtils.assertElementVisible(driver, contactSupportLink, "Contact support link should be visible");
        AssertionUtils.assertAll();
    }

    public void verifyErrorMessage(String expectedMessage) {
        logger.debug("Verifying error message: {}", expectedMessage);
        AssertionUtils.assertElementText(driver, errorMessage, expectedMessage, "Error message should match expected text");
        AssertionUtils.assertAll();
    }
} 
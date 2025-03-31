package com.mobileautomation.tests;

import com.mobileautomation.base.BaseTest;
import com.mobileautomation.data.TestCaseId;
import com.mobileautomation.data.TestDataKey;
import com.mobileautomation.data.TestDataManager;
import com.mobileautomation.pages.LoginPage;
import com.mobileautomation.pages.HomePage;
import com.mobileautomation.utils.AppActionUtils;
import com.mobileautomation.utils.AssertionUtils;
import com.mobileautomation.utils.FileUtils;
import com.mobileautomation.utils.MobileInteractionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.appium.java_client.MobileElement;

public class LoginTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(LoginTest.class);
    private LoginPage loginPage;
    private TestDataManager testDataManager;
    private MobileInteractionUtils mobileUtils;
    private AppActionUtils appUtils;
    private AssertionUtils assertionUtils;

    @BeforeMethod
    public void setUp() {
        try {
            super.setUp();
            ThreadContext.put("threadId", String.valueOf(Thread.currentThread().getId()));
            loginPage = new LoginPage(driver);
            testDataManager = TestDataManager.getInstance();
            mobileUtils = new MobileInteractionUtils(driver);
            appUtils = new AppActionUtils(driver);
            assertionUtils = new AssertionUtils(mobileUtils);
            
            // Create test results directory
            FileUtils.createDirectory("test-results");
            
            // Wait for app to be ready
            appUtils.waitForAppToLoad(By.id("loadingIndicator"), 30);
            
            logger.info("Test setup completed for thread: {}", Thread.currentThread().getId());
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"login", "smoke"}, description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        try {
            logger.info("Starting valid login test");
            
            // Hard assertions for critical elements
            assertionUtils.assertElementPresent(By.id("usernameField"), 30, "Username field should be present");
            assertionUtils.assertElementPresent(By.id("passwordField"), 30, "Password field should be present");
            
            // Enter credentials
            String username = testDataManager.getData(TestCaseId.LOGIN_VALID, TestDataKey.USERNAME);
            String password = testDataManager.getData(TestCaseId.LOGIN_VALID, TestDataKey.PASSWORD);
            mobileUtils.type(By.id("usernameField"), username, 30);
            mobileUtils.type(By.id("passwordField"), password, 30);
            
            // Soft assertions for non-critical validations
            assertionUtils.softAssertElementVisible(By.id("rememberMe"), 10, "Remember me checkbox should be visible");
            assertionUtils.softAssertElementText(By.id("loginButton"), "Login", 10, "Login button text should be 'Login'");
            
            // Tap login button
            mobileUtils.tap(By.id("loginButton"), 30);
            
            // Wait for app to be ready after login
            appUtils.waitForAppToBeReady(By.id("homeScreen"), 30);
            
            // Critical assertions after login
            assertionUtils.assertElementPresent(By.id("welcomeMessage"), 30, "Welcome message should be present");
            assertionUtils.assertElementText(By.id("welcomeMessage"), "Welcome, " + username + "!", 30, 
                "Welcome message should match expected text");
            
            // Non-critical validations
            assertionUtils.softAssertElementCount(By.className("menuItem"), 5, 10, 
                "Should have 5 menu items");
            assertionUtils.softAssertElementVisible(By.id("profileIcon"), 10, 
                "Profile icon should be visible");
            
            // Save test results
            String result = "Login test passed successfully";
            FileUtils.writeToFile(result, "test-results/login_test_result.txt");
            
            // Assert all soft assertions
            assertionUtils.assertAll();
            
            logger.info("Valid login test completed successfully");
        } catch (Exception e) {
            logger.error("Valid login test failed: {}", e.getMessage());
            // Save error details
            FileUtils.appendToFile("Error: " + e.getMessage(), "test-results/login_test_result.txt");
            throw e;
        }
    }

    @Test(groups = {"login", "regression"}, description = "Verify login failure with invalid credentials")
    public void testInvalidLogin() {
        try {
            logger.info("Starting invalid login test");
            
            // Critical validations
            assertionUtils.assertElementEnabled(By.id("usernameField"), 30, 
                "Username field should be enabled");
            assertionUtils.assertElementEnabled(By.id("passwordField"), 30, 
                "Password field should be enabled");
            
            // Enter invalid credentials
            String username = testDataManager.getData(TestCaseId.LOGIN_INVALID, TestDataKey.USERNAME);
            String password = testDataManager.getData(TestCaseId.LOGIN_INVALID, TestDataKey.PASSWORD);
            mobileUtils.type(By.id("usernameField"), username, 30);
            mobileUtils.type(By.id("passwordField"), password, 30);
            
            // Tap login button
            mobileUtils.tap(By.id("loginButton"), 30);
            
            // Wait for error message
            appUtils.waitForAppToLoad(By.id("errorMessage"), 30);
            
            // Critical assertions
            assertionUtils.assertElementPresent(By.id("errorMessage"), 30, 
                "Error message should be present");
            assertionUtils.assertElementText(By.id("errorMessage"), "Invalid credentials", 30, 
                "Error message should match expected text");
            
            // Non-critical validations
            assertionUtils.softAssertElementVisible(By.id("forgotPassword"), 10, 
                "Forgot password link should be visible");
            assertionUtils.softAssertElementText(By.id("loginButton"), "Try Again", 10, 
                "Login button text should change to 'Try Again'");
            
            // Save test results
            String result = "Invalid login test passed successfully";
            FileUtils.writeToFile(result, "test-results/invalid_login_test_result.txt");
            
            // Assert all soft assertions
            assertionUtils.assertAll();
            
            logger.info("Invalid login test completed successfully");
        } catch (Exception e) {
            logger.error("Invalid login test failed: {}", e.getMessage());
            // Save error details
            FileUtils.appendToFile("Error: " + e.getMessage(), "test-results/invalid_login_test_result.txt");
            throw e;
        }
    }

    @Test(groups = {"login", "regression"}, description = "Verify validation messages for empty credentials")
    public void testEmptyLogin() {
        try {
            logger.info("Starting empty login test");
            
            // Critical validations
            assertionUtils.assertElementPresent(By.id("loginButton"), 30, 
                "Login button should be present");
            
            // Tap login button without entering credentials
            mobileUtils.tap(By.id("loginButton"), 30);
            
            // Wait for validation messages
            appUtils.waitForAppToLoad(By.id("validationMessage"), 30);
            
            // Critical assertions
            assertionUtils.assertElementText(By.id("validationMessage"), 
                "Please enter username and password", 30, 
                "Validation message should match expected text");
            
            // Non-critical validations
            assertionUtils.softAssertElementVisible(By.id("usernameField"), 10, 
                "Username field should remain visible");
            assertionUtils.softAssertElementVisible(By.id("passwordField"), 10, 
                "Password field should remain visible");
            
            // Save test results
            String result = "Empty login test passed successfully";
            FileUtils.writeToFile(result, "test-results/empty_login_test_result.txt");
            
            // Assert all soft assertions
            assertionUtils.assertAll();
            
            logger.info("Empty login test completed successfully");
        } catch (Exception e) {
            logger.error("Empty login test failed: {}", e.getMessage());
            // Save error details
            FileUtils.appendToFile("Error: " + e.getMessage(), "test-results/empty_login_test_result.txt");
            throw e;
        }
    }

    @Test(dataProvider = "excelData", dataProviderClass = BaseTest.class)
    public void testLoginWithExcelData(String username, String password, String expectedResult) {
        logger.info("Testing login with username: {}", username);
        
        // Example test steps (replace with actual app elements)
        MobileElement usernameField = driver.findElementByAccessibilityId("username");
        MobileElement passwordField = driver.findElementByAccessibilityId("password");
        MobileElement loginButton = driver.findElementByAccessibilityId("login");

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        // Verify result
        if ("success".equals(expectedResult)) {
            AssertionUtils.assertElementPresent(driver, "home_screen", "Home screen should be visible after successful login");
        } else {
            AssertionUtils.assertElementPresent(driver, "error_message", "Error message should be visible after failed login");
        }
    }

    @Test(dataProvider = "csvData", dataProviderClass = BaseTest.class)
    public void testLoginWithCSVData(String username, String password, String expectedResult) {
        logger.info("Testing login with username: {}", username);
        
        // Example test steps (replace with actual app elements)
        MobileElement usernameField = driver.findElementByAccessibilityId("username");
        MobileElement passwordField = driver.findElementByAccessibilityId("password");
        MobileElement loginButton = driver.findElementByAccessibilityId("login");

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        // Verify result
        if ("success".equals(expectedResult)) {
            AssertionUtils.assertElementPresent(driver, "home_screen", "Home screen should be visible after successful login");
        } else {
            AssertionUtils.assertElementPresent(driver, "error_message", "Error message should be visible after failed login");
        }
    }

    @Test(description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        loginPage.verifyLoginPageElements();
        
        loginPage.enterUsername("testuser");
        loginPage.enterPassword("password123");
        loginPage.checkRememberMe();
        
        HomePage homePage = loginPage.login("testuser", "password123");
        homePage.verifyHomePageElements();
        homePage.verifyWelcomeMessage("Welcome, testuser!");
    }

    @Test(description = "Verify login failure with invalid credentials")
    public void testInvalidLogin() {
        loginPage.verifyLoginPageElements();
        
        loginPage.enterUsername("invaliduser");
        loginPage.enterPassword("wrongpassword");
        
        loginPage.clickLoginButton();
        loginPage.verifyErrorMessage("Invalid username or password");
    }

    @Test(description = "Verify login with empty credentials")
    public void testEmptyCredentials() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickLoginButton();
        loginPage.verifyErrorMessage("Please enter username and password");
    }

    @Test(description = "Verify social login functionality")
    public void testSocialLogin() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickSocialLogin();
        // Add verification for social login page
    }

    @Test(description = "Verify forgot password functionality")
    public void testForgotPassword() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickForgotPassword();
        // Add verification for forgot password page
    }

    @Test(description = "Verify terms and conditions link")
    public void testTermsAndConditions() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickTermsAndConditions();
        // Add verification for terms and conditions page
    }

    @Test(description = "Verify privacy policy link")
    public void testPrivacyPolicy() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickPrivacyPolicy();
        // Add verification for privacy policy page
    }

    @Test(description = "Verify registration link")
    public void testRegistrationLink() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickRegister();
        // Add verification for registration page
    }

    @Test(description = "Verify help link")
    public void testHelpLink() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickHelp();
        // Add verification for help page
    }

    @Test(description = "Verify contact support link")
    public void testContactSupport() {
        loginPage.verifyLoginPageElements();
        
        loginPage.clickContactSupport();
        // Add verification for contact support page
    }

    @AfterMethod
    public void tearDown() {
        if (loginPage != null) {
            loginPage.cleanup();
        }
        ThreadContext.remove("threadId");
        super.tearDown();
        logger.info("Test cleanup completed for thread: {}", Thread.currentThread().getId());
    }
} 
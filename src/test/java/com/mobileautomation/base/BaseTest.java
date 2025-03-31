package com.mobileautomation.base;

import com.mobileautomation.config.AppConfig;
import com.mobileautomation.exceptions.MobileAutomationException;
import com.mobileautomation.utils.DeviceManager;
import com.mobileautomation.utils.TestDataProvider;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.time.Duration;

public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    @Getter
    protected AppiumDriver driver;

    @BeforeMethod
    public void setUp() {
        try {
            // Get platform from system property or default to iOS
            String platform = System.getProperty("platform", "iOS");
            logger.info("Setting up test environment for platform: {}", platform);

            // Initialize driver with platform-specific settings
            String deviceName = System.getProperty("deviceName", "");
            String platformVersion = System.getProperty("platformVersion", "");
            String appPath = System.getProperty("appPath", "");

            // Initialize driver with automatic device detection
            DeviceManager.initializeDriver(platform, deviceName, platformVersion, appPath);
            driver = DeviceManager.getDriver();
            logger.info("Test setup completed successfully");
        } catch (Exception e) {
            logger.error("Test setup failed: {}", e.getMessage());
            throw new RuntimeException("Test setup failed", e);
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            // Quit driver
            DeviceManager.quitDriver();
            
            // Stop Appium server
            DeviceManager.stopAppiumServer();
            
            logger.info("Test teardown completed successfully");
        } catch (Exception e) {
            logger.error("Test teardown failed: {}", e.getMessage());
        }
    }

    @DataProvider(name = "excelData")
    public Object[][] getExcelData() {
        return TestDataProvider.getTestDataFromExcel("Sheet1");
    }

    @DataProvider(name = "csvData")
    public Object[][] getCSVData() {
        return TestDataProvider.getTestDataFromCSV("testdata.csv");
    }
} 
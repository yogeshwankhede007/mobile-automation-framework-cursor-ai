package com.mobileautomation.listeners;

import com.mobileautomation.base.BaseTest;
import com.mobileautomation.utils.FileUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private String reportDir;

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test execution");
        String timestamp = DATE_FORMAT.format(new Date());
        reportDir = "run_reports/test_" + timestamp;
        FileUtils.createDirectory(reportDir);
        FileUtils.createDirectory(reportDir + "/screenshots");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        takeScreenshot(result, "success");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        logger.error("Error: {}", result.getThrowable().getMessage());
        takeScreenshot(result, "failure");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        takeScreenshot(result, "skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}", result.getName());
        takeScreenshot(result, "partial_failure");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test execution completed");
        logger.info("Total tests: {}", context.getAllTestMethods().length);
        logger.info("Passed tests: {}", context.getPassedTests().size());
        logger.info("Failed tests: {}", context.getFailedTests().size());
        logger.info("Skipped tests: {}", context.getSkippedTests().size());
    }

    private void takeScreenshot(ITestResult result, String status) {
        try {
            Object currentClass = result.getInstance();
            if (currentClass instanceof BaseTest) {
                AppiumDriver<MobileElement> driver = ((BaseTest) currentClass).getDriver();
                
                if (driver != null) {
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    String timestamp = DATE_FORMAT.format(new Date());
                    String screenshotName = result.getName() + "_" + status + "_" + timestamp + ".png";
                    String screenshotPath = reportDir + "/screenshots/" + screenshotName;
                    
                    FileUtils.copyFile(screenshot, new File(screenshotPath));
                    logger.info("Screenshot saved: {}", screenshotPath);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
        }
    }
} 
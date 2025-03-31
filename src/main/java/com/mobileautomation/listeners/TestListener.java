package com.mobileautomation.listeners;

import com.mobileautomation.driver.DriverManager;
import com.mobileautomation.utils.ScreenRecorder;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private ScreenRecorder screenRecorder;
    private static final String SCREENSHOT_DIR = "test-output/screenshots";
    private static final String VIDEO_DIR = "test-output/videos";

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
        startScreenRecording(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        stopScreenRecording();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        captureScreenshot(result.getName());
        stopScreenRecording();
        logTestFailure(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        stopScreenRecording();
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test suite started: {}", context.getName());
        createDirectories();
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test suite finished: {}", context.getName());
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            Files.createDirectories(Paths.get(VIDEO_DIR));
        } catch (IOException e) {
            logger.error("Failed to create directories", e);
        }
    }

    private void captureScreenshot(String testName) {
        try {
            AppiumDriver driver = DriverManager.getInstance().getDriver();
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = String.format("%s/%s_%s.png", SCREENSHOT_DIR, testName, timestamp);
            Files.copy(screenshot.toPath(), Paths.get(fileName));
            logger.info("Screenshot saved: {}", fileName);
        } catch (IOException e) {
            logger.error("Failed to capture screenshot", e);
        }
    }

    private void startScreenRecording(String testName) {
        try {
            screenRecorder = new ScreenRecorder(VIDEO_DIR, testName);
            screenRecorder.startRecording();
            logger.info("Screen recording started for test: {}", testName);
        } catch (Exception e) {
            logger.error("Failed to start screen recording", e);
        }
    }

    private void stopScreenRecording() {
        try {
            if (screenRecorder != null) {
                screenRecorder.stopRecording();
                logger.info("Screen recording stopped");
            }
        } catch (Exception e) {
            logger.error("Failed to stop screen recording", e);
        }
    }

    private void logTestFailure(ITestResult result) {
        logger.error("Test failure details:");
        logger.error("Test name: {}", result.getName());
        logger.error("Test class: {}", result.getTestClass().getName());
        logger.error("Failure reason: {}", result.getThrowable().getMessage());
        logger.error("Stack trace:", result.getThrowable());
    }
} 
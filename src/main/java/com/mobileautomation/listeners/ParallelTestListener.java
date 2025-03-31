package com.mobileautomation.listeners;

import com.mobileautomation.utils.ParallelTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class ParallelTestListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(ParallelTestListener.class);
    private final ParallelTestManager parallelManager;

    public ParallelTestListener() {
        this.parallelManager = ParallelTestManager.getInstance();
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            logger.info("Starting test context: {}", context.getName());
            parallelManager.initializeParallelExecution(context);
        } catch (Exception e) {
            logger.error("Failed to start test context: {}", e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            logger.info("Finishing test context: {}", context.getName());
            parallelManager.cleanupAllDevices();
        } catch (Exception e) {
            logger.error("Failed to finish test context: {}", e.getMessage());
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            String deviceId = result.getTestContext().getCurrentXmlTest().getParameter("deviceId");
            logger.info("Starting test {} on device {}", result.getName(), deviceId);
        } catch (Exception e) {
            logger.error("Failed to start test: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            String deviceId = result.getTestContext().getCurrentXmlTest().getParameter("deviceId");
            logger.info("Test {} passed on device {}", result.getName(), deviceId);
        } catch (Exception e) {
            logger.error("Failed to process test success: {}", e.getMessage());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            String deviceId = result.getTestContext().getCurrentXmlTest().getParameter("deviceId");
            logger.error("Test {} failed on device {}: {}", 
                result.getName(), deviceId, result.getThrowable().getMessage());
        } catch (Exception e) {
            logger.error("Failed to process test failure: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            String deviceId = result.getTestContext().getCurrentXmlTest().getParameter("deviceId");
            logger.warn("Test {} skipped on device {}", result.getName(), deviceId);
        } catch (Exception e) {
            logger.error("Failed to process test skip: {}", e.getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        try {
            String deviceId = result.getTestContext().getCurrentXmlTest().getParameter("deviceId");
            logger.warn("Test {} failed within success percentage on device {}", 
                result.getName(), deviceId);
        } catch (Exception e) {
            logger.error("Failed to process test failure within success percentage: {}", e.getMessage());
        }
    }
} 
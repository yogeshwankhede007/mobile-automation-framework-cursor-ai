package com.mobileautomation.test;

import com.mobileautomation.server.AppiumServerManager;
import com.mobileautomation.server.AppiumSetupManager;
import com.mobileautomation.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestGroupManager implements ISuiteListener, ITestListener {
    private static final Logger logger = LogManager.getLogger(TestGroupManager.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private String currentRunner;
    private String reportDir;
    private boolean isBrowserStackEnabled;
    private final AppiumServerManager serverManager;
    private final AppiumSetupManager setupManager;

    public TestGroupManager() {
        this.serverManager = AppiumServerManager.getInstance();
        this.setupManager = AppiumSetupManager.getInstance();
    }

    @Override
    public void onStart(ISuite suite) {
        logger.info("Starting test suite: {}", suite.getName());
        
        // Get the runner name from the suite XML
        XmlSuite xmlSuite = suite.getXmlSuite();
        currentRunner = xmlSuite.getName().toLowerCase().replaceAll("[^a-z0-9]", "_");
        
        // Create timestamped report directory
        String timestamp = DATE_FORMAT.format(new Date());
        reportDir = "run_reports/" + currentRunner + "_" + timestamp;
        FileUtils.createDirectory(reportDir);
        FileUtils.createDirectory(reportDir + "/screenshots");
        FileUtils.createDirectory(reportDir + "/videos");
        
        // Check if BrowserStack is enabled
        isBrowserStackEnabled = Boolean.parseBoolean(System.getProperty("browserstack.enabled", "false"));
        
        // Start Appium server if not using BrowserStack
        if (!isBrowserStackEnabled) {
            try {
                // Setup Appium environment
                setupManager.setupAppiumEnvironment();
                // Start Appium server
                serverManager.startServer();
            } catch (Exception e) {
                logger.error("Failed to initialize Appium environment: {}", e.getMessage());
                throw new RuntimeException("Failed to initialize Appium environment", e);
            }
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("Finishing test suite: {}", suite.getName());
        
        // Stop Appium server if not using BrowserStack
        if (!isBrowserStackEnabled) {
            try {
                // Stop Appium server
                serverManager.stopServer();
                // Cleanup Appium environment
                setupManager.cleanupAppiumEnvironment();
            } catch (Exception e) {
                logger.error("Failed to cleanup Appium environment: {}", e.getMessage());
                throw new RuntimeException("Failed to cleanup Appium environment", e);
            }
        }
        
        // Generate report
        generateReport(suite);
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
        saveTestResult(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {}", result.getName());
        saveTestResult(result, "FAIL");
        saveScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
        saveTestResult(result, "SKIP");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {}", result.getName());
        saveTestResult(result, "PARTIAL");
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test context: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finishing test context: {}", context.getName());
    }

    private void saveTestResult(ITestResult result, String status) {
        try {
            String testName = result.getName();
            String className = result.getTestClass().getName();
            String methodName = result.getMethod().getMethodName();
            String timestamp = DATE_FORMAT.format(new Date(result.getEndMillis()));
            
            String resultContent = String.format(
                "Test: %s\nClass: %s\nMethod: %s\nStatus: %s\nTime: %s\n",
                testName, className, methodName, status, timestamp
            );
            
            FileUtils.writeToFile(
                reportDir + "/" + currentRunner + "_results.txt",
                resultContent,
                true
            );
        } catch (Exception e) {
            logger.error("Error saving test result: {}", e.getMessage());
        }
    }

    private void saveScreenshot(ITestResult result) {
        try {
            // Implementation for saving screenshots
            // This would be implemented based on your screenshot capture mechanism
        } catch (Exception e) {
            logger.error("Error saving screenshot: {}", e.getMessage());
        }
    }

    private void generateReport(ISuite suite) {
        try {
            // Generate ChainTest report
            String reportContent = generateChainTestReport(suite);
            FileUtils.writeToFile(reportDir + "/" + currentRunner + ".html", reportContent);
            
            // Generate summary report
            String summaryContent = generateSummaryReport(suite);
            FileUtils.writeToFile(reportDir + "/" + currentRunner + "_summary.html", summaryContent);
            
            logger.info("Reports generated successfully in: {}", reportDir);
        } catch (Exception e) {
            logger.error("Error generating reports: {}", e.getMessage());
        }
    }

    private String generateChainTestReport(ISuite suite) {
        // Implementation for generating ChainTest report
        // This would be implemented based on your reporting requirements
        return "";
    }

    private String generateSummaryReport(ISuite suite) {
        // Implementation for generating summary report
        // This would be implemented based on your reporting requirements
        return "";
    }
} 
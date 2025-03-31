package com.mobileautomation.listeners;

import com.mobileautomation.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(RetryListener.class);
    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = AppConfig.getTestRetryCount();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.info("Retrying test {} for the {} time", result.getName(), retryCount);
            return true;
        }
        logger.info("Test {} failed after {} retries", result.getName(), MAX_RETRY_COUNT);
        return false;
    }
} 
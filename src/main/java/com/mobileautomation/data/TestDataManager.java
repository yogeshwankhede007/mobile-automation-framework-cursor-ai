package com.mobileautomation.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestDataManager {
    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private static volatile TestDataManager instance;
    private final TestDataProvider testDataProvider;
    private final Map<String, Map<String, String>> dataCache;
    private final ReadWriteLock cacheLock;

    private TestDataManager() {
        this.testDataProvider = new ExcelTestDataProvider();
        this.dataCache = new ConcurrentHashMap<>();
        this.cacheLock = new ReentrantReadWriteLock();
        initializeCache();
    }

    public static TestDataManager getInstance() {
        if (instance == null) {
            synchronized (TestDataManager.class) {
                if (instance == null) {
                    instance = new TestDataManager();
                }
            }
        }
        return instance;
    }

    private void initializeCache() {
        try {
            testDataProvider.loadTestData();
            logger.info("Test data cache initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize test data cache", e);
            throw new RuntimeException("Failed to initialize test data cache", e);
        }
    }

    public String getData(TestCaseId testCaseId, TestDataKey dataKey) {
        String cacheKey = generateCacheKey(testCaseId, dataKey);
        
        // Try to get from cache first
        cacheLock.readLock().lock();
        try {
            Map<String, String> testData = dataCache.get(testCaseId.getId());
            if (testData != null) {
                String value = testData.get(dataKey.getKey());
                if (value != null) {
                    return value;
                }
            }
        } finally {
            cacheLock.readLock().unlock();
        }

        // If not in cache, load from provider
        cacheLock.writeLock().lock();
        try {
            Map<String, String> testData = testDataProvider.getTestData(testCaseId.getId());
            dataCache.put(testCaseId.getId(), testData);
            String value = testData.get(dataKey.getKey());
            if (value == null) {
                logger.warn("No data found for test case {} and key {}", testCaseId, dataKey);
                return "";
            }
            return value;
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public Map<String, String> getAllData(TestCaseId testCaseId) {
        cacheLock.readLock().lock();
        try {
            Map<String, String> testData = dataCache.get(testCaseId.getId());
            if (testData != null) {
                return new ConcurrentHashMap<>(testData);
            }
        } finally {
            cacheLock.readLock().unlock();
        }

        cacheLock.writeLock().lock();
        try {
            Map<String, String> testData = testDataProvider.getTestData(testCaseId.getId());
            dataCache.put(testCaseId.getId(), testData);
            return new ConcurrentHashMap<>(testData);
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    public void refreshData() {
        cacheLock.writeLock().lock();
        try {
            dataCache.clear();
            testDataProvider.refreshTestData();
            logger.info("Test data cache refreshed successfully");
        } finally {
            cacheLock.writeLock().unlock();
        }
    }

    private String generateCacheKey(TestCaseId testCaseId, TestDataKey dataKey) {
        return testCaseId.getId() + ":" + dataKey.getKey();
    }
} 
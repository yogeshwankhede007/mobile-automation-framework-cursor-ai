package com.mobileautomation.utils;

import com.mobileautomation.exceptions.MobileAutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestRunner;
import org.testng.xml.XmlSuite;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelTestManager {
    private static final Logger logger = LogManager.getLogger(ParallelTestManager.class);
    private static ParallelTestManager instance;
    private final DeviceManager deviceManager;
    private final ConcurrentHashMap<String, TestRunner> activeTestRunners;
    private final ConcurrentHashMap<String, Integer> devicePorts;
    private ExecutorService executorService;

    private ParallelTestManager() {
        this.deviceManager = DeviceManager.getInstance();
        this.activeTestRunners = new ConcurrentHashMap<>();
        this.devicePorts = new ConcurrentHashMap<>();
    }

    public static ParallelTestManager getInstance() {
        if (instance == null) {
            instance = new ParallelTestManager();
        }
        return instance;
    }

    public void initializeParallelExecution(ITestContext context) {
        try {
            logger.info("Initializing parallel test execution...");
            deviceManager.detectDevices();
            
            if (!deviceManager.hasDevices()) {
                String error = "No devices available for test execution";
                String suggestion = "Please connect physical devices or start emulators/simulators";
                throw new MobileAutomationException("NO_DEVICES_AVAILABLE", error, suggestion);
            }

            // Create thread pool based on number of available devices
            int threadCount = deviceManager.getAvailableDevices().size();
            executorService = Executors.newFixedThreadPool(threadCount);
            
            // Assign ports to devices
            deviceManager.getAvailableDevices().forEach(device -> 
                devicePorts.put(device.getDeviceId(), deviceManager.getNextPort()));
            
            logger.info("Parallel execution initialized with {} devices", threadCount);
        } catch (Exception e) {
            String error = "Failed to initialize parallel execution";
            String suggestion = "Check device connections and permissions";
            throw new MobileAutomationException("PARALLEL_INIT_FAILED", error, suggestion, e);
        }
    }

    public void executeTestsInParallel(XmlSuite suite, ITestContext context) {
        try {
            List<DeviceInfo> devices = deviceManager.getAvailableDevices();
            logger.info("Starting parallel execution on {} devices", devices.size());

            for (DeviceInfo device : devices) {
                executorService.submit(() -> {
                    try {
                        executeTestOnDevice(suite, context, device);
                    } catch (Exception e) {
                        logger.error("Test execution failed on device {}: {}", 
                            device.getDeviceId(), e.getMessage());
                    }
                });
            }

            // Wait for all tests to complete
            executorService.shutdown();
            if (!executorService.awaitTermination(30, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            String error = "Failed to execute tests in parallel";
            String suggestion = "Check test configuration and device availability";
            throw new MobileAutomationException("PARALLEL_EXECUTION_FAILED", error, suggestion, e);
        }
    }

    private void executeTestOnDevice(XmlSuite suite, ITestContext context, DeviceInfo device) {
        try {
            logger.info("Starting test execution on device: {}", device.getDeviceName());
            
            // Create a new test runner for this device
            TestRunner runner = new TestRunner();
            runner.setSuite(suite);
            
            // Set device-specific parameters
            Map<String, String> parameters = new HashMap<>(suite.getParameters());
            parameters.put("deviceId", device.getDeviceId());
            parameters.put("platform", device.getPlatform());
            parameters.put("deviceName", device.getDeviceName());
            parameters.put("deviceVersion", device.getVersion());
            parameters.put("appiumPort", String.valueOf(devicePorts.get(device.getDeviceId())));
            
            runner.setParameters(parameters);
            
            // Store the runner for later cleanup
            activeTestRunners.put(device.getDeviceId(), runner);
            
            // Execute tests
            runner.run();
            
            logger.info("Test execution completed on device: {}", device.getDeviceName());
        } catch (Exception e) {
            logger.error("Failed to execute tests on device {}: {}", 
                device.getDeviceId(), e.getMessage());
            throw e;
        }
    }

    public void cleanupDevice(String deviceId) {
        try {
            TestRunner runner = activeTestRunners.remove(deviceId);
            if (runner != null) {
                runner.stop();
            }
            devicePorts.remove(deviceId);
            logger.info("Cleaned up resources for device: {}", deviceId);
        } catch (Exception e) {
            logger.warn("Failed to cleanup device {}: {}", deviceId, e.getMessage());
        }
    }

    public void cleanupAllDevices() {
        try {
            activeTestRunners.keySet().forEach(this::cleanupDevice);
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
            logger.info("Cleaned up all device resources");
        } catch (Exception e) {
            logger.warn("Failed to cleanup all devices: {}", e.getMessage());
        }
    }

    public int getDevicePort(String deviceId) {
        return devicePorts.getOrDefault(deviceId, 4723);
    }
} 
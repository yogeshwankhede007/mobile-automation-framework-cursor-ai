package com.mobileautomation.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AppiumSetupManager {
    private static final Logger logger = LogManager.getLogger(AppiumSetupManager.class);
    private static AppiumSetupManager instance;
    private final String npmPath;
    private final String appiumPath;
    private final String homeDir;

    private AppiumSetupManager() {
        this.homeDir = System.getProperty("user.home");
        this.npmPath = findNpmPath();
        this.appiumPath = findAppiumPath();
    }

    public static AppiumSetupManager getInstance() {
        if (instance == null) {
            instance = new AppiumSetupManager();
        }
        return instance;
    }

    public void setupAppiumEnvironment() {
        logger.info("Setting up Appium 2.x environment...");
        try {
            // Install Appium 2.x
            executeCommand(npmPath + " install -g appium@latest");
            
            // Install drivers
            executeCommand(appiumPath + " driver install uiautomator2");
            executeCommand(appiumPath + " driver install xcuitest");
            
            // Install plugins
            executeCommand(appiumPath + " plugin install gestures");
            executeCommand(appiumPath + " plugin install images");
            executeCommand(appiumPath + " plugin install ocr");
            executeCommand(appiumPath + " plugin install relaxed-caps");
            
            logger.info("Appium 2.x environment setup completed successfully");
        } catch (Exception e) {
            logger.error("Failed to setup Appium environment: {}", e.getMessage());
            throw new RuntimeException("Failed to setup Appium environment", e);
        }
    }

    public void cleanupAppiumEnvironment() {
        logger.info("Cleaning up Appium 2.x environment...");
        try {
            // Uninstall plugins
            executeCommand(appiumPath + " plugin uninstall gestures");
            executeCommand(appiumPath + " plugin uninstall images");
            executeCommand(appiumPath + " plugin uninstall ocr");
            executeCommand(appiumPath + " plugin uninstall relaxed-caps");
            
            // Uninstall drivers
            executeCommand(appiumPath + " driver uninstall uiautomator2");
            executeCommand(appiumPath + " driver uninstall xcuitest");
            
            // Uninstall Appium
            executeCommand(npmPath + " uninstall -g appium");
            
            // Clean up Appium directories
            cleanupAppiumDirectories();
            
            logger.info("Appium 2.x environment cleanup completed successfully");
        } catch (Exception e) {
            logger.error("Failed to cleanup Appium environment: {}", e.getMessage());
            throw new RuntimeException("Failed to cleanup Appium environment", e);
        }
    }

    private void cleanupAppiumDirectories() {
        String[] directories = {
            homeDir + "/.appium",
            homeDir + "/.npm",
            homeDir + "/.config/Appium"
        };

        for (String dir : directories) {
            deleteDirectory(new File(dir));
        }
    }

    private void deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            dir.delete();
        }
    }

    private String findNpmPath() {
        try {
            Process process = Runtime.getRuntime().exec("which npm");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String path = reader.readLine();
            if (path == null || path.isEmpty()) {
                throw new RuntimeException("npm not found in PATH");
            }
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find npm path", e);
        }
    }

    private String findAppiumPath() {
        try {
            Process process = Runtime.getRuntime().exec("which appium");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String path = reader.readLine();
            if (path == null || path.isEmpty()) {
                throw new RuntimeException("appium not found in PATH");
            }
            return path;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find appium path", e);
        }
    }

    private void executeCommand(String command) throws Exception {
        logger.debug("Executing command: {}", command);
        Process process = Runtime.getRuntime().exec(command);
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                error.append(line).append("\n");
            }
            throw new RuntimeException("Command failed with exit code " + exitCode + ": " + error.toString());
        }
    }
} 
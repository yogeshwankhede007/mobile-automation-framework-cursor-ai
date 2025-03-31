package com.mobileautomation.server;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class AppiumServerManager {
    private static final Logger logger = LogManager.getLogger(AppiumServerManager.class);
    private static AppiumServerManager instance;
    private AppiumDriverLocalService service;
    private static final int DEFAULT_PORT = 4723;
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 2;

    // Appium 2.x Plugin and Driver paths
    private static final String APPIUM_BASE_PATH = System.getProperty("user.home") + "/.appium";
    private static final String PLUGINS_PATH = APPIUM_BASE_PATH + "/node_modules";
    private static final String DRIVERS_PATH = APPIUM_BASE_PATH + "/node_modules/appium/drivers";

    private AppiumServerManager() {}

    public static AppiumServerManager getInstance() {
        if (instance == null) {
            instance = new AppiumServerManager();
        }
        return instance;
    }

    public void startServer() {
        if (service != null && service.isRunning()) {
            logger.info("Appium server is already running on port: {}", service.getUrl().getPort());
            return;
        }

        try {
            AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(DEFAULT_PORT)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "debug")
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withArgument(GeneralServerFlag.NO_RESET)
                .withArgument(GeneralServerFlag.LOG_TIMESTAMP)
                .withArgument(GeneralServerFlag.LOCAL_TIMEZONE)
                .withArgument(GeneralServerFlag.WEBHOOK)
                .withArgument(GeneralServerFlag.ALLOW_INSECURE, "chromedriver_autodownload");

            // Add Appium 2.x specific arguments
            builder.withArgument(GeneralServerFlag.USE_DRIVERS, "uiautomator2,xcuitest")
                   .withArgument(GeneralServerFlag.USE_PLUGINS, "gestures,images,ocr,relaxed-caps")
                   .withArgument(GeneralServerFlag.PLUGINS_PATH, PLUGINS_PATH)
                   .withArgument(GeneralServerFlag.DRIVERS_PATH, DRIVERS_PATH);

            // Add Android specific arguments
            builder.withArgument(GeneralServerFlag.ANDROID_DEVICE_READY_TIMEOUT, "60")
                   .withArgument(GeneralServerFlag.ANDROID_INSTALL_TIMEOUT, "90000")
                   .withArgument(GeneralServerFlag.ANDROID_DRIVER, "uiautomator2");

            // Add iOS specific arguments
            builder.withArgument(GeneralServerFlag.WEB_DRIVER_AGENT_PORT, "8100")
                   .withArgument(GeneralServerFlag.WDA_LOCAL_PORT, "8100")
                   .withArgument(GeneralServerFlag.IOS_DRIVER, "xcuitest");

            service = AppiumDriverLocalService.buildService(builder);
            service.start();
            waitForServerToBeReady();
            logger.info("Appium server started successfully on port: {}", service.getUrl().getPort());
        } catch (Exception e) {
            logger.error("Failed to start Appium server: {}", e.getMessage());
            throw new RuntimeException("Failed to start Appium server", e);
        }
    }

    public void stopServer() {
        if (service != null && service.isRunning()) {
            try {
                service.stop();
                logger.info("Appium server stopped successfully");
            } catch (Exception e) {
                logger.error("Failed to stop Appium server: {}", e.getMessage());
            }
        }
    }

    private void waitForServerToBeReady() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                Socket socket = new Socket("127.0.0.1", DEFAULT_PORT);
                socket.close();
                return;
            } catch (Exception e) {
                retries++;
                if (retries == MAX_RETRIES) {
                    throw new RuntimeException("Appium server failed to start after " + MAX_RETRIES + " attempts");
                }
                try {
                    TimeUnit.SECONDS.sleep(RETRY_DELAY);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public String getServerUrl() {
        return service != null ? service.getUrl().toString() : null;
    }
} 
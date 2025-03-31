package com.mobileautomation.utils;

import com.mobileautomation.exceptions.MobileAutomationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {
    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    public static void createDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
            logger.info("Created directory: {}", path);
        } catch (IOException e) {
            logger.error("Failed to create directory {}: {}", path, e.getMessage());
            throw new RuntimeException("Failed to create directory: " + path, e);
        }
    }

    public static void deleteDirectory(String path) {
        try {
            logger.debug("Deleting directory: {}", path);
            Path directory = Paths.get(path);
            if (Files.exists(directory)) {
                Files.walk(directory)
                        .sorted((a, b) -> -a.compareTo(b))
                        .forEach(path1 -> {
                            try {
                                Files.delete(path1);
                            } catch (IOException e) {
                                logger.warn("Failed to delete file: {}", path1);
                            }
                        });
            }
        } catch (IOException e) {
            String error = String.format("Failed to delete directory: %s", path);
            String suggestion = "Check permissions and ensure directory is not in use";
            throw new MobileAutomationException("DIRECTORY_DELETION_FAILED", error, suggestion, e);
        }
    }

    public static void copyFile(File source, File destination) {
        try {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Copied file from {} to {}", source.getPath(), destination.getPath());
        } catch (IOException e) {
            logger.error("Failed to copy file from {} to {}: {}", 
                source.getPath(), destination.getPath(), e.getMessage());
            throw new RuntimeException("Failed to copy file", e);
        }
    }

    public static void writeToFile(String content, String filePath) {
        try {
            logger.debug("Writing to file: {}", filePath);
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            String error = String.format("Failed to write to file: %s", filePath);
            String suggestion = "Check file permissions and ensure directory exists";
            throw new MobileAutomationException("FILE_WRITE_FAILED", error, suggestion, e);
        }
    }

    public static String readFromFile(String filePath) {
        try {
            logger.debug("Reading from file: {}", filePath);
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            String error = String.format("Failed to read from file: %s", filePath);
            String suggestion = "Check if file exists and is readable";
            throw new MobileAutomationException("FILE_READ_FAILED", error, suggestion, e);
        }
    }

    public static List<String> listFiles(String directory) {
        try {
            logger.debug("Listing files in directory: {}", directory);
            return Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            String error = String.format("Failed to list files in directory: %s", directory);
            String suggestion = "Check if directory exists and is accessible";
            throw new MobileAutomationException("FILE_LIST_FAILED", error, suggestion, e);
        }
    }

    public static boolean fileExists(String path) {
        return Files.exists(Path.of(path));
    }

    public static void appendToFile(String content, String filePath) {
        try {
            logger.debug("Appending to file: {}", filePath);
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            String error = String.format("Failed to append to file: %s", filePath);
            String suggestion = "Check file permissions and ensure directory exists";
            throw new MobileAutomationException("FILE_APPEND_FAILED", error, suggestion, e);
        }
    }

    public static void moveFile(String source, String destination) {
        try {
            logger.debug("Moving file from {} to {}", source, destination);
            Path sourcePath = Paths.get(source);
            Path destinationPath = Paths.get(destination);
            
            // Create parent directories if they don't exist
            Files.createDirectories(destinationPath.getParent());
            
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String error = String.format("Failed to move file from %s to %s", source, destination);
            String suggestion = "Check file permissions and ensure source file exists";
            throw new MobileAutomationException("FILE_MOVE_FAILED", error, suggestion, e);
        }
    }

    public static boolean deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
            logger.info("Deleted file: {}", path);
            return true;
        } catch (IOException e) {
            logger.error("Failed to delete file {}: {}", path, e.getMessage());
            return false;
        }
    }
} 
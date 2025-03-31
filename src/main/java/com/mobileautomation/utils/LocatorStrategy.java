package com.mobileautomation.utils;

public enum LocatorStrategy {
    ID("id"),
    CLASS_NAME("class name"),
    ACCESSIBILITY_ID("accessibility id"),
    XPATH("xpath"),
    ANDROID_UIAUTOMATOR("android uiautomator"),
    IOS_PREDICATE("ios predicate string"),
    IOS_CLASS_CHAIN("ios class chain");

    private final String value;

    LocatorStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static LocatorStrategy fromString(String text) {
        for (LocatorStrategy strategy : LocatorStrategy.values()) {
            if (strategy.value.equalsIgnoreCase(text)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unknown locator strategy: " + text);
    }
} 
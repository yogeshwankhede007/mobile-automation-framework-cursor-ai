package com.mobileautomation.data;

public enum TestDataKey {
    USERNAME("username"),
    PASSWORD("password"),
    EMAIL("email"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    PHONE_NUMBER("phoneNumber"),
    ADDRESS("address"),
    CITY("city"),
    STATE("state"),
    ZIP_CODE("zipCode"),
    COUNTRY("country");

    private final String key;

    TestDataKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static TestDataKey fromString(String text) {
        for (TestDataKey dataKey : TestDataKey.values()) {
            if (dataKey.key.equalsIgnoreCase(text)) {
                return dataKey;
            }
        }
        throw new IllegalArgumentException("Unknown test data key: " + text);
    }
} 
package com.mobileautomation.data;

public enum TestCaseId {
    LOGIN_VALID("LOGIN_001"),
    LOGIN_INVALID("LOGIN_002"),
    LOGIN_EMPTY("LOGIN_003"),
    REGISTER_NEW_USER("REG_001"),
    FORGOT_PASSWORD("FP_001");

    private final String id;

    TestCaseId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static TestCaseId fromString(String text) {
        for (TestCaseId testCase : TestCaseId.values()) {
            if (testCase.id.equalsIgnoreCase(text)) {
                return testCase;
            }
        }
        throw new IllegalArgumentException("Unknown test case ID: " + text);
    }
} 
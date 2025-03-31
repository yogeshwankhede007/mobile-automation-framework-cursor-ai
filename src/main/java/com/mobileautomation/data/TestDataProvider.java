package com.mobileautomation.data;

import java.util.Map;

public interface TestDataProvider {
    Map<String, String> getTestData(String testCaseId);
    void loadTestData();
    void refreshTestData();
} 
package com.mobileautomation.exceptions;

public class MobileAutomationException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;
    private final String suggestion;

    public MobileAutomationException(String errorCode, String errorMessage, String suggestion, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.suggestion = suggestion;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuggestion() {
        return suggestion;
    }

    @Override
    public String toString() {
        return String.format("MobileAutomationException[code=%s, message=%s, suggestion=%s]",
                errorCode, errorMessage, suggestion);
    }
} 
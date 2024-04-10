package com.caching.cachingtest.exception;

/* This class is a custom exception that extends the RuntimeException class.
This exception is thrown when there’s an issue related to caching operations within the module */
public class CacheModuleException extends RuntimeException {

    public CacheModuleException() {
    }

    public CacheModuleException(String message) {
        super(message);
    }

    public CacheModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheModuleException(Throwable cause) {
        super(cause);
    }

    public CacheModuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

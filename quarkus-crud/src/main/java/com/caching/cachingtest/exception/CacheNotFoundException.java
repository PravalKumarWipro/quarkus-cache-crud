package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue finding a key in the cache */
public class CacheNotFoundException extends CacheModuleException {
    public CacheNotFoundException() {
    }

    public CacheNotFoundException(String message) {
        super(message);
    }

    public CacheNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheNotFoundException(Throwable cause) {
        super(cause);
    }

    public CacheNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

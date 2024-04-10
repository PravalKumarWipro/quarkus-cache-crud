package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue inserting the key to the cache */
public class UnableToAddKeyException extends CacheModuleException {
    public UnableToAddKeyException() {
    }

    public UnableToAddKeyException(String message) {
        super(message);
    }

    public UnableToAddKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToAddKeyException(Throwable cause) {
        super(cause);
    }

    public UnableToAddKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

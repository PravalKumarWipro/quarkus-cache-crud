package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when a key being inserted already exists in the cache */
public class KeyAlreadyExistsException extends CacheModuleException {
    public KeyAlreadyExistsException() {
    }

    public KeyAlreadyExistsException(String message) {
        super(message);
    }

    public KeyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public KeyAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

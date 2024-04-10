package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue inserting the key to the cache as key was already present in the cache */
public class KeyExistsException extends CacheModuleException {
    public KeyExistsException() {
    }

    public KeyExistsException(String message) {
        super(message);
    }

    public KeyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyExistsException(Throwable cause) {
        super(cause);
    }

    public KeyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

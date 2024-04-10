package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue inserting the key to the cache due to ttl value less than 0 */
public class InvalidTTLException extends CacheModuleException {
    public InvalidTTLException() {
    }

    public InvalidTTLException(String message) {
        super(message);
    }

    public InvalidTTLException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTTLException(Throwable cause) {
        super(cause);
    }

    public InvalidTTLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

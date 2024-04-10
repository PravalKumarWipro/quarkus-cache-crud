package com.caching.cachingtest.exception;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.model.Response;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/* Global exception handler to handle all exceptions while dealing with CacheModuleException class*/
@Provider
public class MyApplicationExceptionHandler implements ExceptionMapper<CacheModuleException> {
    @Override
    public javax.ws.rs.core.Response toResponse(CacheModuleException exception) {
        Response response = new Response(AppConstants.FAILED);
        response.setMessage(exception.getMessage());
        return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(response).build();
    }
}
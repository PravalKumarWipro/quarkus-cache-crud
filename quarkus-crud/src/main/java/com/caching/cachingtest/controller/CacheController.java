package com.caching.cachingtest.controller;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import com.caching.cachingtest.model.Response;
import com.caching.cachingtest.service.CacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path("/caching")
public class CacheController {

    @Inject
    public CacheServiceImpl userServiceImpl;

    @Inject
    private CacheDao cacheDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheController.class);


    /***
     * Endpoint to test the Client
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/test")
    public javax.ws.rs.core.Response testApi() {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            response.setMessage("Cache Client :: " + cacheDao.getClient());
            LOGGER.info("In testApi() Cache client Configured : {}", cacheDao.getClient());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            response.setStatus("Internal Server Error");
            response.setMessage("Error occurred : " + e.getMessage());
            LOGGER.error("In testApi() Error while connecting to client :: {} \t stacktrace : {}",e.getMessage(), Arrays.toString(e.getStackTrace()));
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }



    /***
     * Endpoint to retrieve a value from the cache based on a key
     * @param key
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{key}")
    public javax.ws.rs.core.Response getKey(@PathParam("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.info("In getKey() fetching value with key : {} ", key);
            response.setKey(key);
            response.setValue(userServiceImpl.getValueByKey(key));
            LOGGER.info("In getKey() value found with key {} ", key);
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }catch(Exception e){
            LOGGER.error("In getKey() Error occurred while fetching the value for key: {}, \t stacktrace : {}", key, Arrays.toString(e.getStackTrace()));
            response.setStatus("Not Found");
            response.setMessage("Error occurred : "+e.getMessage());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }


    /***
     *  Endpoint to delete a value from the cache based on a key
     * @param key
     * @return
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{key}")
    public javax.ws.rs.core.Response deleteKey(@PathParam("key") String key) {
        Response response = new Response(AppConstants.SUCCESS);
        try {
            LOGGER.info("In deleteKey() key : {} ", key);
            userServiceImpl.delete(key);
            response.setMessage("key " + key + " removed");
            LOGGER.info("In deleteKey() Successfully removed key : {} ", key);
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.OK)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            LOGGER.error("In deleteKey() Error occurred while deleting key: {}\t stacktrace : {}", key, Arrays.toString(e.getStackTrace()));
            response.setStatus("Not Found");
            response.setMessage("Error occurred : " + e.getMessage());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }


    /***
     * Endpoint to add or update a key in the cache
     * @param cacheMap
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    public javax.ws.rs.core.Response addKey(CacheMap cacheMap) {
        LOGGER.info("In addKey() key : {} ", cacheMap.getKey());
        Response response = new Response(AppConstants.SUCCESS);
        try {
            if (cacheMap.getTtl() == null) {
                cacheMap.setTtl(Long.MAX_VALUE);
            }
            userServiceImpl.saveOrUpdate(cacheMap);
            response.setKey(cacheMap.getKey());
            response.setValue(cacheMap.getValue());
            response.setMessage("key " + cacheMap.getKey() + " added");
            LOGGER.info("In addKey() Key : {} added",cacheMap.getKey());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.CREATED)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }catch (KeyExistsException keyExistsException){
            LOGGER.error("In addKey() key : {} already existing in cache\t exception : {}\t stacktrace : ",cacheMap.getKey(),keyExistsException.getMessage(),Arrays.toString(keyExistsException.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage(keyExistsException.getMessage());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }catch (InvalidTTLException invalidTTLException){
            LOGGER.error("In addKey() key : {} invalid ttl\t exception : {}\t stacktrace : ",cacheMap.getKey(),invalidTTLException.getMessage(),Arrays.toString(invalidTTLException.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage(invalidTTLException.getMessage());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }catch (Exception e) {
            LOGGER.error("In addKey() key : {} exception : {}\t stacktrace : ",cacheMap.getKey(),e.getMessage(), Arrays.toString(e.getStackTrace()));
            response.setStatus("Bad Request");
            response.setMessage("Error occurred : " + e.getMessage());
            return javax.ws.rs.core.Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}

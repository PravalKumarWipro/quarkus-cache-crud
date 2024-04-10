package com.caching.cachingtest.dao;

import com.caching.cachingtest.AppConstants;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import com.caching.cachingtest.model.CacheMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;

/* DAO class for caching CRUD operations */
@ApplicationScoped
public class CacheDao {

    @ConfigProperty(name = "cache.client", defaultValue = AppConstants.CACHE_APACHE_IGNITE)
    String cacheClient;
    @Inject
    private ApacheIgniteClient apacheIgniteClient;

    @Inject
    private RedisClient redisClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheDao.class);

    /***
     * Retrieves a value from the cache based on the provided key
     * @param key
     * @return
     * @throws CacheNotFoundException
     */
    public String getUserById(String key) throws CacheNotFoundException {
        try {
            LOGGER.info("In getUserById() Retrieving value for key: {}, client :: {}",key,cacheClient);
            return getClient().getValueById(key);
        }catch(CacheNotFoundException e){
            LOGGER.error("In getUserById() key : {} not found exception : {},\t stacktrace : {}",key, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    /***
     * Deletes a value from the cache using the specified key
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        try {
            LOGGER.info("In delete() Deleting key {}, client :: {}", key, cacheClient);
            return getClient().delete(key);
        }catch(CacheNotFoundException e){
            LOGGER.error("In delete() Failed to delete key :{} from cache: {}, exception : {}\t stacktrace : {} ",key, cacheClient, Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    /***
     *  Saves or updates a value in the cache with the given key and value
     * @param cacheMap
     */
    public void saveOrUpdate(CacheMap cacheMap) {
        try {
            synchronized (this){
                getClient().saveOrUpdate(cacheMap);
                LOGGER.info("In saveOrUpdate() Added key : {}, client : {} ", cacheMap.getKey(), cacheClient);
            }
        }catch (KeyExistsException keyExistsException){
            throw  keyExistsException;
        }catch (UnableToAddKeyException e) {
            throw e;
        }
    }

    /***
     * Determines the appropriate cache client based on the configured type
     * @return
     */
    public GenericCacheClient getClient() {
        GenericCacheClient genericCacheClient = apacheIgniteClient;
        switch (cacheClient) {
            case AppConstants.CACHE_REDIS: {
                genericCacheClient = redisClient;
                break;
            }
        }
        LOGGER.info("In getClient() Selected cache client :  " + genericCacheClient);
        return genericCacheClient;
    }
}

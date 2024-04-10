package com.caching.cachingtest.service;

import com.caching.cachingtest.dao.CacheDao;
import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.InvalidTTLException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.exception.UnableToAddKeyException;
import com.caching.cachingtest.model.CacheMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;

/* This class provides essential methods for managing cache data*/

@ApplicationScoped
public class CacheServiceImpl implements CacheService {

    @Inject
    public CacheDao cacheDao;
    private static final Logger logger= LoggerFactory.getLogger(CacheServiceImpl.class);

    /***
     * Retrieves a value from the cache based on the given key
     * @param key
     * @return
     * @throws CacheNotFoundException
     */
    @Override
    public String getValueByKey(String key) throws CacheNotFoundException {
        try {
            String value = cacheDao.getUserById(key);
            if (value == null || value.length() == 0) {
                throw new CacheNotFoundException("key " + key + " Not Found");
            }
            return value;
        } catch (CacheNotFoundException e) {
            logger.error("In getValueByKey() Exception while fetching value by key excepion : {}\t stacktrace : {}", e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }


    /***
     * Deletes a value from the cache using the specified key
     * @param key
     * @throws CacheNotFoundException
     */
    @Override
    public void delete(String key) throws CacheNotFoundException {
        try {
            Boolean status = cacheDao.delete(key);
            if (!status) {
                throw new CacheNotFoundException("key " + key + " Not Found");
            }
        } catch (CacheNotFoundException e) {
            logger.error("In delete() key not found exception :  {}\t stacktrace : {}", e.getMessage(),Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }


    /***
     * Saves or updates a value in the cache with the given key and value
     * @param cacheMap
     * @throws UnableToAddKeyException
     */
    @Override
    public void saveOrUpdate(CacheMap cacheMap) throws UnableToAddKeyException {
        try {
            if (cacheMap.getTtl() <= 0) {
                throw new InvalidTTLException("Invalid TTL - TTL should be > 0");
            }
            cacheDao.saveOrUpdate(cacheMap);
        } catch (KeyExistsException keyExistsException) {
            throw keyExistsException;
        } catch (InvalidTTLException invalidTTLException) {
            throw invalidTTLException;
        } catch (Exception e) {
            logger.error("In saveOrUpdate()  key : {} Unable To Save exception : {}\t stacktrace : {}", cacheMap.getKey(),e.getMessage(),Arrays.toString(e.getStackTrace()));
            throw new UnableToAddKeyException("key" + cacheMap.getKey() + " Unable To Save");
        }
    }
}

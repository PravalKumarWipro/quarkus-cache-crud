package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.CacheNotFoundException;
import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/* Interacts with Redis for caching purposes */
@ApplicationScoped
public class RedisClient implements GenericCacheClient {

    @Inject
    RedissonClient redissonClient;

    @ConfigProperty(name = "cache.name", defaultValue = "Cache")
    String cacheName;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

    /***
     * Retrieves a value from the Redis cache based on the provided key
     * @param key
     * @return
     * @throws CacheNotFoundException
     */
    public String getValueById(String key) throws CacheNotFoundException {
        try {
            LOGGER.info("In getValueById() client ::: REDIS searching with key :: {}", key);
            RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
            String value = userCache.get(String.valueOf(key));
            LOGGER.info("In getValueById() client ::: REDIS key :: {} found", key);
            return value;
        }catch(CacheNotFoundException e){
            LOGGER.error("In getValueById() client :::REDIS Error while searching with key :: {}",key);
            throw e;
        }
    }



    /***
     *  Deletes a value from the Redis cache based on the provided key
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        try {
            LOGGER.info("In delete() client ::: REDIS deleting the key :: {}", key);
            RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
            String respose = userCache.remove(String.valueOf(key));
            if (respose == null) {
                return false;
            }
            LOGGER.info("In delete() client ::: REDIS deleting the key :: {} success", key);
            return true;
        }catch(CacheNotFoundException e){
            LOGGER.error("In delete() client ::: REDIS Error while deleting key : {}  from ApacheClient exception : {}, stacktrace : {} ",key, e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    /***
     * Saves Or Updates key-value to the Redis cache
     * @param cacheMap
     */
    public void saveOrUpdate(CacheMap cacheMap) {
        try {
            RMapCache<String, String> userCache = redissonClient.getMapCache(cacheName);
            LOGGER.info("In saveOrUpdate() client ::: REDIS  Cache Name :: {} trying  adding the key :: {}", cacheName, cacheMap.getKey());
            String existingValue = userCache.get(cacheMap.getKey());
            if(existingValue !=null){
                LOGGER.error("In saveOrUpdate() client ::: REDIS Cache Name :: {}  adding the key : {}  failed as Key Already exists",cacheName, cacheMap.getKey());
                throw new KeyExistsException("key "+cacheMap.getKey()+" already exists, cannot continue!");
            }
            LOGGER.info("In saveOrUpdate() client ::: REDIS Cache Name :: {}  adding the key : {}  success",cacheName, cacheMap.getKey());
            userCache.put(String.valueOf(cacheMap.getKey()), cacheMap.getValue(), cacheMap.getTtl(), TimeUnit.SECONDS);
        }catch(Exception e){
            LOGGER.error("In saveOrUpdate() client ::: REDIS Error while adding key : {}  exception : {}, stacktrace : {}",cacheMap.getKey(), e.getMessage(), Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    @Override
    public String toString() {
        return "RedisClient";
    }
}

package com.caching.cachingtest.dao;

import com.caching.cachingtest.exception.KeyExistsException;
import com.caching.cachingtest.model.CacheMap;
import org.apache.ignite.cache.CacheRebalanceMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.ClientCacheConfiguration;
import org.apache.ignite.client.IgniteClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/*This class interacts with Apache Ignite Cache and performs all cache operations like save/Update, delete and getValueByKey*/
@ApplicationScoped
public class ApacheIgniteClient implements GenericCacheClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApacheIgniteClient.class);

    @Inject
    IgniteClient igniteClient;

    @ConfigProperty(name = "cache.name", defaultValue = "Cache")
    String cacheName;

    @ConfigProperty(name = "cache.rebalance.mode", defaultValue = "SYNC")
    String cacheRebalanceMode;

    /***
     * Retrieves the value associated with the given key from the cache
     * @param key
     * @return
     */
    public String getValueById(String key) {
        try {
            LOGGER.info("In getValueById() client ::: APACHE IGNITE searching with key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            String value = clientCache.get(key);
            LOGGER.info("In getValueById() client ::: APACHE IGNITE key :: {} found", key);
            return value;
        } catch (Exception e) {
            LOGGER.error("In getValueById() client ::: APACHE IGNITE Error while searching with key :: {}",key);
            return null;
        }
    }


    /***
     * Deletes the entry associated with the given key from the cache
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        try {
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {}", key);
            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheName);
            Boolean status = clientCache.remove(key);
            LOGGER.info("In delete() client ::: APACHE IGNITE deleting the key :: {} success", key);
            return status;
        } catch (Exception e) {
            LOGGER.error("In delete() client ::: APACHE IGNITE Error while deleting key : {}  from ApacheClient exception : {}, stacktrace : {} ",key, e.getMessage(), Arrays.toString(e.getStackTrace()));
            return false;
        }
    }


    /***
     * Saves or updates the value associated with the given key in the cache
     * @param cacheMap
     */
    public void saveOrUpdate(CacheMap cacheMap) {
        ClientCacheConfiguration cacheConfiguration = new ClientCacheConfiguration();
        cacheConfiguration.setName(cacheName);
        CacheRebalanceMode cacheRebalanceMode = getCacheRebalanceingModeFromConfig();
        LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" trying adding the key : "+ cacheMap.getKey());
        cacheConfiguration.setRebalanceMode(cacheRebalanceMode);
        ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(cacheConfiguration).withExpirePolicy(new CreatedExpiryPolicy(new Duration(TimeUnit.SECONDS, cacheMap.getTtl())));
        clientCache.getConfiguration();
        String existingValue = clientCache.get(cacheMap.getKey());
        if(existingValue !=null){
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed as Key Already exists");
            throw new KeyExistsException("key "+cacheMap.getKey()+" already exists, cannot continue!");
        }
        try {
            clientCache.put(cacheMap.getKey(), cacheMap.getValue());
            LOGGER.info("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" success");
        } catch (Exception e) {
            LOGGER.error("In saveOrUpdate() client ::: APACHE IGNITE  Rebalancing Mode ::: "+cacheRebalanceMode+"\t Cache Name ::"+cacheName+" adding the key : "+ cacheMap.getKey()+" failed Error : "+e.getMessage()+"\t stacktrace : "+ Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Error :  " + e.getMessage(), e);
        }
    }

    /***
     *
     * @return
     */
    @Override
    public String toString() {
        return "ApacheIgniteClient";
    }

    /***
     * This method returns the using CacheRebalanceMode which is read from config
     * @return
     */

    public CacheRebalanceMode getCacheRebalanceingModeFromConfig(){
        CacheRebalanceMode finalCacheRebalanceMode = null;
        switch(cacheRebalanceMode){
            case "SYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
            case "ASYNC":
                finalCacheRebalanceMode =  CacheRebalanceMode.ASYNC;
                break;
            case "NONE":
                finalCacheRebalanceMode =  CacheRebalanceMode.NONE;
                break;
            default:
                finalCacheRebalanceMode =  CacheRebalanceMode.SYNC;
                break;
        }
        return finalCacheRebalanceMode;
    }

}

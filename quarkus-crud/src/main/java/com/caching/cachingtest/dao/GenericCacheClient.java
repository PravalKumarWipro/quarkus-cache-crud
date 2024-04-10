package com.caching.cachingtest.dao;

import com.caching.cachingtest.model.CacheMap;

/* This interface defines the methods to interact with Cache */
public interface GenericCacheClient {
    String getValueById(String key);

    Boolean delete(String key);

    void saveOrUpdate(CacheMap cacheMap);
}

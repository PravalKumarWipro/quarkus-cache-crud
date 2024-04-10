package com.caching.cachingtest.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*  Simple model representing a key-value pair for caching purposes */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheMap {
    private String key;
    private String value;
    private Long ttl;

    @Override
    public String toString() {
        return "CacheMap{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", ttl=" + ttl +
                '}';
    }
}

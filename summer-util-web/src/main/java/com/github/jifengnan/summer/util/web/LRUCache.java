package com.github.jifengnan.summer.util.web;

import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * <p>一个简单的LRU缓存。</p>
 *
 * @author jifengnan  2019-03-06
 */
public class LRUCache {
    private final SimpleCache cache;
    private static final int DEFAULT_CACHE_SIZE = 100;

    /**
     * 创建一个容量为100的LRU缓存
     */
    public LRUCache() {
        cache = new SimpleCache(DEFAULT_CACHE_SIZE, true);
    }

    /**
     * 创建一个指定容量的LRU缓存
     *
     * @param cacheSize 缓存的预期容量
     * @throws IllegalArgumentException 如果cacheSize小于1
     */
    public LRUCache(int cacheSize) {
        if (cacheSize < 1) {
            throw new IllegalArgumentException("缓存的容量不能小于1");
        }
        cache = new SimpleCache(cacheSize, true);
    }

    public Object put(String key, Object value) {
        return cache.put(key, value);
    }

    public Object putIfAbsent(String key, Object value) {
        return cache.putIfAbsent(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }
}

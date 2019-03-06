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
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final int DEFAULT_CACHE_SIZE = 100;
    private final int cacheSize;

    /**
     * 创建一个容量为100的LRU缓存
     */
    public LRUCache() {
        this(DEFAULT_CACHE_SIZE, 1f);
    }

    /**
     * 创建一个指定容量的LRU缓存
     *
     * @param cacheSize 缓存的预期容量
     * @throws IllegalArgumentException 如果cacheSize小于0
     */
    public LRUCache(int cacheSize) {
        this(cacheSize, 0.75f);
    }

    private LRUCache(int cacheSize, float loadFactor) {
        super(cacheSize, loadFactor, true);
        this.cacheSize = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > cacheSize;
    }
}

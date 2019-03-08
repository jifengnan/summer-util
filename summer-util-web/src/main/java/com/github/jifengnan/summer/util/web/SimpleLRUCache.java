package com.github.jifengnan.summer.util.web;

/**
 * <p>一个简单的LRU缓存。</p>
 * 本缓存是线程安全的。
 *
 * @author jifengnan  2019-03-06
 */
public class SimpleLRUCache {
    private final SimpleCache container;
    private static final int DEFAULT_CACHE_SIZE = 100;

    /**
     * 创建一个容量为100的LRU缓存
     */
    public SimpleLRUCache() {
        container = new SimpleCache(DEFAULT_CACHE_SIZE, true);
    }

    /**
     * 创建一个指定容量的LRU缓存
     *
     * @param cacheSize 缓存的预期容量
     * @throws IllegalArgumentException 如果cacheSize小于1
     */
    public SimpleLRUCache(int cacheSize) {
        if (cacheSize < 1) {
            throw new IllegalArgumentException("缓存的容量不能小于1");
        }
        container = new SimpleCache(cacheSize, true);
    }

    public synchronized Object put(String key, Object value) {
        return container.put(key, value);
    }

    public synchronized Object putIfAbsent(String key, Object value) {
        return container.putIfAbsent(key, value);
    }

    public synchronized Object get(String key) {
        return container.get(key);
    }
}

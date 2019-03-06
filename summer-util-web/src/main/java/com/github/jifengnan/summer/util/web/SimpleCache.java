package com.github.jifengnan.summer.util.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * <p>一个简单的缓存。</p>
 * 缓存默认容量为100，以先进先出（FIFO）的方式淘汰数据。其逻辑可以通过参数cacheLogic更改。
 *
 * @author jifengnan  2019-01-22
 */
public class SimpleCache<K, V> extends LinkedHashMap<K, V> {
    private static final int DEFAULT_CACHE_SIZE = 100;
    private BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic;

    public SimpleCache(int capacity, float loadFactor, BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic) {
        super(capacity, loadFactor);
        setCacheLogic(cacheLogic);
    }

    public SimpleCache(int initialCapacity, BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic) {
        super(initialCapacity);
        setCacheLogic(cacheLogic);
    }

    /**
     * 创建一个容量为100的，FIFO的缓存
     */
    public SimpleCache() {
        super(128, 1);
        this.cacheLogic = (m, e) -> m.size() >= DEFAULT_CACHE_SIZE;
    }

    /**
     * 创建一个容量为100的指定策略的缓存
     *
     * @param cacheLogic 指定的缓存策略
     */
    public SimpleCache(BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic) {
        super(128, 1);
        setCacheLogic(cacheLogic);
    }

    public SimpleCache(int initialCapacity, float loadFactor, boolean accessOrder, BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic) {
        super(initialCapacity, loadFactor, accessOrder);
        setCacheLogic(cacheLogic);
    }

    private void setCacheLogic(BiPredicate<Map<K, V>, Map.Entry<K, V>> cacheLogic) {
        if (cacheLogic == null) {
            throw new IllegalArgumentException("cacheLogic cannot be null");
        }
        this.cacheLogic = cacheLogic;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return cacheLogic.test(this, eldest);
    }
}

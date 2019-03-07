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
public class SimpleCache extends LinkedHashMap<String, Object> {
    private static final int DEFAULT_CACHE_SIZE = 100;
    private final BiPredicate<Map<String, Object>, Map.Entry<String, Object>> removeLogic;

    /**
     * 创建一个容量为100的FIFO缓存
     */
    public SimpleCache() {
        this(DEFAULT_CACHE_SIZE, false, SimpleCache::test);
    }

    /**
     * 创建一个指定容量的FIFO缓存
     *
     * @param capacity 指定容量
     */
    public SimpleCache(int capacity) {
        this(capacity, false, SimpleCache::test);
    }

    /**
     * 创建一个指定容量的缓存
     *
     * @param capacity    指定容量
     * @param accessOrder the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     */
    public SimpleCache(int capacity, boolean accessOrder) {
        this(capacity, accessOrder, SimpleCache::test);
    }

    /**
     * 创建一个指定容量指定移除逻辑的FIFO缓存
     *
     * @param capacity    指定容量
     * @param removeLogic 指定的移除逻辑
     */
    public SimpleCache(int capacity, BiPredicate<Map<String, Object>, Map.Entry<String, Object>> removeLogic) {
        this(capacity, false, removeLogic);
    }

    /**
     * 创建一个指定容量指定移除逻辑的缓存
     *
     * @param capacity    指定容量
     * @param accessOrder the ordering mode - <tt>true</tt> for access-order, <tt>false</tt> for insertion-order
     * @param removeLogic 指定的移除逻辑
     */
    public SimpleCache(int capacity, boolean accessOrder, BiPredicate<Map<String, Object>, Map.Entry<String, Object>> removeLogic) {
        super(capacity, 1, accessOrder);
        if (removeLogic == null) {
            throw new IllegalArgumentException("removeLogic cannot be null");
        }
        this.removeLogic = removeLogic;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
        return removeLogic.test(this, eldest);
    }


    private static boolean test(Map<String, Object> m, Map.Entry<String, Object> e) {
        return m.size() >= DEFAULT_CACHE_SIZE;
    }

}

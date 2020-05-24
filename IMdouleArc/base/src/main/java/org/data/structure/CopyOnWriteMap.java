package org.data.structure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * File: CopyOnWriteMap.java
 * Author: yuzhuzhang
 * Create: 2020/3/15 11:02 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/15 : Create CopyOnWriteMap.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class CopyOnWriteMap<K, V> implements Map<K, V>, Cloneable {

    private volatile Map<K, V> internalMap;

    public CopyOnWriteMap() {
        internalMap = new HashMap<K, V>();
    }

    public V put(K key, V value) {

        synchronized (this) {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            V val = newMap.put(key, value);
            internalMap = newMap;
            return val;
        }
    }

    @Nullable
    @Override
    public V remove(@Nullable Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(@Nullable Object key) {
        return false;
    }

    @Override
    public boolean containsValue(@Nullable Object value) {
        return false;
    }

    public V get(Object key) {
        return internalMap.get(key);
    }

    public void putAll(Map<? extends K, ? extends V> newData) {
        synchronized (this) {
            Map<K, V> newMap = new HashMap<K, V>(internalMap);
            newMap.putAll(newData);
            internalMap = newMap;
        }
    }

    @Override
    public void clear() {

    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return null;
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return null;
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}

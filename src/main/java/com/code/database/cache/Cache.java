package com.code.database.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K, V> extends LinkedHashMap<K, V> {
    private static Cache instance;
    private int size;

    public static<K, V> Cache<K, V> getInstance(int size) {
        if (instance == null) {
            instance = new Cache<K, V>(size);
        }
        return instance;
    }

    private Cache(int size){
        super(size, 0.75f, true);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    protected boolean removeEldestEntry (Map.Entry<K, V> eldest){
        return size() > size;
    }
}

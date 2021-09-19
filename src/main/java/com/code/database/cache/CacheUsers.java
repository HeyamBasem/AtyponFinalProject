package com.code.database.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheUsers <K, V> extends LinkedHashMap<K, V> {
    private static CacheUsers instance;
    private int size;

    public static<K, V> CacheUsers<K, V> getInstance(int size) {
        if (instance == null) {
            instance = new CacheUsers<K, V>(size);
        }
        return instance;
    }

    private CacheUsers(int size){
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

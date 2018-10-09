package ru.otus.shw6.engine;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MyCacheEngine<K, V> implements CacheEngine<K, V> {
    private final Map<K, SoftReference<V>> cache;

    public MyCacheEngine(final int cacheSize) {
        this.cache = new LinkedHashMap<K, SoftReference<V>>(cacheSize, 0.75f /*default*/, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> eldest) {
                return size() > cacheSize;
            }
        };
    }

    public void printStats(){
        System.out.println("Objects in cache: " + cache.size());
        System.out.println("Null count = " + cache.entrySet().stream().filter(entry -> entry.getValue().get() == null).count());
        System.out.println("Other stats = " + cache.entrySet().stream().filter(entry -> entry.getValue().get() != null)
                .collect(Collectors.groupingBy(entry -> entry.getValue().get().getClass().getSimpleName(), Collectors.counting())));
//        cache.entrySet().stream().filter(entry -> entry.getValue().get() != null)
//                .forEach(entry -> System.out.println(entry.getValue().get().toString()));
    }

    public V put(K key, V value) {
        SoftReference<V> prevVal = cache.put(key, new SoftReference<>(value));
        return prevVal != null ? prevVal.get() : null;
    }

    public V get(K key) {
        SoftReference<V> valueReference = cache.get(key);
        return valueReference != null ? valueReference.get() : null;
    }
}

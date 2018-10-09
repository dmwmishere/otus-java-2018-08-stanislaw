package ru.otus.shw6.engine;

public interface CacheEngine<K, V> {
    V get(K key);
    V put(K key, V value);
    void printStats();
}

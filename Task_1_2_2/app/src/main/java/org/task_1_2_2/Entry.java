package org.task_1_2_2;

class Entry<K, V> {
    K key;
    V value;
    Entry<K, V> next;

    public Entry(K key, V value) {
        this(key, value, null);
    }

    public Entry(K key, V value, Entry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Entry<K, V> getNext() {
        return next;
    }
}

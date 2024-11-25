package org.task_1_2_2;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HashTable<K, V> implements Iterable<Entry<K, V>> {

    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private int expectedModCount = modCount;
        private int index = 0;
        private Entry<K, V> next = null;
        private Entry<K, V> current = null;

        public HashTableIterator() {
            while (index < table.length && table[index] == null) {
                index++;
            }
            if (index < table.length) {
                next = table[index];
            }
        }

        @Override
        public boolean hasNext() {
            checkForComodification();
            return next != null;
        }

        @Override
        public Entry<K, V> next() {
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            current = next;
            next = current.next;
            if (next == null) {
                index++;
                while (index < table.length && table[index] == null) {
                    index++;
                }
                if (index < table.length) {
                    next = table[index];
                }
            }
            return current;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            checkForComodification();
            HashTable.this.remove(current.key);
            current = null;
            expectedModCount = modCount;
        }

        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public static int getInitialCapacity() {
        return INITIAL_CAPACITY;
    }

    public static double getLoadFactor() {
        return LOAD_FACTOR;
    }

    private Entry<K, V>[] table;
    private int size;
    private int modCount;
    private int threshold;

    @SuppressWarnings("unchecked")
    public HashTable() {
        this.table = new Entry[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
        this.threshold = (int) (INITIAL_CAPACITY * LOAD_FACTOR);
    }

    public HashTable(final Entry<K, V>[] table, final int size, final int modCount, final int threshold) {
        this.table = table;
        this.size = size;
        this.modCount = modCount;
        this.threshold = threshold;
    }

    public void put(final K key, final V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        final int index = hash(key);
        if (table[index] == null) {
            table[index] = new Entry<>(key, value);
            size++;
            modCount++;
            if (size >= threshold) {
                resize();
            }
        } else {
            var current = table[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                if (current.next == null) {
                    current.next = new Entry<>(key, value);
                    size++;
                    modCount++;
                    if (size >= threshold) {
                        resize();
                    }
                    return;
                }
                current = current.next;
            }
        }
    }

    public V get(final K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        final int index = hash(key);
        var current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public void remove(final K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        final int index = hash(key);
        var current = table[index];
        Entry<K, V> prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                modCount++;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public boolean containsKey(final K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        final int index = hash(key);
        var current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void update(final K key, final V value) {
        if (!containsKey(key)) {
            throw new IllegalArgumentException("Key not found");
        }
        put(key, value);
    }

    int hash(final K key) {
        final int hashCode = (key == null) ? 0 : key.hashCode();
        return hashCode & (table.length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        final var oldTable = table;
        final int newCapacity = table.length * 2;
        table = new Entry[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);
        size = 0;
        for (var entry : oldTable) {
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (var entry : this) {
            result = prime * result + ((entry.key == null) ? 0 : entry.key.hashCode());
            result = prime * result + ((entry.value == null) ? 0 : entry.value.hashCode());
        }
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @SuppressWarnings("unchecked")
        var that = (HashTable<K, V>) o;
        if (size != that.size) return false;
        for (var entry : this) {
            V thatValue = that.get(entry.key);
            if (!Objects.equals(thatValue, entry.value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        final var sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < table.length; i++) {
            var current = table[i];
            while (current != null) {
                sb.append(current.key.toString()).append("=").append(current.value.toString()).append(", ");
                current = current.next;
            }
        }
        sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    public Entry<K, V>[] getTable() {
        return table;
    }

    public void setTable(final Entry<K, V>[] table) {
        this.table = table;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public int getModCount() {
        return modCount;
    }

    public void setModCount(final int modCount) {
        this.modCount = modCount;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(final int threshold) {
        this.threshold = threshold;
    }
}
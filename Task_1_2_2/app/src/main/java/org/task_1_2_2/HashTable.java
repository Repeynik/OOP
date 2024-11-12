package org.task_1_2_2;

import java.util.*;

public class HashTable<K, V> {
    private static class Entry<K, V> {
        K key;
        List<V> values;

        Entry(K key, V value) {
            this.key = key;
            this.values = new ArrayList<>(Collections.singletonList(value));
        }
    }

    private Map<K, Entry<K, V>> map;
    private int size;
    private int modCount;

    public HashTable() {
        this.map = new HashMap<>();
        this.size = 0;
        this.modCount = 0;
    }

    public void put(K key, V value) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            entry.values.add(value);
        } else {
            entry = new Entry<>(key, value);
            map.put(key, entry);
            size++;
        }
        modCount++;
    }

    public void update(K key, V newValue) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (!entry.values.isEmpty()) {
                entry.values.set(0, newValue);
            }
            modCount++;
        } else {
            throw new NoSuchElementException("Ключ не найден: " + key);
        }
    }

    public void update(K key, V newValue, int index) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (index >= 0 && index < entry.values.size()) {
                entry.values.set(index, newValue);
                modCount++;
            } else {
                throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
            }
        } else {
            throw new NoSuchElementException("Ключ не найден: " + key);
        }
    }
    
    
    public void remove(K key) {
        if (map.remove(key) != null) {
            size--;
            modCount++;
        }
    }

    public void remove(K key, int index) {
        Entry<K, V> entry = map.get(key);
        if (entry != null) {
            if (index >= 0 && index < entry.values.size()) {
                entry.values.remove(index);
                modCount++;
            } else {
                throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
            }
        } else {
            throw new NoSuchElementException("Ключ не найден: " + key);
        }
    }

    public List<V> get(K key) {
        Entry<K, V> entry = map.get(key);
        return entry != null ? new ArrayList<>(entry.values) : Collections.emptyList();
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private Iterator<Entry<K, V>> currentIterator = map.values().iterator();
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return currentIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return currentIterator.next();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof HashTable)) return false;

        @SuppressWarnings("unchecked")
        HashTable<K, V> other = (HashTable<K, V>) obj;

        if (this.size != other.size) return false;

        for (Entry<K, V> entry : map.values()) {
            List<V> otherValues = other.get(entry.key);
            if (!Objects.equals(entry.values, otherValues)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Entry<K, V> entry : map.values()) {
            sb.append(entry.key).append("=").append(entry.values).append(", ");
        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);
        hashTable.put("one", 1.0); 
        
        hashTable.put("one", 1.0);
       
        hashTable.put("one", 1.0); 
       
        System.out.println(hashTable.get("one"));
        
        hashTable.update("one", 10, 1); 
        System.out.println(hashTable.get("one"));
        
        System.out.println(hashTable);

        hashTable.remove("one", 1);

        System.out.println(hashTable);
    }
}

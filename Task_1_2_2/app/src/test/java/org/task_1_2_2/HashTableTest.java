package org.task_1_2_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {
    private HashTable<String, Integer> hashTable;

    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    @Test
    void testPutAndGet() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("one", 3);

        List<Integer> valuesOne = hashTable.get("one");
        List<Integer> valuesTwo = hashTable.get("two");

        assertEquals(List.of(1, 3), valuesOne);
        assertEquals(List.of(2), valuesTwo);
    }

    @Test
    void testUpdate() {
        hashTable.put("one", 1);
        hashTable.update("one", 10);
        assertEquals(List.of(10), hashTable.get("one"));

        hashTable.put("one", 20);
        hashTable.update("one", 15, 1); 
        assertEquals(List.of(10, 15), hashTable.get("one"));
    }

    @Test
    void testUpdateNonExistentKey() {
        assertThrows(NoSuchElementException.class, () -> {
            hashTable.update("nonexistent", 1);
        });
    }

    @Test
    void testUpdateWithIndexOutOfBounds() {
        hashTable.put("one", 1);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            hashTable.update("one", 2, 1);
        });
    }

    @Test
    void testRemove() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.remove("one");
        assertFalse(hashTable.containsKey("one"));
        assertTrue(hashTable.containsKey("two"));
    }

    @Test
    void testContainsKey() {
        hashTable.put("one", 1);
        assertTrue(hashTable.containsKey("one"));
        assertFalse(hashTable.containsKey("two"));
    }

    @Test
    void testEquals() {
        HashTable<String, Integer> anotherHashTable = new HashTable<>();
        hashTable.put("one", 1);
        anotherHashTable.put("one", 1);
        assertEquals(hashTable, anotherHashTable);

        anotherHashTable.put("two", 2);
        hashTable.put("two", 2);
        assertEquals(hashTable, anotherHashTable);

        anotherHashTable.put("three", 3);
        assertNotEquals(hashTable, anotherHashTable);
    }

    @Test
    void testToString() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        String expected = "{one=[1], two=[2]}";
        assertEquals(expected, hashTable.toString());
    }
}

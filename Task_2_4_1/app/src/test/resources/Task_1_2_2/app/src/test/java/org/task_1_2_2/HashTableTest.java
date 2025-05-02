package Task_1_2_2.app.src.test.java.org.task_1_2_2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Task_1_2_2.app.src.main.java.org.task_1_2_2.HashTable;

import java.util.ConcurrentModificationException;

public class HashTableTest {

    @Test
    public void testStaticMethods() {
        assertEquals(16, HashTable.getInitialCapacity());
        assertEquals(0.75, HashTable.getLoadFactor(), 0.0001);
    }

    @Test
    public void testPutAndGet() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        hashTable.put("carrot", "vegetable");
        assertEquals("fruit", hashTable.get("apple"));
        assertEquals("vegetable", hashTable.get("carrot"));
        assertNull(hashTable.get("banana"));
    }

    @Test
    public void testRemove() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        hashTable.remove("apple");
        assertNull(hashTable.get("apple"));
        assertEquals(0, hashTable.getSize());
    }

    @Test
    public void testContainsKey() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        assertTrue(hashTable.containsKey("apple"));
        assertFalse(hashTable.containsKey("banana"));
    }

    @Test
    public void testUpdateExistingKey() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        hashTable.update("apple", "green fruit");
        assertEquals("green fruit", hashTable.get("apple"));
    }


    @Test
    public void testConcurrentModificationDuringIteration() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        var iterator = hashTable.iterator();
        hashTable.put("carrot", "vegetable");
        assertThrows(ConcurrentModificationException.class, () -> iterator.next());
    }


    @Test
    public void testPutWithNullKey() {
        var hashTable = new HashTable<>();
        assertThrows(IllegalArgumentException.class, () -> hashTable.put(null, "value"));
    }

    @Test
    public void testEqualsAndHashCode() {
        var hashTable1 = new HashTable<>();
        hashTable1.put("apple", "fruit");
        var hashTable2 = new HashTable<>();
        hashTable2.put("apple", "fruit");
        assertEquals(hashTable1, hashTable2);
        assertEquals(hashTable1.hashCode(), hashTable2.hashCode());
    }

    @Test
    public void testToString() {
        var hashTable = new HashTable<>();
        hashTable.put("apple", "fruit");
        hashTable.put("carrot", "vegetable");
        var toString = hashTable.toString();
        assertTrue(toString.contains("apple=fruit"));
        assertTrue(toString.contains("carrot=vegetable"));
        assertTrue(toString.startsWith("{") && toString.endsWith("}"));
    }
}

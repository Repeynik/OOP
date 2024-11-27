package org.task_1_2_2;

public class Main {
    public static void main(final String[] args) {
        final HashTable<String, Number> hashTable = new HashTable<>();

        hashTable.put("one", 1);
        System.out.println(hashTable.get("one")); // Output: 1
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one")); // Output: 1.0

        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);

        System.out.println(hashTable); // Output: {one=1.0, two=2, three=3}

        hashTable.remove("two");
        System.out.println(hashTable.containsKey("two")); // Output: false

        // Iteration example
        for (final var entry : hashTable) {
            System.out.println(entry.key + " = " + entry.value);
        }
        // Output:
        // one = 1.0
        // three = 3
    }
}

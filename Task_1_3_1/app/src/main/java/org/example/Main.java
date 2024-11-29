package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            var result =
                    SubstringFinder.find(
                            "Task_1_3_1\\app\\src\\main\\java\\org\\example\\input.txt", "我爱你");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

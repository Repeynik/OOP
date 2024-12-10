package org.example;

import java.util.Collections;

// Header class
public class Header extends Element {
    private int level;
    private String content;
    public Header(int level, String content) {
        this.level = level;
        this.content = content;
    }
    @Override
    public String serialize() {
        return String.join("", Collections.nCopies(level, "#")) + " " + content + "\n";
    }
}
package org.example;
// Quote class
public class Quote extends Element {
    private String content;
    public Quote(String content) {
        this.content = content;
    }
    @Override
    public String serialize() {
        return "> " + content + "\n";
    }
}
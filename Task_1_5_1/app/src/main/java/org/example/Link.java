package org.example;

// Link class
public class Link extends Element {
    private String text;
    private String url;

    public Link(String text, String url) {
        this.text = text;
        this.url = url;
    }

    @Override
    public String serialize() {
        return "[" + text + "](" + url + ")";
    }
}

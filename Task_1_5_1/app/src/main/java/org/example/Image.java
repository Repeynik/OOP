package org.example;

// Image class
public class Image extends Element {
    private String altText;
    private String source;

    public Image(String altText, String source) {
        this.altText = altText;
        this.source = source;
    }

    @Override
    public String serialize() {
        return "![" + altText + "](" + source + ")";
    }
}

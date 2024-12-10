package org.example;
// Text and formatting classes
public class Text extends Element {
    protected String content;
    public Text(String content) {
        this.content = content;
    }
    @Override
    public String serialize() {
        return content;
    }
    public static class Bold extends Text {
        public Bold(String content) {
            super("**" + content + "**");
        }
    }
    public static class Italic extends Text {
        public Italic(String content) {
            super("*" + content + "*");
        }
    }
    public static class Strikethrough extends Text {
        public Strikethrough(String content) {
            super("~~" + content + "~~");
        }
    }
    public static class Code extends Text {
        public Code(String content) {
            super("`" + content + "`");
        }
    }
}

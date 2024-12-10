package org.example;
// CodeBlock class
public class CodeBlock extends Element {
    private String content;
    private String language;
    public CodeBlock(String content) {
        this(content, null);
    }
    public CodeBlock(String content, String language) {
        this.content = content;
        this.language = language;
    }
    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder("``");
        if (language != null) {
            sb.append("```").append(language).append("\n");
        } else {
            sb.append("`").append("\n");
        }
        sb.append(content).append("\n");
        sb.append("``").append(language != null ? "``" : "`");
        return sb.toString();
    }
}
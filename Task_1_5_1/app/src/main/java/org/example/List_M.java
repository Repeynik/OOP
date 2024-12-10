package org.example;
import java.util.List;
import java.util.ArrayList;

// List class
public class List_M extends Element {
    private List<String> items;
    private boolean ordered;
    public List_M(boolean ordered) {
        this.ordered = ordered;
        this.items = new ArrayList<>();
    }
    public void addItem(String item) {
        items.add(item);
    }
    @Override
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (ordered) {
                sb.append(i+1).append(". ").append(items.get(i)).append("\n");
            } else {
                sb.append("- ").append(items.get(i)).append("\n");
            }
        }
        return sb.toString();
    }
}
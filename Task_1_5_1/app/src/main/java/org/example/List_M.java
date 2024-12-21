package org.example;

import java.util.ArrayList;
import java.util.List;

public class List_M extends Element {
    private List<String> items;
    private boolean ordered;
    private String bullet;

    public List_M(boolean ordered) {
        this(ordered, "-");
    }

    public List_M(boolean ordered, String bullet) {
        this.ordered = ordered;
        this.bullet = bullet;
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
                sb.append(i + 1).append(". ").append(items.get(i)).append("\n");
            } else {
                sb.append(bullet).append(" ").append(items.get(i)).append("\n");
            }
        }
        return sb.toString();
    }
}

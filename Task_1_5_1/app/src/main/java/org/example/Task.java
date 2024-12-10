package org.example;
// Task class
public class Task extends Element {
    private boolean completed;
    private String content;
    public Task(boolean completed, String content) {
        this.completed = completed;
        this.content = content;
    }
    @Override
    public String serialize() {
        return "[" + (completed ? "x" : " ") + "] " + content;
    }
}
package org.example;

// Base Element class
public abstract class Element {
    public abstract String serialize();
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Element element = (Element) obj;
        return serialize().equals(element.serialize());
    }
}
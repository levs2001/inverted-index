package ru.leo.doc;

import java.util.Set;

public record Document(long id, Set<String> terms) {
    public static Document create(long id, Set<String> terms) {
        return new Document(id, terms);
    }
}

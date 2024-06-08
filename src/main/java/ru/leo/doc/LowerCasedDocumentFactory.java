package ru.leo.doc;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class LowerCasedDocumentFactory implements IDocumentFactory {
    private static final String regex = "[\\p{Punct}\\s]+";

    /**
     * Create document with lower cased terms without punctuation.
     */
    @Override
    public Document create(long id, String text) {
        String[] termsArr = text.split(regex);
        Set<String> terms = Arrays.stream(termsArr).map(String::toLowerCase).collect(Collectors.toSet());
        return Document.create(id, terms);
    }
}

package ru.leo.search.doc;

public interface IDocumentFactory {
    Document create(long id, String text);
}

package ru.leo.doc;

public interface IDocumentFactory {
    Document create(long id, String text);
}

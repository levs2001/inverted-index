package ru.leo.index;

import java.nio.file.Path;
import ru.leo.doc.Document;

/**
 * Accumulate documents and can save them to index.
 */
public interface IIndexCreator {
    void addDocument(Document document);

    IInvertedIndex saveAndGet(Path dir);
}

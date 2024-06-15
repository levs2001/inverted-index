package ru.leo.search.index;

import java.util.List;
import ru.leo.search.doc.Document;

public interface IIndexer {
    IInvertedIndex makeIndex(List<Document> documents);
}

package ru.leo.index;

import java.util.List;
import ru.leo.doc.Document;

public interface IInvertedIndex {
    // TODO: Нужен булев поиск
    List<Document> find(String term);
}

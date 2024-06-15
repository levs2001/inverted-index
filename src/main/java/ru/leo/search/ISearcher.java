package ru.leo.search;

import java.util.List;
import java.util.Map;
import ru.leo.search.doc.Document;
import ru.leo.search.expression.Expression;

public interface ISearcher {
    void index(Map<Long, String> texts);

    void index(List<Document> documents);

    List<Long> search(Expression expression);
}

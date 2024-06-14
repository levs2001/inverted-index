package ru.leo.search;

import java.util.List;
import java.util.Map;
import ru.leo.doc.Document;
import ru.leo.doc.IDocumentFactory;
import ru.leo.doc.LowerCasedDocumentFactory;
import ru.leo.expression.Expression;
import ru.leo.index.IInvertedIndex;
import ru.leo.index.InvertedIndexUtil;

public class Searcher implements ISearcher {
    private final IDocumentFactory documentFactory = new LowerCasedDocumentFactory();
    private IInvertedIndex index;

    @Override
    public void index(Map<Long, String> texts) {
        List<Document> docs = texts.entrySet().stream().map(e -> documentFactory.create(e.getKey(), e.getValue())).toList();
        index = InvertedIndexUtil.makeIndex(docs);
    }

    @Override
    public List<Long> search(Expression expression) {
        return index.find(expression);
    }
}

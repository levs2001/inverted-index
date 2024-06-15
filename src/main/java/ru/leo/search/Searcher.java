package ru.leo.search;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leo.search.doc.Document;
import ru.leo.search.doc.IDocumentFactory;
import ru.leo.search.doc.LowerCasedDocumentFactory;
import ru.leo.search.expression.Expression;
import ru.leo.search.index.IIndexer;
import ru.leo.search.index.IInvertedIndex;
import ru.leo.search.index.Indexer;
import ru.leo.stats.IStats;

public class Searcher implements ISearcher {
    private static final Logger log = LoggerFactory.getLogger(Searcher.class);
    private static final String SEARCH_STAT_TAG_PREFIX = "search_nanos_";
    private final IDocumentFactory documentFactory = new LowerCasedDocumentFactory();
    private final IIndexer indexer;
    private final IStats stats;

    private IInvertedIndex index;

    public Searcher(IStats stats) {
        this.stats = stats;
        indexer = new Indexer(stats);
    }

    @Override
    public void index(Map<Long, String> texts) {
        log.info("Starting index process...");
        List<Document> documents = texts.entrySet().stream().map(e -> documentFactory.create(e.getKey(), e.getValue())).toList();
        log.info("Documents created.");
        index(documents);
    }

    @Override
    public void index(List<Document> documents) {
        log.info("Starting index process...");
        index = indexer.makeIndex(documents);
        log.info("Index created.");
    }

    @Override
    public List<Long> search(Expression expression) {
        long start = System.nanoTime();
        List<Long> res = index.find(expression);
        stats.addStat(SEARCH_STAT_TAG_PREFIX + expression.getOperation(), System.nanoTime() - start);

        return res;
    }
}

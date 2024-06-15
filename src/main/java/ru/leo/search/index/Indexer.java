package ru.leo.search.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leo.search.doc.Document;
import ru.leo.search.list.DocIdList;
import ru.leo.search.list.IPostingList;
import ru.leo.stats.IStats;

public class Indexer implements IIndexer {
    private static final Logger log = LoggerFactory.getLogger(Indexer.class);
    private static final String TERM_DOCS_STAT_TAG = "create_term_docs_nanos";
    private static final String POSTING_LISTS_STAT_TAG = "create_posting_lists_nanos";
    private static final String TERMS_COUNT_STAT_TAG = "terms_count";

    private final IStats stats;

    public Indexer(IStats stats) {
        this.stats = stats;
    }

    public IInvertedIndex makeIndex(List<Document> documents) {
        log.info("Creating term docs...");
        long start = System.nanoTime();
        Map<String, Set<Long>> termDocs = createTermDocs(documents);
        stats.addStat(TERM_DOCS_STAT_TAG, System.nanoTime() - start);
        log.info("Term docs created. Terms count: {}", termDocs.size());
        stats.addStat(TERMS_COUNT_STAT_TAG, termDocs.size());

        start = System.nanoTime();
        log.info("Creating posting lists...");
        Map<String, IPostingList> postingLists = createPostingLists(termDocs);
        stats.addStat(POSTING_LISTS_STAT_TAG, System.nanoTime() - start);
        log.info("Posting lists created.");
        return new InvertedIndex(postingLists);
    }

    private static Map<String, IPostingList> createPostingLists(Map<String, Set<Long>> termDocs) {
        Map<String, IPostingList> postingLists = new HashMap<>(termDocs.size());
        for (Map.Entry<String, Set<Long>> td : termDocs.entrySet()) {
            postingLists.put(td.getKey(), DocIdList.create(td.getValue()).iterator());
        }

        return postingLists;
    }

    private static Map<String, Set<Long>> createTermDocs(List<Document> documents) {
        Map<String, Set<Long>> termDocs = new HashMap<>();
        for (Document document : documents) {
            for (String term : document.terms()) {
                termDocs.computeIfAbsent(term, (t) -> new HashSet<>());
                termDocs.get(term).add(document.id());
            }
        }

        return termDocs;
    }
}

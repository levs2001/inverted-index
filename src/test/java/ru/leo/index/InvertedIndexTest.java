package ru.leo.index;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.leo.search.expression.Expression.and;
import static ru.leo.search.expression.Expression.term;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.leo.search.doc.Document;
import ru.leo.search.index.IIndexer;
import ru.leo.search.index.IInvertedIndex;
import ru.leo.search.index.Indexer;
import ru.leo.stats.StatsImpl;

class InvertedIndexTest {
    private static final IIndexer indexer = new Indexer(new StatsImpl());

    @Test
    public void testAnd() {
        List<Document> docs = List.of(
            Document.create(2L, Set.of("hi", "jack")),
            Document.create(3L, Set.of("hi", "oleg", "and", "bob"))
        );
        IInvertedIndex index = indexer.makeIndex(docs);

        assertTrue(index.find(and(term("bob"), term("jack"))).isEmpty());
        assertEquals(List.of(3L), index.find(and(term("oleg"), term("bob"))));
    }

    @Test
    public void testBasic() {
        List<Document> docs = List.of(
            Document.create(1L, Set.of("hi", "bob")),
            Document.create(2L, Set.of("hi", "oleg")),
            Document.create(3L, Set.of("hi", "oleg", "and", "bob")),
            Document.create(4L, Set.of("hi", "jack"))
        );

        IInvertedIndex index = indexer.makeIndex(docs);

        assertEquals(List.of(1L, 2L, 3L, 4L), index.find(term("hi")));
        assertEquals(List.of(1L, 3L), index.find(term("bob")));
        assertEquals(List.of(3L), index.find(and(term("bob"), term("oleg"))));
        assertTrue(index.find(term("nn")).isEmpty());
        assertTrue(index.find(and(term("bob"), term("jack"))).isEmpty());
    }

}
package ru.leo.search;

import static org.junit.jupiter.api.Assertions.*;
import static ru.leo.search.expression.Expression.and;
import static ru.leo.search.expression.Expression.term;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.leo.stats.StatsImpl;

class SearcherTest {
    private static final Map<Long, String> textsBase = Map.of(
        1L, "Ty na tancpole igrala",
        2L, "Snizu manila menya",
        3L, "Ya tamada",

        4L, "Day mne slovo ved ya tamada",
        5L, "Tama tamada"
    );

    @Test
    public void testBase() {
        ISearcher searcher = new Searcher(new StatsImpl());
        searcher.index(textsBase);

        assertEquals(List.of(1L), searcher.search(term("igrala")));
        assertEquals(List.of(3L, 4L, 5L), searcher.search(term("tamada")));
        assertEquals(List.of(3L, 4L), searcher.search(and(term("ya"), term("tamada"))));
    }
}
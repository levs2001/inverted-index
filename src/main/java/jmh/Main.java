package jmh;

import static ru.leo.search.expression.Expression.and;
import static ru.leo.search.expression.Expression.term;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leo.parser.IParser;
import ru.leo.parser.WikiJsonParser;
import ru.leo.search.ISearcher;
import ru.leo.search.Searcher;
import ru.leo.search.doc.Document;
import ru.leo.search.expression.Expression;
import ru.leo.stats.IStats;
import ru.leo.stats.StatsImpl;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final String DOCS_COUNT_TAG = "documents_count";

    public static void main(String[] args) throws IOException {
        log.info("Max memory: {} mb", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        String textsFilename = args[0];
        String statsFilename = args[1];

        IStats stats = new StatsImpl();
        List<Document> documents = parse(textsFilename);
        stats.addStat(DOCS_COUNT_TAG, documents.size());

        ISearcher searcher = new Searcher(stats);
        searcher.index(documents);

        String[] searchTerms = new String[] {
            "hello", "russia", "apple", "pen", "nail", "foot", "door", "window", "lamp"
        };
        Arrays.stream(searchTerms).forEach(t -> makeTermSearch(searcher, t));

        for (int i = 0; i < searchTerms.length; i++) {
            for (int j = 0; j < searchTerms.length; j++) {
                if (i == j) {
                    continue;
                }

                makeAndSearch(searcher, searchTerms[i], searchTerms[j]);
            }
        }

        log.info("Stats: {}", stats.getStatAvg());
        Files.write(Path.of(statsFilename), stats.getStatAvg().toString().getBytes());
    }

    private static List<Document> parse(String file) {
        IParser parser = new WikiJsonParser();
        List<Document> documents = parser.parse(Path.of(file));
        log.info("Parsed {} texts.", documents.size());
        return documents;
    }

    private static void makeTermSearch(ISearcher searcher, String term) {
        log.info("{} result: {}", term, searcher.search(term("hello")));
    }

    private static void makeAndSearch(ISearcher searcher, String... terms) {
        Expression[] termExpressions = Arrays.stream(terms).map(Expression::term).toArray(Expression[]::new);
        log.info("And {} result: {}", Arrays.toString(terms), searcher.search(and(termExpressions)));
    }
}

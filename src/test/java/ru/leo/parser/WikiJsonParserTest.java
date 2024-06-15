package ru.leo.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leo.search.doc.Document;

class WikiJsonParserTest {
    private static final Path resourcesPath = Paths.get("src", "test", "resources");
    private static final Logger log = LoggerFactory.getLogger(WikiJsonParserTest.class);

    @Test
    public void testParser() {
        IParser parser = new WikiJsonParser();
        List<Document> res = parser.parse(resourcesPath.resolve("test_wiki.json"));
        assertEquals(3, res.size());
        assertEquals(
            List.of(
                Document.create(12L, Set.of("anarchism", "is", "a", "political")),
                Document.create(39L, Set.of("albedo", "is", "the", "fraction", "of")),
                Document.create(290L, Set.of("a", "or", "is", "the", "first", "letter", "and"))
            ),
            res
        );
    }

    @Test
    public void testWiki() {
        IParser parser = new WikiJsonParser();
        List<Document> res = parser.parse(resourcesPath.resolve("wiki_small.json"));
        assertEquals(10, res.size());
    }
}
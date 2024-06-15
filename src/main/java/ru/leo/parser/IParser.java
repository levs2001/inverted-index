package ru.leo.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import ru.leo.search.doc.Document;

public interface IParser {
    List<Document> parse(Path file);
}

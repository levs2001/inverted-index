package ru.leo.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.leo.search.doc.Document;

public class WikiJsonParser implements IParser {
    private static final Logger log = LoggerFactory.getLogger(WikiJsonParser.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(WikiData.class, new WikiDataDesirializer());
        mapper.registerModule(module);
    }

    @Override
    public List<Document> parse(Path file) {
        WikiData data;
        try {
            data = mapper.readValue(file.toFile(), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Can't parse json file", e);
            throw new RuntimeException(e);
        }

        return data.documents();
    }
}

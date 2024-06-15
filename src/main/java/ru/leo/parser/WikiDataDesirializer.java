package ru.leo.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ru.leo.search.doc.Document;
import ru.leo.search.doc.IDocumentFactory;
import ru.leo.search.doc.LowerCasedDocumentFactory;

public class WikiDataDesirializer extends StdDeserializer<WikiData> {
    private final IDocumentFactory documentFactory = new LowerCasedDocumentFactory();

    public WikiDataDesirializer() {
        super(WikiData.class);
    }

    @Override
    public WikiData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Map<Integer, Long> ids = new HashMap<>();

        Iterator<Map.Entry<String, JsonNode>> idIterator = node.get("id").fields();
        while (idIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = idIterator.next();
            int num = Integer.parseInt(entry.getKey());
            long value = entry.getValue().asLong();
            ids.put(num, value);
        }

        List<Document> documents = new ArrayList<>(ids.size());
        Iterator<Map.Entry<String, JsonNode>> textIterator = node.get("text").fields();
        while (textIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = textIterator.next();
            int num = Integer.parseInt(entry.getKey());
            String text = entry.getValue().asText();
            documents.add(documentFactory.create(ids.get(num), text));
        }

        return new WikiData(documents);
    }
}

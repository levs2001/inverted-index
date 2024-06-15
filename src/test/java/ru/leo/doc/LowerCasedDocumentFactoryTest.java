package ru.leo.doc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.leo.search.doc.Document;
import ru.leo.search.doc.IDocumentFactory;
import ru.leo.search.doc.LowerCasedDocumentFactory;

class LowerCasedDocumentFactoryTest {
    @Test
    public void testSimpleText() {
        String text = "Mama myla ramu, a PAPA don't mil, a mama myla";

        IDocumentFactory documentFactory = new LowerCasedDocumentFactory();
        Document document = documentFactory.create(1, text);

        assertEquals(1, document.id());
        assertEquals(Set.of("mama", "myla", "ramu", "a", "papa", "don", "t", "mil"), document.terms());
    }
}
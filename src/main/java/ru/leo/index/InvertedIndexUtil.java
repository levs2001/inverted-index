package ru.leo.index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.leo.doc.Document;
import ru.leo.list.DocIdList;
import ru.leo.list.IPostingList;

public class InvertedIndexUtil {
    public static IInvertedIndex makeIndex(List<Document> documents) {
        Map<String, Set<Long>> termDocs = createTermDocs(documents);
        Map<String, IPostingList> postingLists = createPostingLists(termDocs);

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

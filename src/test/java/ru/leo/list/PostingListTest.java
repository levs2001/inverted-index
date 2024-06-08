package ru.leo.list;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.Test;

class PostingListTest {
    @Test
    public void testPostingListSimple() {
        List<Long> idsOrd = List.of(1L, 2L, 3L);
        IPostingList postingList = DocIdList.create(idsOrd).iterator();
        assertEquals(idsOrd, fromPostingList(postingList));
    }

    @Test
    public void testUnorderedSimple() {
        List<Long> idsOrd = List.of(10L, 6L, 3L);
        IPostingList postingList = DocIdList.create(idsOrd).iterator();
        List<Long> expectedSorted = idsOrd.stream().sorted().toList();
        assertEquals(expectedSorted, fromPostingList(postingList));
    }

    @Test
    public void testMany() {
        int size = 1_000_000;
        List<Long> expected = new ArrayList<>(size);
        for (long i = 0; i < size; i++) {
            expected.add(i);
        }
        IPostingList postingList = DocIdList.create(expected).iterator();
        assertEquals(expected, fromPostingList(postingList));
    }

    @Test
    public void testManyRandom() {
        int size = 1_000_000;
        List<Long> expected = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            expected.add(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE));
        }
        IPostingList postingList = DocIdList.create(expected).iterator();
        expected.sort(Long::compare);
        assertEquals(expected, fromPostingList(postingList));
    }

    List<Long> fromPostingList(IPostingList postingList) {
        List<Long> ids = new ArrayList<>();
        while (postingList.hasNext()) {
            ids.add(postingList.next());
        }
        return ids;
    }
}
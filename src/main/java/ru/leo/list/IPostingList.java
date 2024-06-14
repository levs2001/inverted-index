package ru.leo.list;

public interface IPostingList {
    boolean hasNext();
    /**
     * Get next doc id. (list pointer will be moved to it).
     */
    long next();

    /**
     * Get current doc id from list (pointer on it).
     * If no next operation was performed will return first doc id.
     */
    long current();

    long advance(long docId);

    int size();

    /**
     * always reset before use.
     */
    void reset();
}
